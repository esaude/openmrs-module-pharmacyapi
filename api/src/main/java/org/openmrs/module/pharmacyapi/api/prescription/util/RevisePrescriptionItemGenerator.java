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
public class RevisePrescriptionItemGenerator extends AbstractPrescriptionItemGenerator {
	
	@Override
	public PrescriptionItem generate(final DrugOrder drugOrder, final Date creationDate)
	        throws PharmacyBusinessException {
		
		final DrugOrder fetchDO = this.fetchDrugOrder(drugOrder);
		final PrescriptionItem prescriptionItem = new PrescriptionItem(fetchDO);
		final Double quantity = this.calculateDrugPikckedUp(fetchDO);
		prescriptionItem.setDrugPickedUp(quantity);
		prescriptionItem
		        .setDrugToPickUp(prescriptionItem.getDrugOrder().getQuantity() - prescriptionItem.getDrugPickedUp());
		prescriptionItem.setExpectedNextPickUpDate(this.getNextPickUpDate(fetchDO));
		prescriptionItem.setStatus(this.calculatePrescriptionItemStatus(fetchDO, creationDate));
		this.setPrescriptionInstructions(prescriptionItem, fetchDO);
		this.setArvDataFields(drugOrder, prescriptionItem);
		
		return prescriptionItem;
	}
	
	@Override
	protected PrescriptionItemStatus calculatePrescriptionItemStatus(final DrugOrder drugOrder,
	        final Date creationDate) {
		
		final PrescriptionItemStatus status = Action.DISCONTINUE.equals(drugOrder.getAction())
		        ? PrescriptionItemStatus.FINALIZED : PrescriptionItemStatus.ACTIVE;
		
		return this.isOrderExpired(drugOrder, creationDate) ? PrescriptionItemStatus.EXPIRED : status;
	}
}
