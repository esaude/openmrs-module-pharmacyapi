/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.Calendar;

import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PrescriptionDateRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(Prescription prescription) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}
		
		if (prescription.getPrescriptionDate() == null) {
			throw new PharmacyBusinessException("Cannot create a Prescription without Prescription Date " + prescription);
		}
		
		if (prescription.getPrescriptionDate().after(Calendar.getInstance().getTime())) {
			
			throw new PharmacyBusinessException("Cannot create a Prescription for future Date "
			        + prescription.getPrescriptionDate());
		}
	}
}
