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

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.springframework.stereotype.Component;

@Component
public class PatientDispensationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(final Dispensation dispensation, final Date date) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		
		final Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
		
		if (patient == null) {
			throw new PharmacyBusinessException("Patient not found for given uuid " + dispensation.getPatientUuid());
		}
	}
}
