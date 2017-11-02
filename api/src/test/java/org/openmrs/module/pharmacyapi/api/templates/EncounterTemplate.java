/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.templates;

import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.ARV_DOSAGE_AMOUNT;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.ARV_PLAN;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DOSAGE_FREQUENCY;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DOSING_INSTRUCTIONS;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DOSING_UNITS;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DRUG_ROUTES;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DURATION;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.DURATION_UNITS;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.MEDICATION_QUANTITY;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.POC_MAPPING_PRESCRIPTION_DATE;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.PREVIOUS_ANTIRETROVIRAL_DRUGS;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.REASON_ANTIRETROVIRALS_STOPPED;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.TREATMENT_PRESCRIBED;
import static org.openmrs.module.pharmacyapi.api.templates.ObsTemplate.TREATMENT_PRESCRIBED_SET;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class EncounterTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	public static final String DISPENSATION = "DISPENSATION";
	
	@Override
	public void load() {
		Fixture.of(Encounter.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("patient", this.one(Patient.class, PatientTemplate.VALID));
				this.add("encounterDatetime", this.instant("now"));
				this.add(
				    "obs",
				    this.has(14).of(Obs.class, TREATMENT_PRESCRIBED_SET, TREATMENT_PRESCRIBED, ARV_DOSAGE_AMOUNT, ARV_PLAN,
				        DOSAGE_FREQUENCY, DOSING_INSTRUCTIONS, DOSING_UNITS, DRUG_ROUTES, DURATION, DURATION_UNITS,
				        JUSTIFICATION_TO_CHANGE_ARV_TREATMENT, POC_MAPPING_PRESCRIPTION_DATE, PREVIOUS_ANTIRETROVIRAL_DRUGS,
				        REASON_ANTIRETROVIRALS_STOPPED));
			}
		});
		
		Fixture.of(Encounter.class).addTemplate(DISPENSATION, new Rule() {
			
			{
				this.add("patient", this.one(Patient.class, PatientTemplate.VALID));
				this.add("encounterDatetime", this.instant("now"));
				this.add("obs", this.has(1).of(Obs.class, MEDICATION_QUANTITY));
			}
		});
	}
}
