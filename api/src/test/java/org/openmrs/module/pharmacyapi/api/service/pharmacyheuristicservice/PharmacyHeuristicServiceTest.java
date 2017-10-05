package org.openmrs.module.pharmacyapi.api.service.pharmacyheuristicservice;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.templates.DrugTemplate;
import org.openmrs.module.pharmacyapi.api.templates.EncounterTypeTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;

import br.com.six2six.fixturefactory.Fixture;

public class PharmacyHeuristicServiceTest extends BaseTest {
	
	@Test
	public void shouldFindEncounterByChildPatient() throws Exception {
		
		PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		Patient childPatient = new Patient();
		childPatient.setBirthdateFromAge(14, null);
		
		EncounterType childEncounterType = Fixture.from(EncounterType.class)
		        .gimme(EncounterTypeTemplate.ARV_FOLLOW_UP_CHILD);
		EncounterType encounterType = pharmacyHeuristicService.getEncounterTypeByPatientAge(childPatient);
		
		Assert.assertNotNull(encounterType);
		Assert.assertEquals(childEncounterType.getUuid(), encounterType.getUuid());
	}
	
	@Test
	public void shouldFindEncounterByAdultPatient() throws Exception {
		
		PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		Patient adultPatient = new Patient();
		adultPatient.setBirthdateFromAge(40, null);
		
		EncounterType adultEncounterType = Fixture.from(EncounterType.class)
		        .gimme(EncounterTypeTemplate.ARV_FOLLOW_UP_ADULT);
		EncounterType encounterType = pharmacyHeuristicService.getEncounterTypeByPatientAge(adultPatient);
		
		Assert.assertNotNull(encounterType);
		Assert.assertEquals(adultEncounterType.getUuid(), encounterType.getUuid());
	}
	
	@Test
	public void shouldFindDrugByOrderUuid() throws Exception {
		
		PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		String orderUuid = "921de0a3-05c4-444a-be03-e01b4c4b9142";
		
		Drug aspirinDrug = Fixture.from(Drug.class).gimme(DrugTemplate.ASPIRIN);
		
		Drug drug = pharmacyHeuristicService.findDrugByOrderUuid(orderUuid);
		
		Assert.assertNotNull(drug);
		Assert.assertEquals(aspirinDrug.getUuid(), drug.getUuid());
	}
	
	@Test
	public void shouldFindEncounterByPatientAndEncounterTypeAndOrder() throws Exception {
		executeDataSet("pharmacyheuristicservice/shouldFindEncounterByPatientAndEncounterTypeAndOrder.xml");
		
		PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		Patient patient = new Patient(7);
		EncounterType encounterType = new EncounterType(1);
		Order order = new Order(100);
		
		Encounter encounter = pharmacyHeuristicService.findEncounterByPatientAndEncounterTypeAndOrder(patient,
		    encounterType, order);
		
		Assert.assertNotNull(encounter);
		Assert.assertEquals(patient.getPatientId(), encounter.getPatient().getPatientId());
		Assert.assertEquals(encounterType.getEncounterTypeId(), encounter.getEncounterType().getEncounterTypeId());
		Assert.assertEquals(1, encounter.getOrders().size());
		Assert.assertEquals(order.getOrderId(), encounter.getOrders().iterator().next().getOrderId());
	}
	
	public void shouldFindObsByOrder() throws Exception {
		executeDataSet("pharmacyheuristicservice/shouldFindEncounterByPatientAndEncounterTypeAndOrder.xml");
		
		PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		Order order = new Order(100);
		
		List<Obs> obs = pharmacyHeuristicService.findObsByOrder(order);
		
		Assert.assertNotNull(obs);
		Assert.assertEquals(1, obs.size());
		Assert.assertEquals(order.getOrderId(), obs.get(0).getOrder().getOrderId());
		
	}
}
