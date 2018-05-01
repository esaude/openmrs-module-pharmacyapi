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

import java.util.Date;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class LocationPrescriptionRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(final Prescription prescription, final Date date) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException("pharmacyapi.invalid.prescription");
		}
		final Location location = prescription.getLocation();
		
		if (location == null) {
			
			throw new PharmacyBusinessException("pharmacyapi.invalid.prescription.location");
		}
		final Location found = Context.getLocationService().getLocationByUuid(location.getUuid());
		
		if (found == null) {
			
			throw new PharmacyBusinessException("pharmacyapi.location.not.found", prescription.getLocation().getUuid());
		}
	}
}
