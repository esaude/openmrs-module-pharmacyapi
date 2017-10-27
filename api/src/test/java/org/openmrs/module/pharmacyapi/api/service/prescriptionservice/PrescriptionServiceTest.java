package org.openmrs.module.pharmacyapi.api.service.prescriptionservice;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.openmrs.module.pharmacyapi.api.templates.EncounterTypeTemplate;
import org.openmrs.module.pharmacyapi.api.templates.LocationTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PatientTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PrescriptionItemTemplate;
import org.openmrs.module.pharmacyapi.api.templates.ProviderTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;

import br.com.six2six.fixturefactory.Fixture;

public class PrescriptionServiceTest extends BaseTest {
	
	/**
	 * @should create a Prescription for non ARV Drug
	 * @throws Exception
	 */
	@Test
	public void shouldCreateNonArvPrescription() throws Exception {
		
		PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
		
		Prescription prescription = new Prescription();
		prescription.setPatient((Patient) Fixture.from(Patient.class).gimme(PatientTemplate.MR_HORATIO));
		prescription.setProvider((Provider) Fixture.from(Provider.class).gimme(ProviderTemplate.TEST));
		prescription.setLocation((Location) Fixture.from(Location.class).gimme(LocationTemplate.XANADU));
		prescription.setPrescriptionDate(Calendar.getInstance().getTime());
		
		PrescriptionItem prescriptionItem = Fixture.from(PrescriptionItem.class).gimme(PrescriptionItemTemplate.VALID_01);
		prescription.setPrescriptionItems(Arrays.asList(prescriptionItem));
		
		Prescription createdPrescription = prescriptionService.createPrescription(prescription);
		
		Assert.assertNotNull(createdPrescription);
		
		Encounter createdEncounter = Context.getEncounterService().getEncounter(
		    prescription.getPrescriptionEncounter().getEncounterId());
		
		Assert.assertNotNull(createdEncounter);
		Assert.assertEquals(EncounterTypeTemplate.ARV_FOLLOW_UP_ADULT, createdEncounter.getEncounterType().getUuid());
		
		Set<Order> allOrders = createdEncounter.getOrders();
		
		Assert.assertEquals(1, allOrders.size());
		
		Order createdOrder = allOrders.iterator().next();
		
		Double durationInDays = 42d; // 7(durationUnits)x2(dose)x3(duration)
		
		Assert.assertEquals(Action.NEW, createdOrder.getAction());
		Assert.assertEquals(prescription.getPatient().getUuid(), createdOrder.getPatient().getUuid());
		Assert.assertEquals(durationInDays, ((DrugOrder) createdOrder).getQuantity());
		Assert.assertEquals(prescriptionItem.getDrugOrder().getDrug().getUuid(), ((DrugOrder) createdOrder).getDrug()
		        .getUuid());
	}
	
	@Test
	public void shouldCancelNotDispensedPrescriptionItem() throws Exception {
		
		PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
		
		DrugOrder drugOrder = new DrugOrder();
		drugOrder.setUuid("921de0a3-05c4-444a-be03-e01b4c4b9142");
		
		PrescriptionItem prescriptionItem = new PrescriptionItem(drugOrder);
		prescriptionService.cancelPrescriptionItem(prescriptionItem, "cancelation reason");
		
		Order vodedOrder = Context.getOrderService().getOrderByUuid(drugOrder.getUuid());
		
		Assert.assertNotNull(vodedOrder);
		Assert.assertEquals(true, vodedOrder.isVoided());
	}
	
	@Test
	public void shouldDiscontinueRevisedPrescriptionItem() throws Exception {
		executeDataSet("prescriptionservice/shouldCancelNotDispensedPrescriptionItem.xml");
		
		PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
		
		DrugOrder drugOrder = new DrugOrder();
		drugOrder.setUuid("e1f95924-697a-11e3-bd76-revised");
		
		PrescriptionItem prescriptionItem = new PrescriptionItem(drugOrder);
		prescriptionService.cancelPrescriptionItem(prescriptionItem, "Discontinuation reason");
		
		drugOrder = (DrugOrder) Context.getOrderService().getOrderByUuid(drugOrder.getUuid());
		
		Order discontinueOrder = Context.getOrderService().getDiscontinuationOrder(drugOrder);
		
		Assert.assertNotNull(discontinueOrder);
		Assert.assertEquals(Action.DISCONTINUE, discontinueOrder.getAction());
		Assert.assertEquals(drugOrder.getUuid(), discontinueOrder.getPreviousOrder().getUuid());
	}
	
}
