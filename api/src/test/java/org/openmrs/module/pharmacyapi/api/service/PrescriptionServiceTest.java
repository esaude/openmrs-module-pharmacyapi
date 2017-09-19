package org.openmrs.module.pharmacyapi.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.openmrs.module.pharmacyapi.api.templates.ConceptTemplate;
import org.openmrs.module.pharmacyapi.api.templates.EncounterTypeTemplate;
import org.openmrs.module.pharmacyapi.api.templates.LocationTemplate;
import org.openmrs.module.pharmacyapi.api.templates.OrderTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PatientTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PrescriptionItemTemplate;
import org.openmrs.module.pharmacyapi.api.templates.ProviderTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;
import org.openmrs.module.pharmacyapi.api.util.EntityFactory;
import org.openmrs.test.Verifies;

import br.com.six2six.fixturefactory.Fixture;

public class PrescriptionServiceTest extends BaseTest {

	private PrescriptionService prescriptionService;

	/**
	 * @should create a Prescription for non ARV Drug
	 * @throws Exception
	 */
	@Test
	@Verifies(value = "should create prescription given valid prescriptionItem", method = "createPrescription(prescriptionItem)")
	public void createPrescription_shouldCreatePrescription() throws Exception {

		PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);

		Prescription prescription = new Prescription();
		prescription.setPatient((Patient) Fixture.from(Patient.class).gimme(PatientTemplate.MR_HORATIO));
		prescription.setProvider((Provider) Fixture.from(Provider.class).gimme(ProviderTemplate.TEST));
		prescription.setLocation((Location) Fixture.from(Location.class).gimme(LocationTemplate.XANADU));
		prescription.setPrescriptionDate(Calendar.getInstance().getTime());

		PrescriptionItem prescriptionItem = Fixture.from(PrescriptionItem.class)
				.gimme(PrescriptionItemTemplate.VALID_01);
		prescription.setPrescriptionItems(Arrays.asList(prescriptionItem));

		Prescription createdPrescription = prescriptionService.createPrescription(prescription);

		Assert.assertNotNull(createdPrescription);

		Encounter createdEncounter = Context.getEncounterService()
				.getEncounter(prescription.getPrescriptionEncounter().getEncounterId());

		Assert.assertNotNull(createdEncounter);
		Assert.assertEquals(EncounterTypeTemplate.ARV_FOLLOW_UP_ADULT, createdEncounter.getEncounterType().getUuid());

		Set<Order> allOrders = createdEncounter.getOrders();

		Assert.assertEquals(1, allOrders.size());

		Order createdOrder = allOrders.iterator().next();

		Double durationInDays = 42d; // 7(durationUnits)x2(dose)x3(duration)

		Assert.assertEquals(Action.NEW, createdOrder.getAction());
		Assert.assertEquals(prescription.getPatient().getUuid(), createdOrder.getPatient().getUuid());
		Assert.assertEquals(durationInDays, ((DrugOrder) createdOrder).getQuantity());
		Assert.assertEquals(prescriptionItem.getDrugOrder().getDrug().getUuid(),
				((DrugOrder) createdOrder).getDrug().getUuid());
	}

	@Test
	@Ignore
	public void xxx() throws Exception {

		final Patient patient = EntityFactory.gimme(Patient.class, PatientTemplate.VALID);
		final List<DrugOrder> orders = EntityFactory.gimme(DrugOrder.class, 5, OrderTemplate.DRUG_ORDER);

		final OrderService orderService = Mockito.mock(OrderService.class);
		Mockito.when(orderService.getActiveOrders(patient, null, null, null)).thenReturn(new ArrayList<Order>(orders));

		final ConceptService conceptService = Mockito.mock(ConceptService.class);
		Mockito.when(conceptService.getConceptByUuid("9d7408af-10e8-11e5-9009-0242ac110012"))
				.thenReturn(EntityFactory.gimme(Concept.class, ConceptTemplate.BEFORE_MEALS));
		this.prescriptionService.setConceptService(conceptService);

		final List<Prescription> prescriptions = this.prescriptionService
				.findPrescriptionsByPatientAndActiveStatus(patient);

		Assert.assertFalse(prescriptions.isEmpty());
		Assert.assertEquals(5, prescriptions.size());

		// for (final Prescription prescription : prescriptions) {
		// Assert.assertEquals("Antes das refeições",
		// prescription.getDosingInstructions());
		// }
	}

	@Test
	@Ignore
	public void shouldCalculateDrugPickedUpAmountByOrder() {

		// final DrugOrder order = EntityFactory. gimme(DrugOrder.class,
		// OrderTemplate.REVISED, new OrderProcessor());

		// final Double amount =
		// this.prescriptionService.calculateDrugPikckedUp(order);

		// Assert.assertEquals(20.0, amount, 0);
	}
}
