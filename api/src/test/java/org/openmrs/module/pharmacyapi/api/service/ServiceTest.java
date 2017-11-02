/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 *
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.pharmacyapi.api.drugitem.service.DrugItemService;
import org.openmrs.module.pharmacyapi.api.drugregime.service.DrugRegimeService;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * @author Guimino Neves
 */
public class ServiceTest extends BaseModuleContextSensitiveTest {
	
	private PatientService patientService;
	
	private PrescriptionService prescriptionService;
	
	private DispensationService dispensationService;
	
	private DrugItemService drugItemService;
	
	private DrugRegimeService drugRegimeService;
	
	private PharmacyHeuristicService pharmacyHeuristicService;
	
	@Before
	public void before() throws Exception {
		
		this.initializeInMemoryDatabase();
		this.authenticate();
	}
	
	@Test
	public void shoulFindServices() throws Exception {
		
		this.prescriptionService = Context.getService(PrescriptionService.class);
		this.dispensationService = Context.getService(DispensationService.class);
		this.patientService = Context.getService(PatientService.class);
		this.drugItemService = Context.getService(DrugItemService.class);
		this.drugRegimeService = Context.getService(DrugRegimeService.class);
		this.pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		Assert.assertNotNull(this.prescriptionService);
		Assert.assertNotNull(this.dispensationService);
		Assert.assertNotNull(this.patientService);
		Assert.assertNotNull(this.drugItemService);
		Assert.assertNotNull(this.drugRegimeService);
		Assert.assertNotNull(this.pharmacyHeuristicService);
	}
}
