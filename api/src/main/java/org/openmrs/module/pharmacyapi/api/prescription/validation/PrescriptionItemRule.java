/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 *
 */
package org.openmrs.module.pharmacyapi.api.prescription.validation;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PrescriptionItemRule implements IPrescriptionValidationRule {
	
	@Override
	public void validate(final Prescription prescription, final Date date) throws PharmacyBusinessException {
		
		if (prescription == null) {
			throw new PharmacyBusinessException("Invalid Prescription Argument");
		}
		
		final Set<String> regimesUuid = new TreeSet<>();
		
		for (final PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {
			
			if (prescriptionItem.getDrugOrder() == null) {
				
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem without drugOrder " + prescriptionItem);
			}
			
			if ((prescriptionItem.getDrugOrder().getDrug() == null)
			        || StringUtils.isBlank(prescriptionItem.getDrugOrder().getDrug().getUuid())) {
				
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem without Drug " + prescriptionItem);
			}
			
			if ((prescriptionItem.getDrugOrder().getDose() == null) || prescriptionItem.getDrugOrder().getDose().isNaN()
			        || (prescriptionItem.getDrugOrder().getDose().doubleValue() <= 0)) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid DrugOrder Dose "
				                + prescriptionItem.getDrugOrder().getDose());
			}
			
			if ((prescriptionItem.getDrugOrder().getDoseUnits() == null)
			        || StringUtils.isBlank(prescriptionItem.getDrugOrder().getDoseUnits().getUuid())) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid dose Units "
				                + prescriptionItem.getDrugOrder().getDoseUnits());
			}
			
			if ((prescriptionItem.getDrugOrder().getDuration() == null)
			        || (prescriptionItem.getDrugOrder().getDuration().intValue() <= 0)) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid DrugOrder Duration "
				                + prescriptionItem.getDrugOrder().getDuration());
			}
			
			if ((prescriptionItem.getDrugOrder().getDurationUnits() == null)
			        || StringUtils.isBlank(prescriptionItem.getDrugOrder().getDurationUnits().getUuid())) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid DrugOrder Duration Units "
				                + prescriptionItem.getDrugOrder().getDurationUnits());
			}
			
			if ((prescriptionItem.getDrugOrder().getFrequency() == null)
			        || StringUtils.isBlank(prescriptionItem.getDrugOrder().getFrequency().getUuid())) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid DrugOrder Frequency "
				                + prescriptionItem.getDrugOrder().getFrequency());
			}
			
			if ((prescriptionItem.getDrugOrder().getQuantityUnits() == null)
			        || StringUtils.isBlank(prescriptionItem.getDrugOrder().getQuantityUnits().getUuid())) {
				throw new PharmacyBusinessException(
				        "Cannot create Prescription for prescriptionItem with Invalid DrugOrder Quantity Units "
				                + prescriptionItem.getDrugOrder().getQuantityUnits());
			}
			
			if ((prescriptionItem.getRegime() != null)
			        && StringUtils.isNotBlank(prescriptionItem.getRegime().getUuid())) {
				regimesUuid.add(prescriptionItem.getRegime().getUuid());
			}
		}
		
		if (regimesUuid.size() > 1) {
			
			throw new PharmacyBusinessException("Cannot Create a Prescription for Arv Drugs having different regimes "
			        + StringUtils.join(prescription.getPrescriptionItems(), "|"));
		}
		
		final Concept regime = prescription.getRegime();
		if (regime != null) {
			
			final Patient patient = Context.getPatientService().getPatientByUuid(prescription.getPatient().getUuid());
			final PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
			
			final List<Prescription> existingsPrescriptions = prescriptionService
			        .findActivePrescriptionsByPatient(patient, prescription.getPrescriptionDate());
			
			for (final Prescription existingPrescription : existingsPrescriptions) {
				
				if (existingPrescription.getRegime() != null) {
					
					throw new PharmacyBusinessException("Cannot create a new ARV Prescription for Patient "
					        + this.getFormattedPatientToDisplay(patient) + " while exist Active ARV prescription "
					        + StringUtils.join(existingsPrescriptions, "|"));
					
				}
			}
		}
		
	}
	
	private String getFormattedPatientToDisplay(final Patient patient) {
		return ((patient.getPatientIdentifier() != null)
		        ? patient.getPatientIdentifier().getIdentifier() + " - " + patient.getPersonName().getFullName()
		        : patient.getGivenName() + " " + patient.getFamilyName());
	}
}
