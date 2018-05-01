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

import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.Order.Action;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.springframework.stereotype.Component;

@Component
public class DiscountinuePrescriptionItemGenerator extends AbstractPrescriptionItemGenerator {
	
	@Override
	public PrescriptionItem generate(final DrugOrder drugOrder, final Date creationDate)
	        throws PharmacyBusinessException {
		
		final DrugOrder fetchDO = this.fetchDrugOrder(drugOrder);
		final PrescriptionItem prescriptionItem = new PrescriptionItem(this.cloneDrugOrder(fetchDO));
		final Double quantity = this.calculateDrugPikckedUp(fetchDO);
		prescriptionItem.setDrugPickedUp(quantity);
		prescriptionItem
		        .setDrugToPickUp(prescriptionItem.getDrugOrder().getQuantity() - prescriptionItem.getDrugPickedUp());
		prescriptionItem.setExpectedNextPickUpDate(this.getNextPickUpDate(prescriptionItem.getDrugOrder()));
		this.setPrescriptionInstructions(prescriptionItem, prescriptionItem.getDrugOrder());
		
		prescriptionItem.setStatus(this.calculatePrescriptionItemStatus(prescriptionItem, creationDate));
		this.setArvDataFields(fetchDO, prescriptionItem);
		prescriptionItem.setInterruptionReason(drugOrder.getOrderReason());
		return prescriptionItem;
	}
	
	private DrugOrder cloneDrugOrder(final DrugOrder drugOrder) {
		
		final DrugOrder clone = new DrugOrder();
		clone.setId(drugOrder.getId());
		clone.setPreviousOrder(drugOrder.getPreviousOrder());
		clone.setOrderReason(drugOrder.getOrderReason());
		
		DrugOrder tempDrugOrder = drugOrder;
		while (!Action.NEW.equals(tempDrugOrder.getAction())) {
			
			tempDrugOrder = (DrugOrder) tempDrugOrder.getPreviousOrder();
		}
		
		clone.setDose(tempDrugOrder.getDose());
		clone.setQuantity(tempDrugOrder.getQuantity());
		clone.setDosingInstructions(tempDrugOrder.getDosingInstructions());
		clone.setDuration(tempDrugOrder.getDuration());
		clone.setDurationUnits(tempDrugOrder.getDurationUnits());
		clone.setQuantityUnits(tempDrugOrder.getQuantityUnits());
		clone.setRoute(tempDrugOrder.getRoute());
		clone.setDoseUnits(tempDrugOrder.getDoseUnits());
		clone.setFrequency(tempDrugOrder.getFrequency());
		// Since we are cloning All atributs but must remain the Action to
		// represents this Order as Stopped
		clone.setAction(Action.DISCONTINUE);
		clone.setUuid(drugOrder.getUuid());
		clone.setEncounter(tempDrugOrder.getEncounter());
		clone.setConcept(tempDrugOrder.getConcept());
		clone.setOrderer(tempDrugOrder.getOrderer());
		clone.setPatient(tempDrugOrder.getPatient());
		clone.setCareSetting(tempDrugOrder.getCareSetting());
		clone.setDrug(tempDrugOrder.getDrug());
		clone.setDateCreated(tempDrugOrder.getDateCreated());
		clone.setAutoExpireDate(tempDrugOrder.getAutoExpireDate());
		
		return clone;
	}
	
	@Override
	protected PrescriptionItemStatus calculatePrescriptionItemStatus(final PrescriptionItem item,
	        final Date consultationDate) {
		
		return item.getDrugOrder().getOrderReason() != null ? PrescriptionItemStatus.INTERRUPTED
		        : this.isOrderExpired(item, consultationDate) ? PrescriptionItemStatus.EXPIRED
		                : PrescriptionItemStatus.FINALIZED;
	}
}
