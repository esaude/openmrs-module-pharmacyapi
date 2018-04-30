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

import java.util.Collection;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ProviderDispensationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(final Dispensation dispensation) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			throw new PharmacyBusinessException(" Invalid Dispensation argument");
		}
		final Person person = Context.getPersonService().getPersonByUuid(dispensation.getProviderUuid());
		final Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(person);
		
		if ((providers == null) || providers.isEmpty()) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + dispensation.getProviderUuid());
		}
	}
}
