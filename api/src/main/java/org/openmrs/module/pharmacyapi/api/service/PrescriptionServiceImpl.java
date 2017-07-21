/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.OrderFrequency;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.MappedDurationUnits;
import org.openmrs.module.pharmacyapi.api.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public class PrescriptionServiceImpl extends BaseOpenmrsService implements PrescriptionService {
	
	private ConceptService conceptService;
	
	private DispensationDAO dispensationDAO;
	
	private DbSessionManager dbSessionManager;
	
	private PrescriptionDispensationService prescriptionDispensationService;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
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
	public void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService) {
		this.prescriptionDispensationService = prescriptionDispensationService;
	}
	
	@Override
	public List<Prescription> findPrescriptionsByPatientAndActiveStatus(final Patient patient)
			throws PharmacyBusinessException {

		final List<Prescription> patientPrescriptions = new ArrayList<>();

		try {
			this.dbSessionManager.setManualFlushMode();

			final List<DrugOrder> drugOrders = this.dispensationDAO
					.findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(patient);

			Map<Encounter, List<DrugOrder>> mapByEncounter = groupDrugOrdersByEncounter(drugOrders);

			try {
				Encounter firstPrescription = getFirstPrescription(drugOrders);

				for (Encounter encounter : mapByEncounter.keySet()) {

					List<DrugOrder> drugOrderItems = mapByEncounter.get(encounter);

					final Prescription prescription = new Prescription();
					prescription.setProvider(drugOrderItems.iterator().next().getOrderer());
					prescription.setPrescriptionEncounter(firstPrescription);
					prescription.setPatient(patient);
					prescription.setLocation(firstPrescription.getLocation());
					setPrescriptionDate(prescription, firstPrescription);
					prescription.setPrescriptionStatus(Boolean.TRUE);

					List<PrescriptionItem> prescriptionItems = new ArrayList<>();

					for (DrugOrder drugOrder : drugOrderItems) {

						PrescriptionItem prescriptionItem = new PrescriptionItem(drugOrder);

						this.setPrescriptionInstructions(prescriptionItem, drugOrder);

						prescriptionItem.setDrugToPickUp(drugOrder.getQuantity());

						if (this.prescriptionDispensationService.isArvDrug(prescriptionItem, drugOrder)) {
							prescriptionItem.setDrugPickedUp(this.calculateDrugPikckedUp(drugOrder));
							prescriptionItem
									.setDrugToPickUp((drugOrder.getQuantity() - prescriptionItem.getDrugPickedUp()));
							prescriptionItem.setArvPlan(findArvPlan(firstPrescription));
						}

						prescriptionItems.add(prescriptionItem);
					}
					prescription.setPrescriptionItems(prescriptionItems);
					patientPrescriptions.add(prescription);
				}

			} catch (APIException e) {
				// the is no first Prescriptions, these drugs haven't be
				// prescribed yet
			}

		} finally {
			this.dbSessionManager.getCurrentFlushMode();
		}

		return patientPrescriptions;
	}
	
	public Map<Encounter, List<DrugOrder>> groupDrugOrdersByEncounter(List<DrugOrder> drugOrders) {

		Map<Encounter, List<DrugOrder>> mapByEncounter = new HashMap<>();

		for (DrugOrder drugOrder : drugOrders) {

			if (!Action.DISCONTINUE.equals(drugOrder.getAction())) {

				List<DrugOrder> list = mapByEncounter.get(drugOrder.getEncounter());

				if (list == null) {
					mapByEncounter.put(drugOrder.getEncounter(), list = new ArrayList<>());
				}
				list.add(drugOrder);
			}
		}

		return mapByEncounter;
	}
	
	private void setPrescriptionInstructions(PrescriptionItem prescriptionItem, final DrugOrder drugOrder) {
		
		final Concept concept = this.conceptService.getConceptByUuid(drugOrder.getDosingInstructions());
		
		prescriptionItem.setDosingInstructions(concept.getNames().iterator().next().getName());
	}
	
	private void setPrescriptionDate(Prescription prescription, Encounter encounter) {
		Set<Obs> allObs = encounter.getAllObs();
		
		Concept precriptionDateConcept = this.conceptService.getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE);
		
		for (Obs obs : allObs) {
			
			if (obs.getConcept().equals(precriptionDateConcept)) {
				prescription.setPrescriptionDate(obs.getValueDatetime());
				return;
			}
		}
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
	
	@Override
	public Prescription createPrescription(Prescription prescription) throws PharmacyBusinessException {
		
		validatePrescription(prescription);
		
		Patient patient = Context.getPatientService().getPatientByUuid(prescription.getPatient().getUuid());
		
		Provider provider = Context.getProviderService().getProviderByUuid(prescription.getProvider().getUuid());
		
		prescription.setPatient(patient);
		prescription.setProvider(provider);
		
		Encounter encounter = this.createEncounter(prescription);
		this.createObservations(prescription, encounter);
		this.createOrders(prescription, encounter);
		
		Context.getEncounterService().saveEncounter(encounter);
		prescription.setPrescriptionEncounter(encounter);
		
		return prescription;
	}
	
	private void createObservations(Prescription prescription, Encounter encounter) throws PharmacyBusinessException {
		
		final Obs obsPrescriptionDate = new Obs();
		obsPrescriptionDate.setConcept(this.conceptService.getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE));
		obsPrescriptionDate.setValueDatetime(prescription.getPrescriptionDate());
		encounter.addObs(obsPrescriptionDate);
		
		if (prescription.getRegime() != null) {
			
			final Obs obsRegime = new Obs();
			obsRegime.setConcept(this.conceptService.getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS));
			obsRegime.setValueCoded(getArvRegime(prescription));
			encounter.addObs(obsRegime);
			
			final Obs obsPlan = new Obs();
			obsPlan.setConcept(this.conceptService.getConceptByUuid(MappedConcepts.ARV_PLAN));
			obsPlan.setValueCoded(this.getArvPlan(prescription));
			encounter.addObs(obsPlan);
			
			if (prescription.getInterruptionReason() != null) {
				
				final Obs obsInterruptionReason = new Obs();
				obsInterruptionReason.setConcept(this.conceptService
				        .getConceptByUuid(MappedConcepts.REASON_ANTIRETROVIRALS_STOPPED));
				obsInterruptionReason.setValueCoded(this.conceptService.getConceptByUuid(prescription
				        .getInterruptionReason().getUuid()));
				encounter.addObs(obsInterruptionReason);
				
			} else if (prescription.getChangeReason() != null) {
				
				final Obs obsChangeReason = new Obs();
				obsChangeReason.setConcept(this.conceptService
				        .getConceptByUuid(MappedConcepts.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT));
				obsChangeReason
				        .setValueCoded(this.conceptService.getConceptByUuid(prescription.getChangeReason().getUuid()));
				encounter.addObs(obsChangeReason);
			}
			
		} else {
			
			for (PrescriptionItem item : prescription.getPrescriptionItems()) {
				
				Concept conceptDrug = this.conceptService.getDrugByUuid(item.getDrugOrder().getDrug().getUuid())
				        .getConcept();
				
				final Obs obsRegime = new Obs();
				obsRegime.setConcept(this.conceptService.getConceptByUuid(MappedConcepts.TREATMENT_PRESCRIBED));
				obsRegime.setValueCoded(conceptDrug);
				encounter.addObs(obsRegime);
			}
		}
	}
	
	private Concept findArvPlan(Encounter encounter) {
		
		Set<Obs> allObs = encounter.getAllObs();
		
		Concept arvPlan = this.conceptService.getConceptByUuid(MappedConcepts.ARV_PLAN);
		
		for (Obs obs : allObs) {
			if (arvPlan.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		return null;
	}
	
	private void createOrders(Prescription prescription, Encounter encounter) {
		
		for (PrescriptionItem item : prescription.getPrescriptionItems()) {
			
			DrugOrder itemDrugOrder = item.getDrugOrder();
			
			Drug drug = this.conceptService.getDrugByUuid(itemDrugOrder.getDrug().getUuid());
			Concept doseUnits = this.conceptService.getConceptByUuid(itemDrugOrder.getDoseUnits().getUuid());
			OrderFrequency frequency = Context.getOrderService().getOrderFrequencyByUuid(
			    itemDrugOrder.getFrequency().getUuid());
			Concept quantityUnits = this.conceptService.getConceptByUuid(itemDrugOrder.getQuantityUnits().getUuid());
			Concept durationUnits = this.conceptService.getConceptByUuid(itemDrugOrder.getDurationUnits().getUuid());
			Concept route = this.conceptService.getConceptByUuid(itemDrugOrder.getRoute().getUuid());
			Concept concept = drug.getConcept();
			CareSetting careSetting = Context.getOrderService().getCareSettingByUuid(
			    itemDrugOrder.getCareSetting().getUuid());
			
			itemDrugOrder.setDrug(drug);
			itemDrugOrder.setDoseUnits(doseUnits);
			itemDrugOrder.setFrequency(frequency);
			itemDrugOrder.setQuantityUnits(quantityUnits);
			itemDrugOrder.setDurationUnits(durationUnits);
			itemDrugOrder.setRoute(route);
			itemDrugOrder.setConcept(concept);
			itemDrugOrder.setPatient(prescription.getPatient());
			itemDrugOrder.setOrderer(prescription.getProvider());
			itemDrugOrder.setCareSetting(careSetting);
			itemDrugOrder.setEncounter(encounter);
			itemDrugOrder.setQuantity(this.calculateDrugQuantity(itemDrugOrder));
			itemDrugOrder.setNumRefills(0);
			
			encounter.addOrder(itemDrugOrder);
		}
	}
	
	private Encounter createEncounter(Prescription prescription) {
		
		EncounterType encounterType = getEncounterTypeByPatientAge(prescription.getPatient());
		final EncounterRole encounterRole = Context.getEncounterService().getEncounterRoleByUuid(
		    MappedEncounters.DEFAULT_ENCONTER_ROLE);
		final Location location = Context.getLocationService().getLocationByUuid(prescription.getLocation().getUuid());
		
		Encounter encounter = new Encounter();
		encounter.setEncounterType(encounterType);
		encounter.setPatient(prescription.getPatient());
		encounter.addProvider(encounterRole, prescription.getProvider());
		encounter.setLocation(location);
		encounter.setEncounterDatetime(prescription.getPrescriptionDate());
		
		return encounter;
	}
	
	private Concept getArvRegime(Prescription prescription) throws PharmacyBusinessException {
		
		Concept regime = null;
		
		if (prescription.getRegime() != null) {
			
			regime = this.conceptService.getConceptByUuid(prescription.getRegime().getUuid());
			
			if (regime != null) {
				
				return regime;
			}
		}
		
		throw new PharmacyBusinessException(" Prescription of ARV must have an ARV Regime " + prescription);
	}
	
	private Concept getArvPlan(Prescription prescription) throws PharmacyBusinessException {
		
		Concept arvPlan = null;
		
		if (prescription.getArvPlan() != null) {
			
			arvPlan = this.conceptService.getConceptByUuid(prescription.getArvPlan().getUuid());
			
			if (arvPlan != null) {
				return arvPlan;
			}
		}
		
		throw new PharmacyBusinessException("Prescription wWith ARV Drug without ARV Plan  cannot be created "
		        + prescription);
	}
	
	private Encounter getFirstPrescription(List<DrugOrder> drugOrders) {
		
		for (DrugOrder drugOrder : drugOrders) {
			
			try {
				
				return this.prescriptionDispensationService.findPrescriptionDispensationByDispensation(
				    drugOrder.getEncounter()).getPrescription();
				
			}
			catch (PharmacyBusinessException e) {
				
			}
		}
		
		for (DrugOrder drugOrder : drugOrders) {
			
			if (MappedEncounters.ARV_FOLLOW_UP_ADULT.equals(drugOrder.getEncounter().getEncounterType().getUuid())
			        || MappedEncounters.ARV_FOLLOW_UP_CHILD.equals(drugOrder.getEncounter().getEncounterType().getUuid())) {
				
				return drugOrder.getEncounter();
			}
		}
		
		throw new APIException("Encounter of Prescription not found");
	}
	
	private void validatePrescription(Prescription prescription) throws PharmacyBusinessException {

		if (prescription == null) {
			throw new PharmacyBusinessException(" Invalid prescriprion argument");
		}

		if (prescription.getPrescriptionItems() == null || prescription.getPrescriptionItems().isEmpty()) {
			throw new PharmacyBusinessException("Prescription without Items to prescribe");
		}

		String patientUUid = (prescription.getPatient() != null) ? prescription.getPatient().getUuid() : "";

		Patient patient = Context.getPatientService().getPatientByUuid(patientUUid);

		if (patient == null) {

			throw new PharmacyBusinessException("Prescription without patient " + prescription);
		}

		List<Prescription> existingsPrescriptions = this.findPrescriptionsByPatientAndActiveStatus(patient);

		if (!existingsPrescriptions.isEmpty()) {

			throw new PharmacyBusinessException(
					"Cannot create a new Prescription for Patient " + getFormattedPatientToDisplay(patient)
							+ " while exist Active prescriptions " + StringUtils.join(existingsPrescriptions, "|"));
		}

		if (prescription.getPrescriptionDate() == null) {
			throw new PharmacyBusinessException(
					"Cannot create a Prescription without Prescription Date " + prescription);
		}

		Set<String> regimesUuid = new TreeSet<>();

		for (PrescriptionItem item : prescription.getPrescriptionItems()) {

			if (item.getRegime() != null && StringUtils.isNotBlank(item.getRegime().getUuid())) {
				regimesUuid.add(item.getRegime().getUuid());
			}
		}
		if (regimesUuid.size() > 1) {

			throw new PharmacyBusinessException("Cannot Create a Prescription for Arv Drugs having different regimes "
					+ StringUtils.join(prescription.getPrescriptionItems(), "|"));
		}
	}
	
	public Double calculateDrugQuantity(final DrugOrder drugOrder) {
		
		final int durationUnitsDays = MappedDurationUnits.getDurationDays(drugOrder.getDurationUnits().getUuid());
		return drugOrder.getDose() * drugOrder.getDuration() * durationUnitsDays;
	}
	
	@Override
	public Prescription findLastActivePrescriptionByPatient(Patient patient) throws PharmacyBusinessException {

		final List<DrugOrder> drugOrders = this.dispensationDAO
				.findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(patient);

		if (drugOrders.isEmpty()) {

			throw new PharmacyBusinessException(
					"There is no prescription for the patient " + getFormattedPatientToDisplay(patient));
		}

		try {

			this.dbSessionManager.setManualFlushMode();

			Encounter firstPrescription = getFirstPrescription(drugOrders);

			DrugOrder lastDrugOrder = drugOrders.iterator().next();

			final Prescription prescription = new Prescription();
			prescription.setProvider(lastDrugOrder.getOrderer());
			prescription.setPrescriptionEncounter(firstPrescription);
			prescription.setPatient(firstPrescription.getPatient());
			prescription.setLocation(firstPrescription.getLocation());
			prescription.setPrescriptionStatus(getPrescriptionStatus(drugOrders));
			setPrescriptionDate(prescription, firstPrescription);

			List<PrescriptionItem> prescriptionItems = new ArrayList<>();

			for (DrugOrder drugOrder : drugOrders) {

				DrugOrder previousDrugOrder = null;
				if (Action.DISCONTINUE.equals(drugOrder.getAction())) {

					previousDrugOrder = (DrugOrder) drugOrder.getPreviousOrder();
				}

				DrugOrder drugOrderToUse = (previousDrugOrder != null) ? previousDrugOrder : drugOrder;
				drugOrder.setFrequency(drugOrderToUse.getFrequency());
				drugOrder.setDoseUnits(drugOrderToUse.getDoseUnits());
				drugOrder.setDurationUnits(drugOrderToUse.getDurationUnits());
				drugOrder.setRoute(drugOrderToUse.getRoute());

				PrescriptionItem prescriptionItem = new PrescriptionItem(drugOrder);
				this.setPrescriptionInstructions(prescriptionItem, drugOrderToUse);
				prescriptionItem.setDrugToPickUp(drugOrderToUse.getQuantity());
				prescriptionItem.setDrugPickedUp(this.calculateDrugPikckedUp(drugOrder));
				prescriptionItem.setDrugToPickUp((drugOrderToUse.getQuantity() - prescriptionItem.getDrugPickedUp()));

				if (this.prescriptionDispensationService.isArvDrug(prescriptionItem, drugOrderToUse)) {

					prescriptionItem.setArvPlan(findArvPlan(firstPrescription));
				}

				prescriptionItems.add(prescriptionItem);
			}
			prescription.setPrescriptionItems(prescriptionItems);

			return prescription;

		} finally {

		}
	}
	
	@Override
	public void cancelPrescription(Prescription prescription, String cancelationReason) throws PharmacyBusinessException {
		
		for (PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {
			this.cancelPrescriptionItem(prescriptionItem, cancelationReason);
		}
	}
	
	@Override
	public void cancelPrescriptionItem(PrescriptionItem prescriptionItem, String cancelationReason)
	        throws PharmacyBusinessException {
		
		Order order = Context.getOrderService().getOrderByUuid(prescriptionItem.getDrugOrder().getUuid());
		
		if (Action.NEW.equals(order.getAction())) {
			
			Context.getOrderService().voidOrder(order, cancelationReason);
			
		} else if (Action.REVISE.equals(order.getAction())) {
			
			Concept discountinueReason = Context.getConceptService().getConceptByUuid(cancelationReason);
			
			try {
				
				Context.getOrderService().discontinueOrder(order, discountinueReason, new Date(), order.getOrderer(),
				    order.getEncounter());
				
			}
			catch (Exception e) {
				
				throw new APIException(e);
			}
		}
	}
	
	@Override
	public List<Prescription> findAllPrescriptionsByPatient(Patient patient) {

		List<Prescription> allPrescription = getAllPrescriptions(patient);

		try {

			Prescription lastPrescription = findLastActivePrescriptionByPatient(patient);
			List<Prescription> result = new ArrayList<>();

			for (Prescription prescription : allPrescription) {

				if (lastPrescription.getPrescriptionEncounter().equals(prescription.getPrescriptionEncounter())) {

					List<PrescriptionItem> items = new ArrayList<>();

					for (PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {

						for (PrescriptionItem lastPrescriptionItem : lastPrescription.getPrescriptionItems()) {

							if (!prescriptionItem.getDrugOrder().isVoided() && prescriptionItem.getDrugOrder().getDrug()
									.equals(lastPrescriptionItem.getDrugOrder().getDrug())) {
								items.add(lastPrescriptionItem);
								break;
							}
						}
					}

					prescription.setPrescriptionItems(items);
					prescription.setPrescriptionStatus(lastPrescription.isPrescriptionStatus());

				}
				if (!prescription.getPrescriptionItems().isEmpty()) {

					if (hasAtLeastOneActiveOrder(prescription)) {

						result.add(0, prescription);

					} else {
						result.add(prescription);
					}
				}
			}

			return result;

		} catch (PharmacyBusinessException e) {

			return allPrescription;
		}
	}
	
	private boolean hasAtLeastOneActiveOrder(Prescription prescription) {
		
		for (PrescriptionItem item : prescription.getPrescriptionItems()) {
			
			if (Action.NEW.equals(item.getDrugOrder().getAction()) || Action.REVISE.equals(item.getDrugOrder().getAction())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	private List<Prescription> getAllPrescriptions(Patient patient) {

		EncounterType encounterType = getEncounterTypeByPatientAge(patient);

		List<Encounter> encounters = this.dispensationDAO.findEncountersByPatientAndEnconterType(patient,
				encounterType);

		List<Prescription> prescriptions = new ArrayList<>();

		for (Encounter encounter : encounters) {

			Set<Order> orders = encounter.getOrders();

			if (!orders.isEmpty()) {

				Order anyOrder = orders.iterator().next();
				final Prescription prescription = new Prescription();
				prescription.setProvider(anyOrder.getOrderer());
				prescription.setPrescriptionEncounter(encounter);
				prescription.setPatient(patient);
				prescription.setLocation(encounter.getLocation());
				setPrescriptionDate(prescription, encounter);

				List<PrescriptionItem> prescriptionItems = new ArrayList<>();

				for (Order drugOrder : orders) {

					PrescriptionItem prescriptionItem = new PrescriptionItem((DrugOrder) drugOrder);

					try {
						if (this.prescriptionDispensationService.isArvDrug(prescriptionItem, (DrugOrder) drugOrder)) {
							prescriptionItem.setArvPlan(findArvPlan(encounter));
						}
					} catch (PharmacyBusinessException e) {
					}

					this.setPrescriptionInstructions(prescriptionItem, (DrugOrder) drugOrder);
					prescriptionItem.setDrugToPickUp(((DrugOrder) drugOrder).getQuantity());
					prescriptionItems.add(prescriptionItem);
				}
				prescription.setPrescriptionItems(prescriptionItems);
				prescriptions.add(prescription);
			}
		}
		return prescriptions;
	}
	
	@Override
	public EncounterType getEncounterTypeByPatientAge(Patient patient) {
		
		if (patient != null) {
			
			EncounterType encounterType = Context.getEncounterService().getEncounterTypeByUuid(
			    patient.getAge() < 15 ? MappedEncounters.ARV_FOLLOW_UP_CHILD : MappedEncounters.ARV_FOLLOW_UP_ADULT);
			return encounterType;
		}
		
		throw new APIException("Cannot find encounterType for non given patient");
	}
	
	private Boolean getPrescriptionStatus(List<DrugOrder> drugOrders) {
		
		for (DrugOrder drugOrder : drugOrders) {
			
			if (!Action.DISCONTINUE.equals(drugOrder.getAction())) {
				
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	private String getFormattedPatientToDisplay(Patient patient) {
		return ((patient.getPatientIdentifier() != null) ? patient.getPatientIdentifier().getIdentifier() + " - "
		        + patient.getPersonName().getFullName() : patient.getGivenName() + " " + patient.getFamilyName());
	}
	
}
