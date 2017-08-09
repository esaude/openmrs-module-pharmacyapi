package org.openmrs.module.pharmacyapi.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class PrescriptionDispensationServiceTest extends BaseModuleContextSensitiveTest {
	
	private PrescriptionDispensationService prescriptionDispensationService;
	
	@Before
	public void setUp() {
		this.prescriptionDispensationService = Context.getService(PrescriptionDispensationService.class);
	}
	
	@Test
	public void shouldInjectService() throws Exception {
		
		Assert.assertNotNull(this.prescriptionDispensationService);
	}
}
