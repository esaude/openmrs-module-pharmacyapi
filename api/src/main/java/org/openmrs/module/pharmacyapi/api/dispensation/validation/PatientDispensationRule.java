/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.springframework.stereotype.Component;

@Component
public class PatientDispensationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(Dispensation dispensation) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		
		Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
		
		if (patient == null) {
			throw new PharmacyBusinessException("Patient not found for given uuid " + dispensation.getPatientUuid());
		}
	}
}
