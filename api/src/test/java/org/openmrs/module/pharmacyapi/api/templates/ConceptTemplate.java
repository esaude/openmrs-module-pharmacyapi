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
	
	public static final String TRIOMUNE30 = "d144d24f-6913-4b63-9660-a9108c2bebef";
	
	public static final String ASPIRIN = "15f83cd6-64e9-4e06-a5f9-364d3b14a43d";
	
	public static final String NYQUIL = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab";
	
	public static final String AZT_3TC_NVP = "e1dd2f44-1d5f-11e0-b929-000c29ad1d07";
	
	public static final String ARV_FIRST_LINE_PLAN = "a6bbe1ac-5243-40e4-98cb-7d4a1467dfbe";
	
	public static final String START_DRUGS_ARV_PLAN = "e1d9ef28-1d5f-11e0-b929-000c29ad1d07";
	
	public final static String NEVIRAPINA = "e1dd2f44-1d5f-11e0-b929-000c29ad1d07";
	
	public static final String FREQUENCY_ONCE_A_DAY = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab";
	
	public static final String FREQUENCY_TWICE_A_DAY = "9d717849-10e8-11e5-9009-0242ac110012";
	
	public static final String FREQUENCY_THRICE_A_DAY = "9d71d41e-10e8-11e5-9009-0242ac110012";
	
	public static final String ROUTE_UNKNOWN = "e10ffe54-5184-4efe-8960-cd565ec1cdf8";
	
	public static final String TABS_QUANTITY_UNITS = "5a2aa3db-68a3-11e3-bd76-0800271c1b75";
	
	public static final String MG_DOSE_UNITS = "557b9699-68a3-11e3-bd76-0800271c1b75";
	
	@Override
	public void load() {
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.TREATMENT_PRESCRIBED_SET, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.TREATMENT_PRESCRIBED_SET);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.TREATMENT_PRESCRIBED, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.TREATMENT_PRESCRIBED);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ARV_DOSAGE_AMOUNT, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ARV_DOSAGE_AMOUNT);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.PREVIOUS_ANTIRETROVIRAL_DRUGS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.PREVIOUS_ANTIRETROVIRAL_DRUGS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ARV_PLAN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ARV_PLAN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ARV_THERAPEUTIC_LINE, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ARV_THERAPEUTIC_LINE);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.REASON_ANTIRETROVIRALS_STOPPED, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.REASON_ANTIRETROVIRALS_STOPPED);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DOSING_UNITS, new Rule() {
			
			{
				this.add("conceptId", 6390);
				this.add("uuid", "9d66a447-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DOSAGE_FREQUENCY, new Rule() {
			
			{
				this.add("conceptId", 6338);
				this.add("uuid", "5368f4d6-10e7-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DRUG_ROUTES, new Rule() {
			
			{
				this.add("conceptId", 6398);
				this.add("uuid", "9d6a9238-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DURATION, new Rule() {
			
			{
				this.add("conceptId", 1710);
				this.add("uuid", "e1de27a0-1d5f-11e0-b929-000c29ad1d07");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DURATION_UNITS, new Rule() {
			
			{
				this.add("conceptId", 6408);
				this.add("uuid", "9d6f0bea-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DOSING_INSTRUCTIONS, new Rule() {
			
			{
				this.add("conceptId", 6414);
				this.add("uuid", "9d73c2a7-10e8-11e5-9009-0242ac110012");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.POC_MAPPING_PRESCRIPTION_DATE, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.POC_MAPPING_PRESCRIPTION_DATE);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.BEFORE_MEALS, new Rule() {
			
			{
				this.add("conceptId", 6415);
				this.add("uuid", "9d7408af-10e8-11e5-9009-0242ac110012");
				this.add("names", this.has(1).of(ConceptName.class, ConceptNameTemplate.BEFORE_MEALS));
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DURATION_WEEKS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.DURATION_WEEKS);
				// this.add("conceptMappings", this.has(1).of(ConceptMap.class,
				// ConceptMapTemplate.VALID));
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DURATION_MONTHS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.DURATION_MONTHS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.DURATION_DAYS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.DURATION_DAYS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.MEDICATION_QUANTITY, new Rule() {
			
			{
				this.add("conceptId", 1715);
				this.add("uuid", "e1de2ca0-1d5f-11e0-b929-000c29ad1d07");
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.NEVIRAPINA, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.NEVIRAPINA);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ARV_FIRST_LINE_PLAN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ARV_FIRST_LINE_PLAN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.START_DRUGS_ARV_PLAN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.START_DRUGS_ARV_PLAN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.AZT_3TC_NVP, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.AZT_3TC_NVP);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.TRIOMUNE30, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.TRIOMUNE30);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ASPIRIN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ASPIRIN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.NYQUIL, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.NYQUIL);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.FREQUENCY_ONCE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.FREQUENCY_ONCE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.FREQUENCY_TWICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.FREQUENCY_TWICE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.FREQUENCY_THRICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.FREQUENCY_THRICE_A_DAY);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.ROUTE_UNKNOWN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.ROUTE_UNKNOWN);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.TABS_QUANTITY_UNITS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.TABS_QUANTITY_UNITS);
			}
		});
		
		Fixture.of(Concept.class).addTemplate(ConceptTemplate.MG_DOSE_UNITS, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", ConceptTemplate.MG_DOSE_UNITS);
			}
		});
	}
}
