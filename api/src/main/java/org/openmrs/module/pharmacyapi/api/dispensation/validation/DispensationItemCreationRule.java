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
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Order.Action;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.springframework.stereotype.Component;

@Component
public class DispensationItemCreationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(final Dispensation dispensation, final Date date) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			
			throw new PharmacyBusinessException(" Invalid dispensation argument");
		}
		
		if ((dispensation.getDispensationItems() == null) || dispensation.getDispensationItems().isEmpty()) {
			
			throw new PharmacyBusinessException("No provided Item(s) of drugOrder to be Dispensed");
		}
		
		for (final DispensationItem dispensationItem : dispensation.getDispensationItems()) {
			
			final DrugOrder order = (DrugOrder) Context.getOrderService()
			        .getOrderByUuid(dispensationItem.getOrderUuid());
			
			if (order == null) {
				
				throw new PharmacyBusinessException(
				        "No Order found for given uuid: " + dispensationItem.getOrderUuid());
			}
			
			if ((dispensationItem.getQuantityToDispense() == null)
			        || (dispensationItem.getQuantityToDispense().doubleValue() <= 0)) {
				
				throw new PharmacyBusinessException(
				        "The Order to be Dispensed must have valid quantity to Dispense. Order : " + order
				                + " Quantity passed: " + dispensationItem.getQuantityToDispense());
			}
			
			if (Action.DISCONTINUE.equals(order.getAction())) {
				
				throw new PharmacyBusinessException(
				        "The Order to be Dispensed must have Order Action in 'NEW' or 'REVISE'. order uuid: "
				                + dispensationItem.getOrderUuid());
			}
			
			if (dispensationItem.getQuantityToDispense().doubleValue() > order.getQuantity().doubleValue()) {
				
				throw new PharmacyBusinessException(
				        "The quantity to Be Dispensed must be less or equals the Order  Quantity : " + order
				                + " Quantity passed: " + dispensationItem.getQuantityToDispense());
			}
			
			if (order.isVoided()) {
				throw new PharmacyBusinessException("Cannot Dispense A voided Item" + order);
			}
			
			final Encounter prescriptionEncounter = Context.getEncounterService()
			        .getEncounterByUuid(dispensationItem.getPrescriptionUuid());
			
			if (prescriptionEncounter == null) {
				
				throw new PharmacyBusinessException(
				        "Encounter of Prescription not Found for Dispensation Item " + order);
			}
		}
	}
}
