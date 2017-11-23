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

import java.util.UUID;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class ObsTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	public static final String TREATMENT_PRESCRIBED_SET = "TREATMENT_PRESCRIBED_SET";
	
	public static final String TREATMENT_PRESCRIBED = "TREATMENT_PRESCRIBED";
	
	public static final String ARV_DOSAGE_AMOUNT = "ARV_DASAGE_AMOUNT";
	
	public static final String PREVIOUS_ANTIRETROVIRAL_DRUGS = "PREVIOUS_ANTIRETROVIRAL_DRUGS";
	
	public static final String ARV_PLAN = "ARV_PLAN";
	
	public static final String REASON_ANTIRETROVIRALS_STOPPED = "REASON_ANTIRETROVIRALS_STOPPED";
	
	public static final String JUSTIFICATION_TO_CHANGE_ARV_TREATMENT = "JUSTIFICATION_TO_CHANGE_ARV_TREATMENT";
	
	public static final String DOSING_UNITS = "DOSING_UNITS";
	
	public static final String DOSAGE_FREQUENCY = "DOSAGE_FREQUENCY";
	
	public static final String DRUG_ROUTES = "DRUG_ROUTES";
	
	public static final String DURATION = "DURATION";
	
	public static final String DURATION_UNITS = "DURATION_UNITS";
	
	public static final String DOSING_INSTRUCTIONS = "DOSING_INSTRUCTIONS";
	
	public static final String POC_MAPPING_PRESCRIPTION_DATE = "POC_MAPPING_PRESCRIPTION_DATE";
	
	public static final String MEDICATION_QUANTITY = "MEDICATION_QUANTITY";
	
	@Override
	public void load() {
		
		Fixture.of(Obs.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("obsId", this.random(Integer.class, this.range(1, 100)));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(TREATMENT_PRESCRIBED_SET).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.TREATMENT_PRESCRIBED_SET));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(TREATMENT_PRESCRIBED).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.TREATMENT_PRESCRIBED));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(ARV_DOSAGE_AMOUNT).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.ARV_DOSAGE_AMOUNT));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(PREVIOUS_ANTIRETROVIRAL_DRUGS).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.PREVIOUS_ANTIRETROVIRAL_DRUGS));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(ARV_PLAN).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.ARV_PLAN));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(REASON_ANTIRETROVIRALS_STOPPED).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.REASON_ANTIRETROVIRALS_STOPPED));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(JUSTIFICATION_TO_CHANGE_ARV_TREATMENT).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DOSING_UNITS).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DOSING_UNITS));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DOSAGE_FREQUENCY).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DOSAGE_FREQUENCY));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DRUG_ROUTES).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DRUG_ROUTES));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DURATION).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DURATION));
				this.add("valueNumeric", this.random(Double.class, this.range(1, 10)));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DURATION_UNITS).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DURATION_UNITS));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(DOSING_INSTRUCTIONS).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.DOSING_INSTRUCTIONS));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(POC_MAPPING_PRESCRIPTION_DATE).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.POC_MAPPING_PRESCRIPTION_DATE));
			}
		});
		
		Fixture.of(Obs.class).addTemplate(MEDICATION_QUANTITY).inherits(VALID, new Rule() {
			
			{
				this.add("uuid", UUID.randomUUID().toString().replace("-", ""));
				this.add("concept", this.one(Concept.class, ConceptTemplate.MEDICATION_QUANTITY));
				this.add("valueNumeric", 10.0);
			}
		});
	}
}
