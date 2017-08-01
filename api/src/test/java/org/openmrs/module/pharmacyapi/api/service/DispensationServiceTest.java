/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationServiceImpl;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.templates.DispensationTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;
import org.openmrs.module.pharmacyapi.api.util.EntityFactory;

/**
 * @author Stélio Moiane
 */
public class DispensationServiceTest extends BaseTest {
	
	private DispensationService dispensationService;
	
	@Before
	public void setUp() {
		this.dispensationService = new DispensationServiceImpl();
	}
	
	@Test
	@Ignore
	public void shouldDispenseOrders() throws PharmacyBusinessException {
		
		// TODO I need to get time to do this test........
		
		final Dispensation dispensation = EntityFactory.gimme(Dispensation.class, DispensationTemplate.VALID);
		
		final Dispensation createdDispensation = this.dispensationService.dispense(dispensation);
		
		assertNull(createdDispensation);
	}
}
