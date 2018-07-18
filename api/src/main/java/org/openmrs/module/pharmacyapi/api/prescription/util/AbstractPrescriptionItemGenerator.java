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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.drugregime.model.DrugRegime;
import org.openmrs.module.pharmacyapi.api.drugregime.service.DrugRegimeService;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractPrescriptionItemGenerator implements PrescriptionItemGenerator {
	
	@Override
	public PrescriptionItem generate(final Prescription prescription, final DrugOrder drugOrder,
	        final Date creationDate) throws PharmacyBusinessException {
		
		final DrugOrder fetchDrugOrder = this.fetchDrugOrder(drugOrder);
		final PrescriptionItem prescriptionItem = new PrescriptionItem(fetchDrugOrder);
		prescriptionItem.setStatus(this.calculatePrescriptionItemStatus(prescriptionItem, creationDate));
		this.setPrescriptionInstructions(prescriptionItem, fetchDrugOrder);
		prescriptionItem.setExpectedNextPickUpDate(this.getNextPickUpDate(fetchDrugOrder));
		this.setArvFlag(prescriptionItem);
		
		return prescriptionItem;
	}
	
	protected void setPrescriptionInstructions(final PrescriptionItem prescriptionItem, final DrugOrder drugOrder) {
		final Concept concept = Context.getConceptService().getConceptByUuid(drugOrder.getDosingInstructions());
		prescriptionItem.setDosingInstructions(concept.getNames().iterator().next().getName());
	}
	
	protected DrugOrder fetchDrugOrder(final DrugOrder drugOrder) {
		return (DrugOrder) Context.getOrderService().getOrderByUuid(drugOrder.getUuid());
	}
	
	protected abstract PrescriptionItemStatus calculatePrescriptionItemStatus(PrescriptionItem item,
	        Date consultationDate);
	
	protected void setArvFlag(final PrescriptionItem item) {
		
		if (item.getDrugOrder() != null) {
			
			final List<DrugRegime> drugRegimes = Context.getService(DrugRegimeService.class)
			        .findDrugRegimeByDrugUuid(item.getDrugOrder().getDrug().getUuid());
			item.setArv(!drugRegimes.isEmpty());
		}
	}
	
	protected Double calculateDrugPikckedUp(final DrugOrder order) {
		
		Double quantity = 0.0;
		final List<Obs> observations = new ArrayList<>();
		
		DrugOrder tempOrder = order;
		while (tempOrder.getPreviousOrder() != null) {
			
			if ((tempOrder.getOrderReason() == null) && !tempOrder.getVoided()) {
				
				observations.addAll(Context.getObsService().getObservations(tempOrder.getEncounter()));
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
	
	public boolean isOrderExpired(final PrescriptionItem item, final Date creationDate) {
		
		final Double drugToPickUp = item.getDrugToPickUp();
		
		final Date nextPickUpDate = this.getNextPickUpDate(item.getDrugOrder());
		
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(nextPickUpDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		calendar.add(Calendar.DAY_OF_MONTH, drugToPickUp.intValue());
		
		while ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		        || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		return creationDate.after(calendar.getTime());
	}
}
