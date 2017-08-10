package org.openmrs.module.pharmacyapi.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.drugitem.service.DrugItemService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DrugItemServiceTest extends BaseModuleContextSensitiveTest {
	
	private DrugItemService drugItemService;
	
	@Before
	public void setup() {
		this.drugItemService = Context.getService(DrugItemService.class);
	}
	
	@Test
	public void shouldInjectSevices() throws Exception {
		
		Assert.assertNotNull(this.drugItemService);
	}
}
