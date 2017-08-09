/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PatientPrescriptionRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		
		Patient patient = prescription.getPatient();
		if (patient == null) {
			
			throw new PharmacyBusinessException("Invalid prescription patient argument");
		}
		
		Patient found = Context.getPatientService().getPatientByUuid(patient.getUuid());
		
		if (found == null) {
			throw new PharmacyBusinessException("Patient not found for given uuid " + patient.getUuid());
		}
	}
}
