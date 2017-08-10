/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.springframework.stereotype.Component;

@Component
public class LocationDispensationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(Dispensation dispensation) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			throw new PharmacyBusinessException(" Invalid Dispensation argument");
		}
		
		Location location = Context.getLocationService().getLocationByUuid(dispensation.getLocationUuid());
		
		if (location == null) {
			
			throw new PharmacyBusinessException("Location with uuid '" + dispensation.getLocationUuid() + "' not found");
		}
	}
}
