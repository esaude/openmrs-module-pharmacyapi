/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.springframework.stereotype.Component;

@Component
public class LocationPrescriptionRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		Location location = prescription.getLocation();
		
		if (location == null) {
			
			throw new PharmacyBusinessException("Invalid Prescription Location argument " + prescription);
		}
		Location found = Context.getLocationService().getLocationByUuid(location.getUuid());
		
		if (found == null) {
			
			throw new PharmacyBusinessException("Location with uuid '" + prescription.getLocation().getUuid()
			        + "' not found");
		}
	}
}
