/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.prescription.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription.PrescriptionStatus;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionGenerator;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionUtils;
import org.openmrs.module.pharmacyapi.api.prescription.validation.PrescriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public class PrescriptionServiceImpl extends BaseOpenmrsService implements PrescriptionService {
	
	private EncounterService encounterService;
	
	private DispensationDAO dispensationDAO;
	
	private PharmacyHeuristicService pharmacyHeuristicService;
	
	@Autowired
	private PrescriptionValidator prescriptionValidator;
	
	@Autowired
	private PrescriptionUtils prescriptionUtils;
	
	@Autowired
	private PrescriptionGenerator prescriptionGenerator;
	
	@Override
	public void setEncounterService(final EncounterService encounterService) {
		
		this.encounterService = encounterService;
	}
	
	@Override
	public void setDispensationDAO(final DispensationDAO dispensationDAO) {
		this.dispensationDAO = dispensationDAO;
	}
	
	@Override
	public void setPharmacyHeuristicService(final PharmacyHeuristicService pharmacyHeuristicService) {
		this.pharmacyHeuristicService = pharmacyHeuristicService;
	}
	
	@Override
	public List<Prescription> findActivePrescriptionsByPatient(final Patient patient, final Date actualDate)
	        throws PharmacyBusinessException {
		
		final List<Prescription> allPrescriptions = this.findAllPrescriptionsByPatient(patient, actualDate);
		final List<Prescription> result = new ArrayList<>();
		
		for (final Prescription prescription : allPrescriptions) {
			
			if (PrescriptionStatus.ACTIVE.equals(prescription.getPrescriptionStatus())) {
				prescription.setPrescriptionItems(this.filterOnlyActiveItems(prescription.getPrescriptionItems()));
				result.add(prescription);
			}
		}
		return result;
	}
	
	@Override
	public List<Prescription> findAllPrescriptionsByPatient(final Patient patient, final Date creationDate)
	        throws PharmacyBusinessException {
		
		final List<DrugOrder> orders = this.getOrdersNotDispensed(patient);
		final List<DrugOrder> dispensed = this.dispensationDAO.findDispensedDrugOrdersByPatient(patient);
		
		orders.addAll(dispensed);
		
		return this.prescriptionGenerator.generatePrescriptions(orders, creationDate);
	}
	
	@Override
	public List<Prescription> findNotExpiredArvPrescriptions(final Patient patient, final Date actualDate)
	        throws PharmacyBusinessException {
		final List<Prescription> prescriptions = this.findAllPrescriptionsByPatient(patient, actualDate);
		final List<Prescription> result = new ArrayList<>();
		for (final Prescription prescription : prescriptions) {
			if (this.hasActivePrescriptionItems(prescription)) {
				result.add(prescription);
			}
		}
		return result;
	}
	
	private List<PrescriptionItem> filterOnlyActiveItems(final List<PrescriptionItem> items) {
		
		final List<PrescriptionItem> result = new ArrayList<>();
		
		for (final PrescriptionItem item : items) {
			
			if (PrescriptionItemStatus.NEW.equals(item.getStatus())
			        || PrescriptionItemStatus.ACTIVE.equals(item.getStatus())) {
				result.add(item);
			}
		}
		return result;
	}
	
	private boolean hasActivePrescriptionItems(final Prescription prescription) {
		for (final PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {
			if (prescriptionItem.getRegime() != null) {
				if (PrescriptionItemStatus.NEW.equals(prescriptionItem.getStatus())
				        || PrescriptionItemStatus.ACTIVE.equals(prescriptionItem.getStatus())) {
					return true;
				} else if (PrescriptionItemStatus.FINALIZED.equals(prescriptionItem.getStatus())
				        && (prescriptionItem.getDrugOrder().getOrderReason() == null)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Prescription createPrescription(final Prescription prescription) throws PharmacyBusinessException {
		
		this.prescriptionValidator.validateCreation(prescription);
		
		final Patient patient = Context.getPatientService().getPatientByUuid(prescription.getPatient().getUuid());
		final Provider provider = Context.getProviderService().getProviderByUuid(prescription.getProvider().getUuid());
		prescription.setPatient(patient);
		prescription.setProvider(provider);
		
		final Encounter encounter = this.prescriptionUtils.preparePrescriptionEncounter(prescription);
		this.prescriptionUtils.prepareObservations(prescription, encounter);
		this.prescriptionUtils.prepareOrders(prescription, encounter);
		
		this.encounterService.saveEncounter(encounter);
		prescription.setPrescriptionEncounter(encounter);
		
		return prescription;
	}
	
	@Override
	public void cancelPrescriptionItem(final PrescriptionItem prescriptionItem, final String cancelationReason)
	        throws PharmacyBusinessException {
		
		final Order order = Context.getOrderService().getOrderByUuid(prescriptionItem.getDrugOrder().getUuid());
		if (Action.NEW.equals(order.getAction())) {
			Context.getOrderService().voidOrder(order, cancelationReason);
			
		} else {
			
			final Concept discountinueReason = Context.getConceptService().getConceptByUuid(cancelationReason);
			if (Action.REVISE.equals(order.getAction())) {
				try {
					Context.getOrderService().discontinueOrder(order, discountinueReason, new Date(),
					    order.getOrderer(), order.getEncounter());
				}
				catch (final Exception e) {
					throw new APIException(e.getMessage());
				}
			} else if (Action.DISCONTINUE.equals(order.getAction())) {
				this.pharmacyHeuristicService.updateOrder(order, discountinueReason);
			}
		}
	}
	
	private List<DrugOrder> getOrdersNotDispensed(final Patient patient) {
		
		final EncounterType arvEncounterType = this.pharmacyHeuristicService.getEncounterTypeByPatientAge(patient);
		final EncounterType generalPrescriptionEncType = Context.getEncounterService()
		        .getEncounterTypeByUuid(MappedEncounters.GENERAL_PRESCRIPTION);
		
		return this.dispensationDAO.findNotDispensedDrugOrdersByPatient(patient, arvEncounterType,
		    generalPrescriptionEncType);
	}
}
