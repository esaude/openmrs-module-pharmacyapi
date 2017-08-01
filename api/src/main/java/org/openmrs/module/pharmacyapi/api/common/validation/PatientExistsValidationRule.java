/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.common.validation;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.springframework.stereotype.Component;

@Component
public class PatientExistsValidationRule implements IValidationRule<Patient> {
	
	@Override
	public void validate(Patient patient) throws PharmacyBusinessException {
		
		if (patient == null) {
			
			throw new PharmacyBusinessException("No given Patient");
		}
		
		Patient found = Context.getPatientService().getPatientByUuid(patient.getUuid());
		
		if (found == null) {
			throw new PharmacyBusinessException("Patient not found for given uuid " + patient.getUuid());
		}
	}
}
