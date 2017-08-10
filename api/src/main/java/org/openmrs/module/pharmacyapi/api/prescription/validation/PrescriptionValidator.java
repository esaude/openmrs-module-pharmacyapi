/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionValidator {

	private List<IPrescriptionValidationRule> rulesForCreatingPrescription = new ArrayList<>();

	@Autowired
	private PatientPrescriptionRule patientRule;

	@Autowired
	private ProviderPrescriptionRule ProviderRule;

	@Autowired
	private LocationPrescriptionRule locationRule;

	@Autowired
	private PrescriptionDateRule prescriptionDateRule;

	@Autowired
	private PrescriptionItemRule prescriptionItemRule;

	@PostConstruct
	private void initializeRules() {

		rulesForCreatingPrescription.add(patientRule);
		rulesForCreatingPrescription.add(ProviderRule);
		rulesForCreatingPrescription.add(locationRule);
		rulesForCreatingPrescription.add(prescriptionDateRule);
		rulesForCreatingPrescription.add(prescriptionItemRule);
	}

	public void validateCreation(Prescription prescription) throws PharmacyBusinessException {

		for (IPrescriptionValidationRule rule : rulesForCreatingPrescription) {

			rule.validate(prescription);
		}
	}
}
