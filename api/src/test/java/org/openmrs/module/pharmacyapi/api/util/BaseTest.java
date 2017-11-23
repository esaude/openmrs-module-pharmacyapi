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
package org.openmrs.module.pharmacyapi.api.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.templates.ConceptTemplate;
import org.openmrs.module.pharmacyapi.api.templates.EncounterTypeTemplate;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.processor.HibernateProcessor;

/**
 * @author Stélio Moiane
 */
public abstract class BaseTest extends BaseModuleContextSensitiveTest {
	
	private static final String EXAMPLE_XML_DATASET_PACKAGE_PATH = "standardTestDataset.xml";
	
	@BeforeClass
	public static void setupClass() {
		
		FixtureFactoryLoader.loadTemplates("org.openmrs.module.pharmacyapi.api.templates");
		
	}
	
	@Override
	@Before
	public void baseSetupWithStandardDataAndAuthentication() throws Exception {
		
		if (!Context.isSessionOpen()) {
			Context.openSession();
		}
		this.initializeInMemoryDatabase();
		
		this.deleteAllData();
		
		if (this.useInMemoryDatabase()) {
			this.initializeInMemoryDatabase();
		} else {
			this.executeDataSet(BaseContextSensitiveTest.INITIAL_XML_DATASET_PACKAGE_PATH);
		}
		
		this.executeDataSet(BaseTest.EXAMPLE_XML_DATASET_PACKAGE_PATH);
		
		// Commit so that it is not rolled back after a test.
		this.getConnection().commit();
		
		this.updateSearchIndex();
		this.authenticate();
		
		Context.clearSession();
		
		this.setUp();
	}
	
	public void setUp() {
		
		final SessionFactory sessionFactory = (SessionFactory) this.applicationContext.getBean("sessionFactory");
		
		final Session currentSession = sessionFactory.getCurrentSession();
		Fixture.from(EncounterType.class).uses(new HibernateProcessor(currentSession))
		        .gimme(EncounterTypeTemplate.ARV_FOLLOW_UP_CHILD);
		Fixture.from(EncounterType.class).uses(new HibernateProcessor(currentSession))
		        .gimme(EncounterTypeTemplate.DISPENSATION_ENCOUNTER_TYPE);
		Fixture.from(EncounterType.class).uses(new HibernateProcessor(currentSession))
		        .gimme(EncounterTypeTemplate.FILA_ENCOUNTER_TYPE);
		Fixture.from(Concept.class).uses(new HibernateProcessor(currentSession))
		        .gimme(ConceptTemplate.TREATMENT_PRESCRIBED);
		Fixture.from(Concept.class).uses(new HibernateProcessor(currentSession))
		        .gimme(ConceptTemplate.REASON_ANTIRETROVIRALS_STOPPED);
		Fixture.from(Concept.class).uses(new HibernateProcessor(currentSession))
		        .gimme(ConceptTemplate.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT);
		Fixture.from(Concept.class).uses(new HibernateProcessor(currentSession))
		        .gimme(ConceptTemplate.TREATMENT_PRESCRIBED_SET);
		Fixture.from(Concept.class).uses(new HibernateProcessor(currentSession))
		        .gimme(ConceptTemplate.ARV_DOSAGE_AMOUNT);
		
		// Fixture.from(OrderFrequency.class).uses(new
		// HibernateProcessor(currentSession))
		// .gimme(OrderFrequencyTemplate.FREQUENCY_ONCE_A_DAY);
		// Fixture.from(OrderFrequency.class).uses(new
		// HibernateProcessor(currentSession))
		// .gimme(OrderFrequencyTemplate.FREQUENCY_TWICE_A_DAY);
		// Fixture.from(OrderFrequency.class).uses(new
		// HibernateProcessor(currentSession))
		// .gimme(OrderFrequencyTemplate.FREQUENCY_THRICE_A_DAY);
		
		// Fixture.from(CDrugOrderValidatoroncept.class).uses(new
		// HibernateProcessor(currentSession)).gimme(ConceptTemplate.DURATION_DAYS);
		// Fixture.from(Concept.class).uses(new
		// HibernateProcessor(currentSession)).gimme(ConceptTemplate.DURATION_WEEKS);
		// Fixture.from(Concept.class).uses(new
		// HibernateProcessor(currentSession)).gimme(ConceptTemplate.DURATION_MONTHS);
		//
		// Fixture.from(ConceptMap.class).uses(new
		// HibernateProcessor(currentSession)).gimme(ConceptMapTemplate.VALID);
		
	}
}
