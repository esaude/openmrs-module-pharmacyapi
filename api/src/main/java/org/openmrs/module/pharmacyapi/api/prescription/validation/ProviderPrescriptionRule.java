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

import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ProviderPrescriptionRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(final Prescription prescription, final Date date) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		final Provider provider = prescription.getProvider();
		
		if (provider == null) {
			
			throw new PharmacyBusinessException("Invalid prescriprion provider argument " + prescription);
		}
		
		final Provider providerFound = Context.getProviderService().getProviderByUuid(provider.getUuid());
		
		if (providerFound == null) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + provider.getUuid());
		}
		prescription.setProvider(providerFound);
	}
}
