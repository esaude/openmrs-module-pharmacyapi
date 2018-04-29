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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.springframework.stereotype.Component;

@Component
public class DispensationItemCancelationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(final Dispensation dispensation, final Date date) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			
			throw new PharmacyBusinessException(" Invalid dispensation argument");
		}
		
		if ((dispensation.getDispensationItems() == null) || dispensation.getDispensationItems().isEmpty()) {
			
			throw new PharmacyBusinessException("No provided Item(s) of drugOrder to be Dispensed");
		}
		
		final Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
		final List<Prescription> arvPrescriptions = Context.getService(PrescriptionService.class)
		        .findNotExpiredArvPrescriptions(patient, date);
		
		for (final DispensationItem dispensationItem : dispensation.getDispensationItems()) {
			
			final DrugOrder order = (DrugOrder) Context.getOrderService()
			        .getOrderByUuid(dispensationItem.getOrderUuid());
			
			if (order == null) {
				
				throw new PharmacyBusinessException(
				        "No Order found for given uuid: " + dispensationItem.getOrderUuid());
			}
			
			boolean isCancelable = false;
			if (StringUtils.isNotBlank(dispensationItem.getRegimeUuid()) && !arvPrescriptions.isEmpty()) {
				
				for (final Prescription prescription : arvPrescriptions) {
					
					for (final PrescriptionItem item : prescription.getPrescriptionItems()) {
						
						if (item.getDrugOrder().equals(order)) {
							isCancelable = true;
							break;
						}
					}
				}
			}
			
			if (!isCancelable) {
				throw new APIException(Context.getMessageSourceService().getMessage(
				    "pharmacyapi.cannot.cancel.dispensation.due.existence.of.active.arv.prescriptions"));
			}
		}
		
	}
}
