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

import org.junit.Before;
import org.junit.BeforeClass;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

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
		
	}
}
