/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.dispensation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.validation.DispensationValidator;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionUtils;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public class DispensationServiceImpl extends BaseOpenmrsService implements DispensationService {
	
	private ProviderService providerService;
	
	private OrderService orderService;
	
	private PatientService patientService;
	
	private EncounterService encounterService;
	
	private LocationService locationService;
	
	private ConceptService conceptService;
	
	private PersonService personService;
	
	private DbSessionManager dbSessionManager;
	
	private PrescriptionDispensationService prescriptionDispensationService;
	
	@Autowired
	private DispensationValidator dispensationValidator;
	
	@Autowired
	private PrescriptionUtils prescriptionUtils;
	
	private DispensationDAO dispensationDAO;
	
	@Override
	public Dispensation dispense(final Dispensation dispensation) throws PharmacyBusinessException {

		try {

			// workaround to controll the hibernate sessions commits
			this.dbSessionManager.setManualFlushMode();

			dispensationValidator.validateCreation(dispensation);

			Person person = Context.getPersonService().getPersonByUuid(dispensation.getProviderUuid());
			Provider provider = Context.getProviderService().getProvidersByPerson(person).iterator().next();
			final Patient patient = this.patientService.getPatientByUuid(dispensation.getPatientUuid());

			final EncounterRole encounterRole = this.encounterService
					.getEncounterRoleByUuid(MappedEncounters.DEFAULT_ENCONTER_ROLE);
			final EncounterType encounterType = this.encounterService
					.getEncounterTypeByUuid(MappedEncounters.DISPENSATION_ENCOUNTER_TYPE);

			final Location location = this.locationService.getLocationByUuid(dispensation.getLocationUuid());

			List<DispensationItem> arvDispensationItems = new ArrayList<>();

			Concept arvConceptQuestion = Context.getConceptService()
					.getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);

			final Concept dispensationConceptSet = this.conceptService
					.getConceptByUuid(MappedConcepts.DISPENSATION_SET);
			final Concept quantityConcept = this.conceptService.getConceptByUuid(MappedConcepts.MEDICATION_QUANTITY);
			final Concept nextPickUpConcept = this.conceptService.getConceptByUuid(MappedConcepts.DATE_OF_NEXT_PICK_UP);

			Map<Encounter, List<DispensationItem>> mapDispensationItemByPrescription = groupDispensationItemsByPrescription(
					dispensation);

			for (Encounter prescription : mapDispensationItemByPrescription.keySet()) {

				final Encounter dispensationEncounter = this.createEncounter(provider, patient, encounterRole,
						encounterType, location);

				List<DispensationItem> dispensationItems = mapDispensationItemByPrescription.get(prescription);

				for (DispensationItem dispensationItem : dispensationItems) {

					final Order order = this.orderService.getOrderByUuid(dispensationItem.getOrderUuid());
					Order orderProcess = order.cloneForRevision();

					if (dispensationItem.getTotalDispensed().equals(((DrugOrder) orderProcess).getQuantity())) {
						orderProcess = order.cloneForDiscontinuing();
						((DrugOrder) (orderProcess)).setDispenseAsWritten(Boolean.TRUE);
					}

					if (StringUtils.isNotEmpty(dispensationItem.getRegimeUuid())) {
						arvDispensationItems.add(dispensationItem);
					}

					orderProcess.setOrderer(provider);
					this.prepareDispensation(orderProcess, dispensationEncounter, dispensationConceptSet,
							quantityConcept, nextPickUpConcept, dispensationItem, arvConceptQuestion);
				}

				this.encounterService.saveEncounter(dispensationEncounter);

				this.prescriptionDispensationService.savePrescriptionDispensation(patient, prescription,
						dispensationEncounter);

				if (!arvDispensationItems.isEmpty()) {

					final EncounterType filaEncounterType = this.encounterService
							.getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);

					final Encounter filaEncounter = this.createEncounter(provider, patient, encounterRole,
							filaEncounterType, location);

					this.processFila(filaEncounter, dispensationEncounter, arvDispensationItems, arvConceptQuestion,
							quantityConcept, nextPickUpConcept, prescription);
				}
			}

		} finally {
			// this.dbSessionManager.setAutoFlushMode();
			Context.flushSession();
		}

		return dispensation;
	}
	
	private Encounter createEncounter(final Provider provider, final Patient patient, final EncounterRole encounterRole,
	        final EncounterType encounterType, final Location location) {
		
		final Encounter encounter = new Encounter();
		encounter.setPatient(patient);
		encounter.addProvider(encounterRole, provider);
		encounter.setEncounterType(encounterType);
		encounter.setLocation(location);
		encounter.setEncounterDatetime(new Date());
		
		return encounter;
	}
	
	private void prepareDispensation(final Order order, final Encounter encounter, final Concept dispensationConceptSet,
	        final Concept quantityConcept, final Concept nextPickUpConcept, final DispensationItem dispensationItem,
	        Concept arvConceptQuestion) {
		
		final Obs obsGroup = new Obs();
		obsGroup.setConcept(dispensationConceptSet);
		obsGroup.setOrder(order);
		
		final Obs obsQuantity = new Obs();
		obsQuantity.setConcept(quantityConcept);
		obsQuantity.setValueNumeric(dispensationItem.getQuantityToDispense());
		obsQuantity.setOrder(order);
		
		final Obs obsNextPickUp = new Obs();
		obsNextPickUp.setConcept(nextPickUpConcept);
		obsNextPickUp.setValueDatetime(dispensationItem.getDateOfNextPickUp());
		obsNextPickUp.setOrder(order);
		
		obsGroup.addGroupMember(obsQuantity);
		obsGroup.addGroupMember(obsNextPickUp);
		
		if (StringUtils.isNotEmpty(dispensationItem.getRegimeUuid())) {
			Concept valueCoded = Context.getConceptService().getConceptByUuid(dispensationItem.getRegimeUuid());
			final Obs obsRegime = new Obs();
			obsRegime.setConcept(arvConceptQuestion);
			obsRegime.setValueCoded(valueCoded);
			obsRegime.setOrder(order);
			encounter.addObs(obsRegime);
		}
		
		encounter.addObs(obsGroup);
		encounter.addOrder(order);
	}
	
	private void processFila(Encounter encounter, Encounter dispensationEncounter,
	        List<DispensationItem> arvDispensationItems, Concept arvConceptQuestion, final Concept quantityConcept,
	        final Concept nextPickUpConcept, Encounter prescriptionEncounter) throws PharmacyBusinessException {
		
		DispensationItem dispensationItem = arvDispensationItems.iterator().next();
		final Order arvOrder = this.orderService.getOrderByUuid(dispensationItem.getOrderUuid());
		
		final Concept posologyConcept = this.conceptService.getConceptByUuid(MappedConcepts.POSOLOGY);
		final Concept regimenConcept = this.conceptService.getConceptByUuid(MappedConcepts.REGIMEN);
		Concept regime = this.conceptService.getConceptByUuid(dispensationItem.getRegimeUuid());
		
		final Obs obsQuantity = new Obs();
		obsQuantity.setConcept(quantityConcept);
		obsQuantity.setValueNumeric(calculateArvDispensedQuantity(arvDispensationItems));
		
		final Obs obsNextPickUp = new Obs();
		obsNextPickUp.setConcept(nextPickUpConcept);
		obsNextPickUp.setValueDatetime(dispensationItem.getDateOfNextPickUp());
		
		final Obs posologyObs = new Obs();
		posologyObs.setConcept(posologyConcept);
		
		final StringBuilder posologyBuilder = new StringBuilder();
		posologyBuilder.append(((DrugOrder) arvOrder).getDose());
		posologyBuilder.append(" ");
		posologyBuilder.append(((DrugOrder) arvOrder).getDoseUnits().getName());
		posologyBuilder.append(" ");
		posologyBuilder.append(((DrugOrder) arvOrder).getFrequency().getName());
		
		posologyObs.setValueText(posologyBuilder.toString());
		
		final Obs regimenObs = new Obs();
		regimenObs.setConcept(regimenConcept);
		regimenObs.setValueCoded(regime);
		
		encounter.addObs(obsQuantity);
		encounter.addObs(obsNextPickUp);
		encounter.addObs(posologyObs);
		encounter.addObs(regimenObs);
		
		for (DispensationItem dispItem : arvDispensationItems) {
			
			final DrugOrder order = (DrugOrder) this.orderService.getOrderByUuid(dispItem.getOrderUuid());
			
			final Obs obsDrugOrder = new Obs();
			obsDrugOrder.setConcept(arvConceptQuestion);
			obsDrugOrder.setValueCoded(order.getDrug().getConcept());
			obsDrugOrder.setValueNumeric(dispItem.getQuantityToDispense());
			obsDrugOrder.setValueDatetime(dispItem.getDateOfNextPickUp());
			
			DrugOrder dispensedDrugOrder = getDispensedDrugOrder(dispensationEncounter, order.getDrug());
			obsDrugOrder.setOrder(dispensedDrugOrder);
			obsDrugOrder.setValueDrug(dispensedDrugOrder.getDrug());
			
			encounter.addObs(obsDrugOrder);
		}
		
		encounter = this.encounterService.saveEncounter(encounter);
		PrescriptionDispensation prescriptionDispensation = this.prescriptionDispensationService
		        .findPrescriptionDispensationByDispensation(dispensationEncounter);
		prescriptionDispensation.setFila(encounter);
		this.prescriptionDispensationService.updatePrescriptionDispensation(prescriptionDispensation);
	}
	
	private DrugOrder getDispensedDrugOrder(Encounter dispensationEncounter, Drug drug) {
		
		for (Order order : dispensationEncounter.getOrders()) {
			
			DrugOrder drugOrder = (DrugOrder) order;
			
			if (drugOrder.getDrug().equals(drug)) {
				
				return drugOrder;
			}
			
		}
		return null;
	}
	
	private Double calculateArvDispensedQuantity(List<DispensationItem> arvDispensationItems) {
		
		Double totalQuantityToDispense = 0.0;
		for (DispensationItem dispensationItem : arvDispensationItems) {
			totalQuantityToDispense += dispensationItem.getQuantityToDispense();
		}
		return totalQuantityToDispense;
	}
	
	@Override
	public void setProviderService(final ProviderService providerService) {
		this.providerService = providerService;
	}
	
	@Override
	public void setOrderService(final OrderService orderService) {
		this.orderService = orderService;
	}
	
	@Override
	public void setPatientService(final PatientService patientService) {
		this.patientService = patientService;
	}
	
	@Override
	public void setEncounterService(final EncounterService encounterService) {
		this.encounterService = encounterService;
	}
	
	@Override
	public void setLocationService(final LocationService locationService) {
		this.locationService = locationService;
	}
	
	@Override
	public void setConceptService(final ConceptService conceptService) {
		this.conceptService = conceptService;
	}
	
	@Override
	public void setPersonService(final PersonService personService) {
		this.personService = personService;
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
	public void cancelDispensationItems(Dispensation dispensation, String cancelationReason)
	        throws PharmacyBusinessException {
		
		dispensationValidator.validateCancellation(dispensation);
		
		DispensationItem dispensationItem = dispensation.getDispensationItems().iterator().next();
		DrugOrder drugOrder = (DrugOrder) Context.getOrderService().getOrderByUuid(dispensationItem.getOrderUuid());
		
		if (StringUtils.isNotBlank(dispensationItem.getRegimeUuid())) {
			
			if (this.prescriptionDispensationService.isArvDrug(new PrescriptionItem(), drugOrder)) {
				removeDrugOrderObsFromFilaEncounter(dispensation, dispensationItem, drugOrder);
			}
		}
		Context.getOrderService().voidOrder(drugOrder, cancelationReason);
		
		List<Obs> lstObs = this.prescriptionDispensationService.findObsByOrder(drugOrder);
		
		for (Obs obs : lstObs) {
			
			Context.getObsService()
			        .voidObs(obs, "Cancellation of Dispensation Item " + drugOrder.getDrug().getDisplayName());
		}
	}
	
	private void removeDrugOrderObsFromFilaEncounter(Dispensation dispensation, DispensationItem dispensationItem,
	        DrugOrder drugOrder) throws PharmacyBusinessException {
		
		Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
		
		final EncounterType filaEncounterType = this.encounterService
		        .getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);
		Encounter filaEncounter = this.prescriptionDispensationService.findEncounterByPatientAndEncounterTypeAndOrder(
		    patient, filaEncounterType, drugOrder);
		
		Concept arvConceptQuestion = Context.getConceptService().getConceptByUuid(
		    MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		final Concept quantityConcept = this.conceptService.getConceptByUuid(MappedConcepts.MEDICATION_QUANTITY);
		Set<Obs> filaObs = filaEncounter.getAllObs();
		
		Double valueToCancel = 0.0;
		Obs obsQuantity = null;
		
		for (Obs obs : filaObs) {
			if (obs.getConcept().equals(arvConceptQuestion)
			        && obs.getOrder().getOrderId().intValue() == drugOrder.getOrderId().intValue()) {
				
				valueToCancel += obs.getValueNumeric();
				Context.getObsService().voidObs(obs,
				    "Cancellation of Dispensation Item " + drugOrder.getDrug().getDisplayName());
			} else if (obs.getConcept().equals(quantityConcept)) {
				obsQuantity = obs;
			}
		}
		if (obsQuantity != null && valueToCancel > 0.0) {
			
			obsQuantity.setValueNumeric(obsQuantity.getValueNumeric() - valueToCancel);
			
			if (obsQuantity.getValueNumeric().doubleValue() == 0) {
				Context.getEncounterService().voidEncounter(filaEncounter,
				    "retired resulting from cancellation of regimen Dispensation");
				
				PrescriptionDispensation prescriptionDispensation = Context
				        .getService(PrescriptionDispensationService.class).findPrescriptionDispensationByDispensation(
				            drugOrder.getEncounter());
				Context.getService(PrescriptionDispensationService.class).retire(drugOrder.getCreator(),
				    prescriptionDispensation, "retired resulting from cancellation of regimen Dispensation");
			}
		}
	}
	
	private Map<Encounter, List<DispensationItem>> groupDispensationItemsByPrescription(Dispensation dispensation) {

		Map<Encounter, List<DispensationItem>> mapPrescription = new HashMap<>();

		Map<String, Encounter> mapCachedPrescription = new HashMap<>();

		for (DispensationItem dispensationItem : dispensation.getDispensationItems()) {

			Encounter prescription = mapCachedPrescription.get(dispensationItem.getPrescriptionUuid()) != null
					? mapCachedPrescription.get(dispensationItem.getPrescriptionUuid())
					: Context.getEncounterService().getEncounterByUuid(dispensationItem.getPrescriptionUuid());

			List<DispensationItem> list = mapPrescription.get(prescription);

			if (list == null) {
				mapPrescription.put(prescription, list = new ArrayList<>());
			}
			list.add(dispensationItem);
		}

		return mapPrescription;
	}
	
	@Override
	public List<Dispensation> findFilaDispensationByPatientAndDateInterval(Patient patient, Date startDate,
			Date endDate) throws PharmacyBusinessException {

		EncounterType filaEncounter = Context.getEncounterService()
				.getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);

		List<Encounter> filas = this.dispensationDAO.findEncountersByPatientAndEncounterTypeAndDateInterval(patient,
				filaEncounter, startDate, endDate);

		Concept arvDrugConcept = Context.getConceptService()

				.getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		List<Dispensation> dispensations = new ArrayList<>();
		for (Encounter fila : filas) {

			PrescriptionDispensation prescriptionDispensation = this.prescriptionDispensationService
					.findPrescriptionDispensationByFila(fila);

			Dispensation dispensation = new Dispensation();
			dispensation.setProviderUuid(prescriptionDispensation.getDispensation().getEncounterProviders().iterator()
					.next().getProvider().getUuid());
			List<DrugOrder> drugOrders = this.dispensationDAO.findDrugOrderByEncounterAndOrderActionAndVoided(
					prescriptionDispensation.getPrescription(), Action.NEW, false);

			Date prescriptionExpirationDate = this.prescriptionUtils.calculatePrescriptionExpirationDate(drugOrders);

			List<DispensationItem> dispensationItems = new ArrayList<>();
			for (Obs obs : fila.getAllObs()) {

				if (arvDrugConcept.equals(obs.getConcept())) {
					DispensationItem dispensationItem = new DispensationItem();

					DrugOrder drugOrder = this.dispensationDAO.findDrugOrderByOrderUuid(obs.getOrder().getUuid());
					dispensationItem.setDrugOrder(drugOrder);
					dispensationItem.setQuantityDispensed(obs.getValueNumeric());
					dispensationItem.setDateOfNextPickUp(obs.getValueDatetime());
					dispensationItem.setPrescription(preparePrescription(prescriptionDispensation.getPrescription()));
					dispensationItems.add(dispensationItem);
					dispensationItem.setPrescriptionExpirationDate(prescriptionExpirationDate);
					dispensationItem.setDispensationItemCreationDate(fila.getDateCreated());
				}
			}
			if (!dispensationItems.isEmpty()) {
				dispensation.setDispensationItems(dispensationItems);
				dispensations.add(dispensation);
			}
		}

		return dispensations;
	}
	
	@Override
	public void setDispensationDAO(DispensationDAO dispensationDAO) {
		this.dispensationDAO = dispensationDAO;
		
	}
	
	private Prescription preparePrescription(Encounter prescriptionEncounter) {
		
		Order anyOrder = prescriptionEncounter.getOrders().iterator().next();
		final Prescription prescription = new Prescription();
		prescription.setProvider(anyOrder.getOrderer());
		prescription.setPrescriptionEncounter(prescriptionEncounter);
		prescription.setPatient(prescriptionEncounter.getPatient());
		prescription.setLocation(prescriptionEncounter.getLocation());
		Set<Obs> allObs = prescriptionEncounter.getAllObs();
		Concept precriptionDateConcept = this.conceptService.getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE);
		for (Obs obs : allObs) {
			if (obs.getConcept().equals(precriptionDateConcept)) {
				prescription.setPrescriptionDate(obs.getValueDatetime());
				break;
			}
		}
		return prescription;
	}
}
