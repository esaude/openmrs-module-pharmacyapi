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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PrescriptionDateRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(final Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException("pharmacyapi.invalid.prescription");
		}
		
		if (prescription.getPrescriptionDate() == null) {
			throw new PharmacyBusinessException("pharmacyapi.cannot.create.prescription.without.date");
		}
		
		if (prescription.getPrescriptionDate().after(Calendar.getInstance().getTime())) {
			
			final Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			throw new PharmacyBusinessException("pharmacyapi.cannot.create.prescription.for.future.date",
			        formatter.format(prescription.getPrescriptionDate()));
		}
	}
}
