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
