/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.OrderFrequency;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.adapter.ObsOrderAdapter;
import org.openmrs.module.pharmacyapi.api.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.DrugRegime;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.MappedOrders;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public class PrescriptionServiceImpl extends BaseOpenmrsService implements PrescriptionService {
	
	private ObsOrderAdapter obsOrderAdapter;
	
	private OrderService orderService;
	
	private ConceptService conceptService;
	
	private DispensationDAO dispensationDAO;
	
	private DbSessionManager dbSessionManager;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void parseObsToOrders(final Patient patient) throws APIException {
		
		final Encounter lastEncounter = this.getLastEncounter(patient);
		
		if (lastEncounter == null) {
			return;
		}
		
		final List<Order> orders = this.obsOrderAdapter.adaptPatientObsPrescriptionToOrders(patient, lastEncounter);
		
		this.saveOrders(orders, lastEncounter);
	}
	
	private void saveOrders(final List<Order> orders, final Encounter encounter) {
		
		for (final Order order : orders) {
			
			if (order instanceof DrugOrder) {
				final OrderType orderType = this.orderService.getOrderTypeByUuid(MappedOrders.DRUG_ORDER);
				order.setOrderType(orderType);
				
				final OrderFrequency orderFrequency = this.orderService.getOrderFrequencyByConcept(((DrugOrder) order)
				        .getFrequency().getConcept());
				((DrugOrder) order).setFrequency(orderFrequency);
				
				Drug drug = ((DrugOrder) order).getDrug();
				
				final List<Drug> drugs = Context.getConceptService().getDrugsByConcept(drug.getConcept());
				
				if (!drugs.isEmpty()) {
					drug = drugs.get(0);
				} else {
					drug = Context.getConceptService().saveDrug(drug);
				}
				
				((DrugOrder) order).setDrug(drug);
			}
			
			order.setOrderer(this.getProvider(encounter));
			final CareSetting careSetting = this.orderService.getCareSettingByUuid(MappedOrders.CARE_SETTING_OUTPATIENT);
			order.setCareSetting(careSetting);
			
			if (this.hasOrder(encounter.getPatient(), order, careSetting)) {
				return;
			}
			
			this.orderService.saveOrder(order, null);
		}
	}
	
	private boolean hasOrder(final Patient patient, final Order order, final CareSetting careSetting) {
		final List<Order> foundOrders = this.orderService.getOrders(patient, careSetting, null, false);
		
		for (final Order foundOrder : foundOrders) {
			if (order.getCommentToFulfiller().equals(foundOrder.getCommentToFulfiller())) {
				return true;
			}
		}
		
		return false;
	}
	
	private Provider getProvider(final Encounter encounter) {
		return encounter.getEncounterProviders().iterator().next().getProvider();
	}
	
	private Encounter getLastEncounter(final Patient patient) {
		
		final Concept conceptConvSet = Context.getConceptService().getConceptByUuid(MappedConcepts.TREATMENT_PRESCRIBED_SET);
		
		// Gets the lastest observation conv set of the patient with a
		// prescription
		final List<Obs> observations = Context.getObsService().getObservations(Arrays.asList((Person) patient), null,
		    Arrays.asList(conceptConvSet), null, null, null, null, 1, null, null, null, false);
		
		if (observations.isEmpty()) {
			return null;
		}
		
		final Obs obsGroup = observations.get(0);
		
		return Context.getEncounterService().getEncounterByUuid(obsGroup.getEncounter().getUuid());
	}
	
	public void setObsOrderAdapter(final ObsOrderAdapter obsOrderAdapter) throws APIException {
		this.obsOrderAdapter = obsOrderAdapter;
	}
	
	@Override
	public void setOrderService(final OrderService orderService) throws APIException {
		this.orderService = orderService;
	}
	
	@Override
	public void setConceptService(final ConceptService conceptService) {
		this.conceptService = conceptService;
	}
	
	@Override
	public void setDispensationDAO(final DispensationDAO dispensationDAO) {
		this.dispensationDAO = dispensationDAO;
	}
	
	@Override
	public void setDbSessionManager(final DbSessionManager dbSessionManager) {
		this.dbSessionManager = dbSessionManager;
	}
	
	@Override
	public List<Prescription> findPrescriptionsByPatient(final Patient patient) throws PharmacyBusinessException {

		final List<Prescription> prescriptions = new ArrayList<>();

		try {

			this.dbSessionManager.setManualFlushMode();

			final List<DrugOrder> drugOrders = this.dispensationDAO
					.findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(patient);

			for (DrugOrder drugOrder : drugOrders) {

				if (!Action.DISCONTINUE.equals(drugOrder.getAction())) {

					final Prescription prescription = new Prescription(drugOrder);

					this.setPrescriptionInstructions(drugOrder, prescription);
					prescription.setProvider(drugOrder.getOrderer().getName());
					prescription.setPrescriptionDate(drugOrder.getEncounter().getEncounterDatetime());
					prescription.setDrugToPickUp(drugOrder.getQuantity());

					if (this.isArvDrug(prescription, drugOrder)) {
						prescription.setDrugPickedUp(this.calculateDrugPikckedUp(drugOrder));
						prescription.setDrugToPickUp((drugOrder.getQuantity() - prescription.getDrugPickedUp()));
					}

					prescriptions.add(prescription);
				}
			}

		} finally {
			this.dbSessionManager.getCurrentFlushMode();
		}

		return prescriptions;
	}
	
	private boolean isArvDrug(final Prescription prescription, final DrugOrder drugOrder) throws PharmacyBusinessException {
		
		List<DrugRegime> result = Context.getService(DrugRegimeService.class).findDrugRegimeByDrugUuid(
		    drugOrder.getDrug().getUuid());
		if (!result.isEmpty()) {
			prescription.setDrugRegime(result.iterator().next());
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private void setPrescriptionInstructions(final DrugOrder drugOrder, final Prescription prescription) {
		final String dosingInstructions = drugOrder.getDosingInstructions();
		final Concept concept = this.conceptService.getConceptByUuid(dosingInstructions);
		prescription.setDosingInstructions(concept.getNames().iterator().next().getName());
	}
	
	@Override
	public Double calculateDrugPikckedUp(final DrugOrder order) throws APIException {

		Double quantity = 0.0;
		final List<Obs> observations = new ArrayList<>();

		DrugOrder tempOrder = order;
		while (tempOrder.getPreviousOrder() != null) {
			observations.addAll(tempOrder.getEncounter().getObs());
			tempOrder = (DrugOrder) tempOrder.getPreviousOrder();
		}

		for (final Obs observation : observations) {

			if (this.isTheSameConceptAndSameDrug(order, observation)) {
				quantity += observation.getValueNumeric();
			}
		}

		return quantity;
	}
	
	private boolean isTheSameConceptAndSameDrug(final DrugOrder order, Obs observation) {
		
		Drug obsDrug = this.dispensationDAO.findDrugByOrderUuid(observation.getOrder().getUuid());
		
		return MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())
		        && order.getDrug().getUuid().equals(obsDrug.getUuid());
	}
	
}
