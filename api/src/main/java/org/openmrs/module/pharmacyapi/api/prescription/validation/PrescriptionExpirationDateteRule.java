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
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionExpirationDateteRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(final Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription.isArv()) {
			
			final List<Prescription> arvNotExpiredPrescriptions = Context.getService(PrescriptionService.class)
			        .findNotExpiredArvPrescriptions(prescription.getPatient(), prescription.getPrescriptionDate());
			if (!arvNotExpiredPrescriptions.isEmpty()) {
				throw new PharmacyBusinessException(
				        "pharmacyapi.prescription.cannot.be.created.due.exisiting.active.arv.prescription");
			}
		}
	}
}
