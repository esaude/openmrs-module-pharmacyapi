/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.prescription.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription.PrescriptionStatus;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionGenerator {
	
	@Autowired
	private PrescriptionItemFactory prescriptionItemFactory;
	
	private Concept precriptionDateConcept = null;
	
	public List<Prescription> generatePrescriptions(final List<DrugOrder> drugOrders, final Date creationDate)
	        throws PharmacyBusinessException {
		
		final List<Prescription> result = new ArrayList<>();
		
		final Map<Encounter, List<DrugOrder>> drugOrdersByEncounter = this.groupDrugOrdersByEncounter(drugOrders);
		
		for (final Entry<Encounter, List<DrugOrder>> ordersByEncounter : drugOrdersByEncounter.entrySet()) {
			
			final Prescription prescription = this.preparePrescription(ordersByEncounter.getKey());
			final List<PrescriptionItem> prescriptionItems = this.prescriptionItemFactory
			        .generatePrescriptionItems(prescription, creationDate, ordersByEncounter.getValue());
			prescription.setPrescriptionItems(prescriptionItems);
			prescription.setPrescriptionStatus(this.calculatePrescriptioStatus(prescriptionItems));
			result.add(prescription);
		}
		
		return result;
	}
	
	private Map<Encounter, List<DrugOrder>> groupDrugOrdersByEncounter(final List<DrugOrder> drugOrders)
	        throws PharmacyBusinessException {
		
		final PrescriptionDispensationService prescriptionDispensationService = Context
		        .getService(PrescriptionDispensationService.class);
		final Map<Encounter, List<DrugOrder>> mapped = new HashMap<>();
		
		for (final DrugOrder drugOrder : drugOrders) {
			
			Encounter encounter = drugOrder.getEncounter();
			if (!Action.NEW.equals(drugOrder.getAction())) {
				
				final PrescriptionDispensation prescriptionDispensation = prescriptionDispensationService
				        .findPrescriptionDispensationByDispensation(encounter);
				encounter = prescriptionDispensation.getPrescription();
			}
			List<DrugOrder> list = mapped.get(encounter);
			if (list == null) {
				mapped.put(encounter, list = new ArrayList<>());
			}
			list.add(drugOrder);
		}
		return mapped;
	}
	
	private Prescription preparePrescription(final Encounter encounter) {
		
		final Order anyOrder = encounter.getOrders().iterator().next();
		final Prescription prescription = new Prescription();
		prescription.setProvider(anyOrder.getOrderer());
		prescription.setPrescriptionEncounter(encounter);
		prescription.setPatient(encounter.getPatient());
		prescription.setLocation(encounter.getLocation());
		prescription.setPrescriptionDate(this.getPrescriptionDate(encounter));
		
		return prescription;
	}
	
	private Date getPrescriptionDate(final Encounter encounter) {
		
		final Encounter prescriptionEncounter = this.getPrescriptionEncounter(encounter);
		
		final Set<Obs> allObs = prescriptionEncounter.getAllObs();
		if (this.precriptionDateConcept == null) {
			this.precriptionDateConcept = Context.getConceptService()
			        .getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE);
		}
		
		for (final Obs obs : allObs) {
			
			if (obs.getConcept().equals(this.precriptionDateConcept)) {
				return obs.getValueDatetime();
			}
		}
		return null;
	}
	
	private Encounter getPrescriptionEncounter(final Encounter encounter) {
		
		if (MappedEncounters.DISPENSATION_ENCOUNTER_TYPE.equals(encounter.getEncounterType().getUuid())) {
			
			try {
				
				return Context.getService(PrescriptionDispensationService.class)
				        .findPrescriptionDispensationByDispensation(encounter).getPrescription();
			}
			catch (final PharmacyBusinessException e) {
				throw new APIException(e);
			}
		}
		return encounter;
	}
	
	private PrescriptionStatus calculatePrescriptioStatus(final List<PrescriptionItem> prescriptionItems) {
		
		final int countNew = this.countPrescriptionItemByItemStatus(prescriptionItems, PrescriptionItemStatus.NEW);
		
		final int countFinalized = this.countPrescriptionItemByItemStatus(prescriptionItems,
		    PrescriptionItemStatus.FINALIZED);
		final int countExpired = this.countPrescriptionItemByItemStatus(prescriptionItems,
		    PrescriptionItemStatus.EXPIRED);
		
		final int countInterrupted = this.countPrescriptionItemByItemStatus(prescriptionItems,
		    PrescriptionItemStatus.INTERRUPTED);
		
		final int countActiveStatus = this.countPrescriptionItemByItemStatus(prescriptionItems,
		    PrescriptionItemStatus.ACTIVE);
		
		final int prescriptionItemsSize = prescriptionItems.size();
		
		if ((countNew > 0) || (countActiveStatus > 0)) {
			return PrescriptionStatus.ACTIVE;
		}
		if (prescriptionItemsSize == countFinalized) {
			return PrescriptionStatus.FINALIZED;
		}
		if (prescriptionItemsSize == countExpired) {
			return PrescriptionStatus.EXPIRED;
		}
		if (prescriptionItemsSize == countInterrupted) {
			return PrescriptionStatus.INTERRUPTED;
		}
		if (countFinalized > 0) {
			return PrescriptionStatus.FINALIZED;
		}
		if (countExpired > 0) {
			return PrescriptionStatus.EXPIRED;
		}
		if (countInterrupted > 0) {
			return PrescriptionStatus.INTERRUPTED;
		}
		throw new IllegalArgumentException(
		        " No mapping found for Prescription status for items with status " + prescriptionItems.toString());
	}
	
	private int countPrescriptionItemByItemStatus(final List<PrescriptionItem> prescriptionItems,
	        final PrescriptionItemStatus prescriptionItemStatus) {
		int countStatus = 0;
		for (final PrescriptionItem prescriptionItem : prescriptionItems) {
			if (prescriptionItemStatus.equals(prescriptionItem.getStatus())) {
				countStatus++;
			}
		}
		return countStatus;
	}
}
