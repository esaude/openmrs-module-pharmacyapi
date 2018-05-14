/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.service.pharmacyheuristicservice;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedForms;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.templates.DrugTemplate;
import org.openmrs.module.pharmacyapi.api.templates.EncounterTypeTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;

import br.com.six2six.fixturefactory.Fixture;

public class PharmacyHeuristicServiceTest extends BaseTest {
	
	@Test
	@Ignore
	public void shouldFindEncounterByChildPatient() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient childPatient = new Patient();
		childPatient.setBirthdateFromAge(14, null);
		
		final EncounterType encounterType = pharmacyHeuristicService.getEncounterTypeByPatientAge(childPatient);
		
		Assert.assertNotNull(encounterType);
		Assert.assertEquals(EncounterTypeTemplate.ARV_FOLLOW_UP_CHILD, encounterType.getUuid());
	}
	
	@Test
	public void shouldFindEncounterByAdultPatient() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient adultPatient = new Patient();
		adultPatient.setBirthdateFromAge(40, null);
		
		final EncounterType encounterType = pharmacyHeuristicService.getEncounterTypeByPatientAge(adultPatient);
		
		Assert.assertNotNull(encounterType);
		Assert.assertEquals(EncounterTypeTemplate.ARV_FOLLOW_UP_ADULT, encounterType.getUuid());
	}
	
	@Test
	public void shouldFindDrugByOrderUuid() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final String orderUuid = "921de0a3-05c4-444a-be03-e01b4c4b9142";
		
		final Drug aspirinDrug = Fixture.from(Drug.class).gimme(DrugTemplate.ASPIRIN);
		
		final Drug drug = pharmacyHeuristicService.findDrugByOrderUuid(orderUuid);
		
		Assert.assertNotNull(drug);
		Assert.assertEquals(aspirinDrug.getUuid(), drug.getUuid());
	}
	
	@Test
	public void shouldFindEncounterByPatientAndEncounterTypeAndOrder() throws Exception {
		this.executeDataSet("pharmacyheuristicservice/shouldFindEncounterByPatientAndEncounterTypeAndOrder.xml");
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient patient = new Patient(7);
		final EncounterType encounterType = new EncounterType(1);
		final Order order = new Order(100);
		
		final Encounter encounter = pharmacyHeuristicService.findEncounterByPatientAndEncounterTypeAndOrder(patient,
		    encounterType, order);
		
		Assert.assertNotNull(encounter);
		Assert.assertEquals(patient.getPatientId(), encounter.getPatient().getPatientId());
		Assert.assertEquals(encounterType.getEncounterTypeId(), encounter.getEncounterType().getEncounterTypeId());
		Assert.assertEquals(1, encounter.getOrders().size());
		Assert.assertEquals(order.getOrderId(), encounter.getOrders().iterator().next().getOrderId());
	}
	
	@Test
	public void shouldFindObsByOrder() throws Exception {
		this.executeDataSet("pharmacyheuristicservice/shouldFindEncounterByPatientAndEncounterTypeAndOrder.xml");
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Order order = new Order(100);
		
		final List<Obs> obs = pharmacyHeuristicService.findObservationsByOrder(order);
		
		Assert.assertNotNull(obs);
		Assert.assertEquals(1, obs.size());
		Assert.assertEquals(order.getOrderId(), obs.get(0).getOrder().getOrderId());
		
	}
	
	@Test
	public void shouldGetPediatricFormByChildtPatientAge() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient patient = Context.getPatientService().getPatient(6);
		
		final Form childForm = pharmacyHeuristicService.getFormByPatientAge(patient);
		
		Assert.assertNotNull(childForm);
		
		Assert.assertEquals(MappedForms.PEDIATRICS_FOLLOW_UP, childForm.getUuid());
	}
	
	@Test
	public void shouldGetAdultFormByAdultPatientAge() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient patient = Context.getPatientService().getPatient(7);
		
		final Form adultForm = pharmacyHeuristicService.getFormByPatientAge(patient);
		
		Assert.assertNotNull(adultForm);
		
		Assert.assertEquals(MappedForms.ADULT_FOLLOW_UP, adultForm.getUuid());
	}
	
	@Test(expected = PharmacyBusinessException.class)
	public void shouldThrowExceptionForNonGivenPatient() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Patient patient = null;
		pharmacyHeuristicService.getFormByPatientAge(patient);
	}
	
	@Test
	public void shouldFindfindLastVisitByPatient() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2005);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		final Visit lastVisit = pharmacyHeuristicService.findLastVisitByPatientAndEncounterDate(new Patient(2),
		    calendar.getTime());
		
		Assert.assertNotNull(lastVisit);
		Assert.assertEquals(Integer.valueOf(3), lastVisit.getId());
	}
	
	@Test(expected = PharmacyBusinessException.class)
	public void shouldThrowExceptionOnFindingLastVisitByForNonExistPatient() throws Exception {
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		pharmacyHeuristicService.findLastVisitByPatientAndEncounterDate(new Patient(200), new Date());
	}
	
	@Test
	public void shoudFindEncountersByPatientAndEncounterTypeAndLocationAndDate() throws Exception {
		this.executeDataSet(
		        "pharmacyheuristicservice/shoudFindEncountersByPatientAndEncounterTypeAndLocationAndDate-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 10);
		calendar.set(Calendar.DAY_OF_MONTH, 9);
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Encounter encounter = pharmacyHeuristicService
		        .findLastEncounterByPatientAndEncounterTypeAndLocationAndDate(new Patient(7), new EncounterType(2),
		            new Location(1), calendar.getTime());
		
		Assert.assertNotNull(encounter);
		Assert.assertEquals(Integer.valueOf(1005), encounter.getEncounterId());
	}
}
