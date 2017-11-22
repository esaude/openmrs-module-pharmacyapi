/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 *
 */
package org.openmrs.module.pharmacyapi.api.prescription.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractPrescriptionItemGenerator implements PrescriptionItemGenerator {
	
	protected void setPrescriptionInstructions(final PrescriptionItem prescriptionItem, final DrugOrder drugOrder) {
		final Concept concept = Context.getConceptService().getConceptByUuid(drugOrder.getDosingInstructions());
		prescriptionItem.setDosingInstructions(concept.getNames().iterator().next().getName());
	}
	
	protected DrugOrder fetchDrugOrder(final DrugOrder drugOrder) {
		return (DrugOrder) Context.getOrderService().getOrderByUuid(drugOrder.getUuid());
	}
	
	protected void setArvDataFields(final DrugOrder drugOrder, final PrescriptionItem prescriptionItem)
	        throws PharmacyBusinessException {
		
		final PrescriptionDispensationService prescriptionDispensationService = Context
		        .getService(PrescriptionDispensationService.class);
		
		if (prescriptionDispensationService.isArvDrug(drugOrder)) {
			prescriptionItem.setRegime(this.findRegime(drugOrder));
			prescriptionItem.setArvPlan(this.findArvPlan(drugOrder));
			prescriptionItem.setTherapeuticLine(this.findArvTherapeuticPlan(drugOrder));
		}
	}
	
	protected abstract PrescriptionItemStatus calculatePrescriptionItemStatus(final DrugOrder drugOrder,
	        Date expirationDate);
	
	private Concept findArvPlan(final DrugOrder drugOrder) {
		DrugOrder tempDrugOrder = drugOrder;
		while (!Action.NEW.equals(tempDrugOrder.getAction())) {
			tempDrugOrder = (DrugOrder) tempDrugOrder.getPreviousOrder();
		}
		
		final Set<Obs> allObs = tempDrugOrder.getEncounter().getAllObs();
		final Concept arvPlan = Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_PLAN);
		
		for (final Obs obs : allObs) {
			if (arvPlan.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		throw new IllegalArgumentException("No ARV plan found for drugOrder with uuid " + drugOrder.getUuid());
	}
	
	private Concept findArvTherapeuticPlan(final DrugOrder drugOrder) {
		DrugOrder tempDrugOrder = drugOrder;
		while (!Action.NEW.equals(tempDrugOrder.getAction())) {
			tempDrugOrder = (DrugOrder) tempDrugOrder.getPreviousOrder();
		}
		
		final Set<Obs> allObs = tempDrugOrder.getEncounter().getAllObs();
		final Concept arvPlan = Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_THERAPEUTIC_LINE);
		
		for (final Obs obs : allObs) {
			if (arvPlan.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		throw new IllegalArgumentException(
		        "No ARV Therapeutic Line found for drugOrder with uuid " + drugOrder.getUuid());
	}
	
	private Concept findRegime(final DrugOrder drugOrder) {
		DrugOrder tempDrugOrder = drugOrder;
		while (!Action.NEW.equals(tempDrugOrder.getAction())) {
			tempDrugOrder = (DrugOrder) tempDrugOrder.getPreviousOrder();
		}
		
		final Set<Obs> allObs = tempDrugOrder.getEncounter().getAllObs();
		final Concept regime = Context.getConceptService()
		        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		
		for (final Obs obs : allObs) {
			if (regime.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		throw new IllegalArgumentException("No Regime found for drugOrder with uuid " + drugOrder.getUuid());
	}
	
	protected Double calculateDrugPikckedUp(final DrugOrder order) {
		
		Double quantity = 0.0;
		final List<Obs> observations = new ArrayList<>();
		
		DrugOrder tempOrder = order;
		while (tempOrder.getPreviousOrder() != null) {
			
			if ((tempOrder.getOrderReason() == null) && !tempOrder.getVoided()) {
				observations.addAll(tempOrder.getEncounter().getObs());
			}
			tempOrder = (DrugOrder) tempOrder.getPreviousOrder();
		}
		
		for (final Obs observation : observations) {
			
			if (!observation.getVoided() && this.isTheSameConceptAndSameDrug(order, observation)) {
				quantity += observation.getValueNumeric();
			}
		}
		
		return quantity;
	}
	
	protected Date getNextPickUpDate(final DrugOrder drugOrder) {
		
		final List<Obs> observations = Context.getService(PharmacyHeuristicService.class)
		        .findObservationsByOrder(drugOrder);
		for (final Obs obs : observations) {
			
			if (MappedConcepts.DATE_OF_NEXT_PICK_UP.equals(obs.getConcept().getUuid())) {
				
				return obs.getValueDate();
			}
		}
		return null;
	}
	
	private boolean isTheSameConceptAndSameDrug(final DrugOrder order, final Obs observation) {
		
		final Drug obsDrug = Context.getService(PharmacyHeuristicService.class)
		        .findDrugByOrderUuid(observation.getOrder().getUuid());
		
		return MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())
		        && order.getDrug().getUuid().equals(obsDrug.getUuid());
	}
	
	public boolean isOrderExpired(final Order order, final Date creationDate) {
		
		Order tempDrugOrder = order;
		while (!Action.NEW.equals(tempDrugOrder.getAction())) {
			tempDrugOrder = tempDrugOrder.getPreviousOrder();
		}
		return creationDate.after(tempDrugOrder.getAutoExpireDate());
	}
	
}
