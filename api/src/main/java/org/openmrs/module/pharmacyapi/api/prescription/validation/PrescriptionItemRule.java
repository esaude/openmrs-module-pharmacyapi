/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PrescriptionItemRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(Prescription prescription) throws PharmacyBusinessException {

		if (prescription == null) {
			throw new PharmacyBusinessException("Invalid Prescription Argument");
		}

		Set<String> regimesUuid = new TreeSet<>();

		for (PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {

			if (prescriptionItem.getDrugOrder() == null) {

				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem without drugOrder " + prescriptionItem);
			}

			if (prescriptionItem.getDrugOrder().getDrug() == null
					|| StringUtils.isBlank(prescriptionItem.getDrugOrder().getDrug().getUuid())) {

				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem without Drug " + prescriptionItem);
			}

			if (prescriptionItem.getDrugOrder().getDose() == null || prescriptionItem.getDrugOrder().getDose().isNaN()
					|| prescriptionItem.getDrugOrder().getDose().doubleValue() <= 0) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid DrugOrder Dose "
								+ prescriptionItem.getDrugOrder().getDose());
			}

			if (prescriptionItem.getDrugOrder().getDoseUnits() == null
					|| StringUtils.isBlank(prescriptionItem.getDrugOrder().getDoseUnits().getUuid())) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid dose Units "
								+ prescriptionItem.getDrugOrder().getDoseUnits());
			}

			if (prescriptionItem.getDrugOrder().getDuration() == null
					|| prescriptionItem.getDrugOrder().getDuration().intValue() <= 0) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid DrugOrder Duration "
								+ prescriptionItem.getDrugOrder().getDuration());
			}

			if (prescriptionItem.getDrugOrder().getDurationUnits() == null
					|| StringUtils.isBlank(prescriptionItem.getDrugOrder().getDurationUnits().getUuid())) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid DrugOrder Duration Units "
								+ prescriptionItem.getDrugOrder().getDurationUnits());
			}

			if (prescriptionItem.getDrugOrder().getFrequency() == null
					|| StringUtils.isBlank(prescriptionItem.getDrugOrder().getFrequency().getUuid())) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid DrugOrder Frequency "
								+ prescriptionItem.getDrugOrder().getFrequency());
			}

			if (prescriptionItem.getDrugOrder().getQuantityUnits() == null
					|| StringUtils.isBlank(prescriptionItem.getDrugOrder().getQuantityUnits().getUuid())) {
				throw new PharmacyBusinessException(
						"Cannot create Prescription for prescriptionItem with Invalid DrugOrder Quantity Units "
								+ prescriptionItem.getDrugOrder().getQuantityUnits());
			}

			if (prescriptionItem.getRegime() != null
					&& StringUtils.isNotBlank(prescriptionItem.getRegime().getUuid())) {
				regimesUuid.add(prescriptionItem.getRegime().getUuid());
			}
		}

		if (regimesUuid.size() > 1) {

			throw new PharmacyBusinessException("Cannot Create a Prescription for Arv Drugs having different regimes "
					+ StringUtils.join(prescription.getPrescriptionItems(), "|"));
		}

		Concept regime = prescription.getRegime();
		if (regime != null) {

			Patient patient = Context.getPatientService().getPatientByUuid(prescription.getPatient().getUuid());
			PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);

			List<Prescription> existingsPrescriptions = prescriptionService
					.findPrescriptionsByPatientAndActiveStatus(patient);

			for (Prescription existingPrescription : existingsPrescriptions) {

				if (existingPrescription.getRegime() != null) {

					throw new PharmacyBusinessException("Cannot create a new ARV Prescription for Patient "
							+ getFormattedPatientToDisplay(patient) + " while exist Active ARV prescription "
							+ StringUtils.join(existingsPrescriptions, "|"));

				}
			}
		}

	}
	
	private String getFormattedPatientToDisplay(Patient patient) {
		return ((patient.getPatientIdentifier() != null) ? patient.getPatientIdentifier().getIdentifier() + " - "
		        + patient.getPersonName().getFullName() : patient.getGivenName() + " " + patient.getFamilyName());
	}
}
