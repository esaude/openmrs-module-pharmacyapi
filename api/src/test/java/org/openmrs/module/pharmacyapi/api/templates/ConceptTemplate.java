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

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class ConceptTemplate implements BaseTemplateLoader {
	
	public static final String TREATMENT_PRESCRIBED_SET = MappedConcepts.TREATMENT_PRESCRIBED_SET;
	
	public static final String TREATMENT_PRESCRIBED = MappedConcepts.TREATMENT_PRESCRIBED;
	
	public static final String ARV_DOSAGE_AMOUNT = MappedConcepts.DOSAGE_AMOUNT;
	
	public static final String PREVIOUS_ANTIRETROVIRAL_DRUGS = MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS;
	
	public static final String ARV_PLAN = MappedConcepts.ARV_PLAN;
	
	public static final String ARV_THERAPEUTIC_LINE = MappedConcepts.ARV_THERAPEUTIC_LINE;
	
	public static final String REASON_ANTIRETROVIRALS_STOPPED = MappedConcepts.REASON_ANTIRETROVIRALS_STOPPED;
	
	public static final String JUSTIFICATION_TO_CHANGE_ARV_TREATMENT = MappedConcepts.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT;
	
	public static final String POC_MAPPING_PRESCRIPTION_DATE = MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE;
	
	public static final String DOSING_UNITS = "DOSING_UNITS";
	
	public static final String DOSAGE_FREQUENCY = "DOSAGE_FREQUENCY";
	
	public static final String DRUG_ROUTES = "DRUG_ROUTES";
	
	public static final String DURATION = "DURATION";
	
	public static final String DURATION_UNITS = "DURATION_UNITS";
	
	public static final String DOSING_INSTRUCTIONS = "DOSING_INSTRUCTIONS";
	
	public static final String BEFORE_MEALS = "BEFORE_MEALS";
	
	public static final String DURATION_DAYS = MappedConcepts.DURATION_DAYS;
	
	public static final String DURATION_WEEKS = MappedConcepts.DURATION_WEEKS;
	
	public static final String DURATION_MONTHS = MappedConcepts.DURATION_MONTHS;
	
	public static final String MEDICATION_QUANTITY = "MEDICATION_QUANTITY";
	
	public static final String AZT_3TC_NVP = "AZT_3TC_NVP";
	
	public static final String TRIOMUNE30 = "d144d24f-6913-4b63-9660-a9108c2bebef";
	
	public static final String ASPIRIN = "15f83cd6-64e9-4e06-a5f9-364d3b14a43d";
	
	public static final String NYQUIL = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab";
	
	public static final String FREQUENCY_ONCE_A_DAY = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab"; // "9d7127f9-10e8-11e5-9009-0242ac110012";
	
	public static final String FREQUENCY_TWICE_A_DAY = "9d717849-10e8-11e5-9009-0242ac110012";
	
	public static final String FREQUENCY_THRICE_A_DAY = "9d71d41e-10e8-11e5-9009-0242ac110012";
	
	public static final String ROUTE_UNKNOWN = "e10ffe54-5184-4efe-8960-cd565ec1cdf8";
	
	public static final String TABS_QUANTITY_UNITS = "5a2aa3db-68a3-11e3-bd76-0800271c1b75";
	
	public static final String MG_DOSE_UNITS = "557b9699-68a3-11e3-bd76-0800271c1b75";
	
	@Override
	public void load() {
		
		Fixture.of(Concept.class).addTemplate(TREATMENT_PRESCRIBED_SET, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", TREATMENT_PRESCRIBED_SET);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(TREATMENT_PRESCRIBED, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", TREATMENT_PRESCRIBED);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ARV_DOSAGE_AMOUNT, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ARV_DOSAGE_AMOUNT);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(PREVIOUS_ANTIRETROVIRAL_DRUGS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", PREVIOUS_ANTIRETROVIRAL_DRUGS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ARV_PLAN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ARV_PLAN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ARV_THERAPEUTIC_LINE, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ARV_THERAPEUTIC_LINE);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(REASON_ANTIRETROVIRALS_STOPPED, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", REASON_ANTIRETROVIRALS_STOPPED);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(JUSTIFICATION_TO_CHANGE_ARV_TREATMENT, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", JUSTIFICATION_TO_CHANGE_ARV_TREATMENT);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DOSING_UNITS, new Rule() {
			
			{
				this.add("conceptId", 6390);
				this.add("uuid", "9d66a447-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DOSAGE_FREQUENCY, new Rule() {
			
			{
				this.add("conceptId", 6338);
				this.add("uuid", "5368f4d6-10e7-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DRUG_ROUTES, new Rule() {
			
			{
				this.add("conceptId", 6398);
				this.add("uuid", "9d6a9238-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DURATION, new Rule() {
			
			{
				this.add("conceptId", 1710);
				this.add("uuid", "e1de27a0-1d5f-11e0-b929-000c29ad1d07");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DURATION_UNITS, new Rule() {
			
			{
				this.add("conceptId", 6408);
				this.add("uuid", "9d6f0bea-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DOSING_INSTRUCTIONS, new Rule() {
			
			{
				this.add("conceptId", 6414);
				this.add("uuid", "9d73c2a7-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(POC_MAPPING_PRESCRIPTION_DATE, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", POC_MAPPING_PRESCRIPTION_DATE);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(BEFORE_MEALS, new Rule() {
			
			{
				this.add("conceptId", 6415);
				this.add("uuid", "9d7408af-10e8-11e5-9009-0242ac110012");
				this.add("names", this.has(1).of(ConceptName.class, ConceptNameTemplate.BEFORE_MEALS));
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DURATION_WEEKS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", DURATION_WEEKS);
				// this.add("conceptMappings", this.has(1).of(ConceptMap.class,
				// ConceptMapTemplate.VALID));
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DURATION_MONTHS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", DURATION_MONTHS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(DURATION_DAYS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", DURATION_DAYS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(MEDICATION_QUANTITY, new Rule() {
			
			{
				this.add("conceptId", 1715);
				this.add("uuid", "e1de2ca0-1d5f-11e0-b929-000c29ad1d07");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(AZT_3TC_NVP, new Rule() {
			
			{
				this.add("conceptId", 1651);
				this.add("uuid", "e1dd2f44-1d5f-11e0-b929-000c29ad1d07");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(TRIOMUNE30, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", TRIOMUNE30);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ASPIRIN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ASPIRIN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(NYQUIL, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", NYQUIL);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(FREQUENCY_ONCE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", FREQUENCY_ONCE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(FREQUENCY_TWICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", FREQUENCY_TWICE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(FREQUENCY_THRICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", FREQUENCY_THRICE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ROUTE_UNKNOWN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ROUTE_UNKNOWN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(TABS_QUANTITY_UNITS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", TABS_QUANTITY_UNITS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(MG_DOSE_UNITS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", MG_DOSE_UNITS);
			}
		});
	}
}
