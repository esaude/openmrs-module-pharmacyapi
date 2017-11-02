/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.drugregime.service.DrugRegimeService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DrugRegimeServiceTest extends BaseModuleContextSensitiveTest {
	
	private DrugRegimeService drugRegimeService;
	
	@Before
	public void setup() {
		
		this.drugRegimeService = Context.getService(DrugRegimeService.class);
	}
	
	@Test
	public void shouldInjectServices() throws Exception {
		
		Assert.assertNotNull(this.drugRegimeService);
	}
}
