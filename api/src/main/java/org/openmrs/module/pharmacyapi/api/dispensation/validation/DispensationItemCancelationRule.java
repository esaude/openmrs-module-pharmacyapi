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

import java.util.Arrays;
import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispensationItemCancelationRule implements IDispensationRuleValidation {
	
	@Autowired
	private PrescriptionGenerator prescriptionGenerator;
	
	@Override
	public void validate(final Dispensation dispensation) throws PharmacyBusinessException {
		
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
			
			this.validateForInterruptedReason(order);
			this.validateForExpirationReason(order, dispensation.getDispensationDate());
		}
	}
	
	private void validateForInterruptedReason(final DrugOrder order) {
		
		Order childOrder = null;
		if (Action.REVISE.equals(order.getAction())) {
			childOrder = Context.getService(PharmacyHeuristicService.class).findOrderByPreviousOrder(order);
		}
		if ((order.getOrderReason() != null) || ((childOrder != null) && (childOrder.getOrderReason() != null))) {
			throw new APIException("Medicamento " + order.getDrug().getDisplayName()
			        + " não pode ser cancelado pois foi interrompido");
		}
	}
	
	private void validateForExpirationReason(final DrugOrder order, final Date date) throws PharmacyBusinessException {
		
		final Prescription prescription = this.prescriptionGenerator.generatePrescriptions(Arrays.asList(order), date)
		        .iterator().next();
		if (Prescription.PrescriptionStatus.EXPIRED.equals(prescription.getPrescriptionStatus())) {
			throw new APIException(
			        "Medicamento " + order.getDrug().getDisplayName() + " não pode ser cancelado pois está expirado");
		}
	}
	
}
