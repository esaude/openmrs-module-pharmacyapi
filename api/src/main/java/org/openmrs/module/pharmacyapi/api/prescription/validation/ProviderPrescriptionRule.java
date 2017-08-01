/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.Collection;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ProviderPrescriptionRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		Provider provider = prescription.getProvider();
		
		if (provider == null) {
			
			throw new PharmacyBusinessException("Invalid prescriprion provider argument " + prescription);
		}
		
		Person person = Context.getPersonService().getPersonByUuid(provider.getUuid());
		if (person == null) {
			throw new PharmacyBusinessException(" Person Provider not found for given uuid " + provider.getUuid());
		}
		Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(person);
		
		if (providers == null || providers.isEmpty()) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + provider.getUuid());
		}
	}
}
