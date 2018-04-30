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
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
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
	
	@Test
	public void shouldSavePrescriptionDispensation() {
		
		final Patient patient = new Patient(7);
		patient.setUuid("5946f880-b197-400b-9caa-a3c661d23041");
		
		final Encounter prescription = new Encounter();
		final Encounter dispensation = new Encounter();
		
		prescription.setPatient(patient);
		dispensation.setPatient(patient);
		
		final PrescriptionDispensation savedPrescriptionDispensation = this.prescriptionDispensationService
		        .savePrescriptionDispensation(patient, prescription, dispensation);
		
		Assert.assertNotNull(savedPrescriptionDispensation);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSavePrescriptionDispensationForDispensationWithWrongPatient() {
		
		final Patient patient = new Patient(7);
		patient.setUuid("5946f880-b197-400b-9caa-a3c661d23041");
		
		final Encounter prescription = new Encounter();
		final Encounter dispensation = new Encounter();
		
		prescription.setPatient(patient);
		dispensation.setPatient(new Patient());
		
		this.prescriptionDispensationService.savePrescriptionDispensation(patient, prescription, dispensation);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSavePrescriptionDispensationForDispensationForNotProvidedPatient() {
		
		final Encounter prescription = new Encounter();
		final Encounter dispensation = new Encounter();
		
		this.prescriptionDispensationService.savePrescriptionDispensation(null, prescription, dispensation);
	}
	
	@Test
	public void shouldRetirePrescriptionDispensation() throws Exception {
		this.executeDataSet("prescriptionservice/shouldRetirePrescriptionDispensation-dataset.xml");
		
		final PrescriptionDispensation prescriptionDispensation = new PrescriptionDispensation();
		prescriptionDispensation.setUuid("3a9eb480-4112-4fdd-8aba-0175bddb3706");
		
		this.prescriptionDispensationService.retire(new User(), prescriptionDispensation, "test reason");
		
		// Assert.assertTrue(prescriptionDispensation.isRetired());
	}
}
