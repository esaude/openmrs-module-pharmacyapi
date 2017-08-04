/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import java.util.Collection;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ProviderDispensationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(Dispensation dispensation) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			throw new PharmacyBusinessException(" Invalid Dispensation argument");
		}
		Person person = Context.getPersonService().getPersonByUuid(dispensation.getProviderUuid());
		Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(person);
		
		if (providers == null || providers.isEmpty()) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + dispensation.getProviderUuid());
		}
	}
}
