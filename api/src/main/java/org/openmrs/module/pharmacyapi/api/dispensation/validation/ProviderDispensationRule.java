/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

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
		
		Provider provider = Context.getProviderService().getProviderByUuid(dispensation.getProviderUuid());
		
		if (provider == null) {
			throw new PharmacyBusinessException("Provider not found for given uuid " + dispensation.getProviderUuid());
		}
	}
}
