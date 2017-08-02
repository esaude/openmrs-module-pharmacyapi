/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

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
		
		Provider providerFound = Context.getProviderService().getProviderByUuid(provider.getUuid());
		
		if (providerFound == null) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + provider.getUuid());
		}
	}
}
