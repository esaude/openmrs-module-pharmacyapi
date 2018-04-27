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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionValidator {
	
	private final List<IPrescriptionValidationRule> rulesForCreatingPrescription = new ArrayList<>();
	
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
	
	@Autowired
	private PrescriptionExpirationDateteRule prescriptionExpirationDateteRule;
	
	@PostConstruct
	private void initializeRules() {
		
		this.rulesForCreatingPrescription.add(this.patientRule);
		this.rulesForCreatingPrescription.add(this.ProviderRule);
		this.rulesForCreatingPrescription.add(this.locationRule);
		this.rulesForCreatingPrescription.add(this.prescriptionDateRule);
		this.rulesForCreatingPrescription.add(this.prescriptionItemRule);
		this.rulesForCreatingPrescription.add(this.prescriptionExpirationDateteRule);
	}
	
	public void validateCreation(final Prescription prescription) throws PharmacyBusinessException {
		
		for (final IPrescriptionValidationRule rule : this.rulesForCreatingPrescription) {
			
			rule.validate(prescription);
		}
	}
}
