/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.dispensation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.Visit;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.common.util.MappedForms;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.validation.DispensationValidator;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
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
	
	private PharmacyHeuristicService pharmacyHeuristicService;
	
	@Autowired
	private DispensationValidator dispensationValidator;
	
	@Autowired
	private PrescriptionUtils prescriptionUtils;
	
	private DispensationDAO dispensationDAO;
	
	private BatchService batchService;
	
	@Override
	public Dispensation dispense(final Dispensation dispensation) throws PharmacyBusinessException {
		
		try {
			
			// workaround to controll the hibernate sessions commits
			this.dbSessionManager.setManualFlushMode();
			
			this.dispensationValidator.validateCreation(dispensation, new Date());
			
			final Person person = this.personService.getPersonByUuid(dispensation.getProviderUuid());
			final Provider provider = this.providerService.getProvidersByPerson(person).iterator().next();
			final Patient patient = this.patientService.getPatientByUuid(dispensation.getPatientUuid());
			
			final EncounterRole encounterRole = this.encounterService
			        .getEncounterRoleByUuid(MappedEncounters.DEFAULT_ENCONTER_ROLE);
			final EncounterType encounterType = this.encounterService
			        .getEncounterTypeByUuid(MappedEncounters.DISPENSATION_ENCOUNTER_TYPE);
			
			final Location location = this.locationService.getLocationByUuid(dispensation.getLocationUuid());
			
			final List<DispensationItem> arvDispensationItems = new ArrayList<>();
			
			final Concept arvConceptQuestion = Context.getConceptService()
			        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
			
			final Concept dispensationConceptSet = this.conceptService
			        .getConceptByUuid(MappedConcepts.DISPENSATION_SET);
			final Concept quantityConcept = this.conceptService.getConceptByUuid(MappedConcepts.MEDICATION_QUANTITY);
			final Concept nextPickUpConcept = this.conceptService.getConceptByUuid(MappedConcepts.DATE_OF_NEXT_PICK_UP);
			
			final Map<Encounter, List<DispensationItem>> mapDispensationItemByPrescription = this
			        .groupDispensationItemsByPrescription(dispensation);
			
			final Map<DrugOrder, Double> mapQuantityByDrugOrder = new HashMap<>();
			for (final Entry<Encounter, List<DispensationItem>> prescriptionDispensationItems : mapDispensationItemByPrescription
			        .entrySet()) {
				
				final Encounter prescriptionEncounter = prescriptionDispensationItems.getKey();
				final Encounter dispensationEncounter = this.createEncounter(provider, patient, encounterRole,
				    encounterType, location);
				
				for (final DispensationItem dispensationItem : prescriptionDispensationItems.getValue()) {
					
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
					orderProcess.setPatient(order.getPatient());
					this.prepareDispensation(orderProcess, dispensationEncounter, dispensationConceptSet,
					    quantityConcept, nextPickUpConcept, dispensationItem, arvConceptQuestion);
					
					mapQuantityByDrugOrder.put((DrugOrder) orderProcess, dispensationItem.getQuantityToDispense());
				}
				
				this.encounterService.saveEncounter(dispensationEncounter);
				
				this.prescriptionDispensationService.savePrescriptionDispensation(patient, prescriptionEncounter,
				    dispensationEncounter);
				
				this.performWastDrugOrders(mapQuantityByDrugOrder, location);
				
				if (!arvDispensationItems.isEmpty()) {
					
					final EncounterType filaEncounterType = this.encounterService
					        .getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);
					
					final Encounter filaEncounter = this.createEncounter(provider, patient, encounterRole,
					    filaEncounterType, location);
					
					this.processFila(filaEncounter, dispensationEncounter, arvDispensationItems, arvConceptQuestion,
					    quantityConcept, nextPickUpConcept);
				}
			}
		}
		finally {
			Context.flushSession();
		}
		
		return dispensation;
	}
	
	private void performWastDrugOrders(final Map<DrugOrder, Double> mapQuantityByDrugOrder, final Location location) {
		for (final Entry<DrugOrder, Double> entry : mapQuantityByDrugOrder.entrySet()) {
			try {
				this.batchService.createWasteDrug(entry.getKey(), location, entry.getValue(), new Date());
			}
			catch (final Exception e) {
				throw new APIException(e.getMessage());
			}
		}
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
	
	private void prepareDispensation(final Order order, final Encounter dispensationEncounter,
	        final Concept dispensationConceptSet, final Concept quantityConcept, final Concept nextPickUpConcept,
	        final DispensationItem dispensationItem, final Concept arvConceptQuestion) {
		
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
			final Concept valueCoded = Context.getConceptService().getConceptByUuid(dispensationItem.getRegimeUuid());
			final Obs obsRegime = new Obs();
			obsRegime.setConcept(arvConceptQuestion);
			obsRegime.setValueCoded(valueCoded);
			obsRegime.setOrder(order);
			dispensationEncounter.addObs(obsRegime);
		}
		
		dispensationEncounter.addObs(obsGroup);
		dispensationEncounter.addOrder(order);
	}
	
	private void processFila(final Encounter filaEncounter, final Encounter dispensationEncounter,
	        final List<DispensationItem> arvDispensationItems, final Concept arvConceptQuestion,
	        final Concept quantityConcept, final Concept nextPickUpConcept) throws PharmacyBusinessException {
		
		final DispensationItem dispensationItem = arvDispensationItems.iterator().next();
		final Order arvOrder = this.orderService.getOrderByUuid(dispensationItem.getOrderUuid());
		
		final Concept posologyConcept = this.conceptService.getConceptByUuid(MappedConcepts.POSOLOGY);
		final Concept regimenConcept = this.conceptService.getConceptByUuid(MappedConcepts.REGIMEN);
		final Concept regime = this.conceptService.getConceptByUuid(dispensationItem.getRegimeUuid());
		
		final Obs obsQuantity = new Obs();
		obsQuantity.setConcept(quantityConcept);
		obsQuantity.setValueNumeric(this.calculateArvDispensedQuantity(arvDispensationItems));
		
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
		
		final Visit lastVisit = this.pharmacyHeuristicService
		        .findLastVisitByPatientAndEncounterDate(filaEncounter.getPatient(), new Date());
		final Form filaForm = Context.getFormService().getFormByUuid(MappedForms.FILA);
		
		filaEncounter.setForm(filaForm);
		filaEncounter.setVisit(lastVisit);
		filaEncounter.addObs(obsQuantity);
		filaEncounter.addObs(obsNextPickUp);
		filaEncounter.addObs(posologyObs);
		filaEncounter.addObs(regimenObs);
		
		for (final DispensationItem dispItem : arvDispensationItems) {
			
			final DrugOrder order = (DrugOrder) this.orderService.getOrderByUuid(dispItem.getOrderUuid());
			
			final Obs obsDrugOrder = new Obs();
			obsDrugOrder.setConcept(arvConceptQuestion);
			obsDrugOrder.setValueCoded(order.getDrug().getConcept());
			obsDrugOrder.setValueNumeric(dispItem.getQuantityToDispense());
			obsDrugOrder.setValueDatetime(dispItem.getDateOfNextPickUp());
			
			final DrugOrder dispensedDrugOrder = this.getDispensedDrugOrder(dispensationEncounter, order.getDrug());
			obsDrugOrder.setOrder(dispensedDrugOrder);
			obsDrugOrder.setValueDrug(dispensedDrugOrder.getDrug());
			
			filaEncounter.addObs(obsDrugOrder);
		}
		
		final Encounter createdEncounter = this.encounterService.saveEncounter(filaEncounter);
		final PrescriptionDispensation prescriptionDispensation = this.prescriptionDispensationService
		        .findPrescriptionDispensationByDispensation(dispensationEncounter);
		prescriptionDispensation.setFila(createdEncounter);
		this.prescriptionDispensationService.updatePrescriptionDispensation(prescriptionDispensation);
	}
	
	private DrugOrder getDispensedDrugOrder(final Encounter dispensationEncounter, final Drug drug)
	        throws PharmacyBusinessException {
		
		for (final Order order : dispensationEncounter.getOrders()) {
			
			final DrugOrder drugOrder = (DrugOrder) order;
			
			if (drugOrder.getDrug().equals(drug)) {
				
				return drugOrder;
			}
			
		}
		throw new PharmacyBusinessException("Drug Order not found for drug " + drug);
	}
	
	private Double calculateArvDispensedQuantity(final List<DispensationItem> arvDispensationItems) {
		
		Double totalQuantityToDispense = 0.0;
		for (final DispensationItem dispensationItem : arvDispensationItems) {
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
	public void setPrescriptionDispensationService(
	        final PrescriptionDispensationService prescriptionDispensationService) {
		
		this.prescriptionDispensationService = prescriptionDispensationService;
	}
	
	@Override
	public void setDispensationDAO(final DispensationDAO dispensationDAO) {
		this.dispensationDAO = dispensationDAO;
		
	}
	
	@Override
	public void setPharmacyHeuristicService(final PharmacyHeuristicService pharmacyHeuristicService) {
		this.pharmacyHeuristicService = pharmacyHeuristicService;
	}
	
	@Override
	public void cancelDispensationItems(final Dispensation dispensation, final String cancelationReason)
	        throws Exception {
		
		this.dispensationValidator.validateCancellation(dispensation, new Date());
		
		final DispensationItem dispensationItem = dispensation.getDispensationItems().iterator().next();
		final DrugOrder drugOrder = (DrugOrder) Context.getOrderService()
		        .getOrderByUuid(dispensationItem.getOrderUuid());
		
		if (StringUtils.isNotBlank(dispensationItem.getRegimeUuid())
		        && this.prescriptionDispensationService.isArvDrug(drugOrder)) {
			this.removeDrugOrderObsFromFilaEncounter(dispensation, drugOrder);
			
		}
		Context.getOrderService().voidOrder(drugOrder, cancelationReason);
		
		final List<Obs> lstObs = this.pharmacyHeuristicService.findObservationsByOrder(drugOrder);
		
		for (final Obs obs : lstObs) {
			
			Context.getObsService().voidObs(obs,
			    "Cancellation of Dispensation Item " + drugOrder.getDrug().getDisplayName());
		}
		
		this.batchService.reverseWastedDrug(drugOrder);
	}
	
	private void removeDrugOrderObsFromFilaEncounter(final Dispensation dispensation, final DrugOrder drugOrder)
	        throws PharmacyBusinessException {
		
		final Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
		
		final EncounterType filaEncounterType = this.encounterService
		        .getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);
		final Encounter filaEncounter = this.pharmacyHeuristicService
		        .findEncounterByPatientAndEncounterTypeAndOrder(patient, filaEncounterType, drugOrder);
		
		final Concept arvConceptQuestion = Context.getConceptService()
		        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		final Concept quantityConcept = this.conceptService.getConceptByUuid(MappedConcepts.MEDICATION_QUANTITY);
		final Set<Obs> filaObs = filaEncounter.getAllObs();
		
		Double valueToCancel = 0.0;
		Obs obsQuantity = null;
		
		for (final Obs obs : filaObs) {
			if (obs.getConcept().equals(arvConceptQuestion)
			        && (obs.getOrder().getOrderId().intValue() == drugOrder.getOrderId().intValue())) {
				
				valueToCancel += obs.getValueNumeric();
				Context.getObsService().voidObs(obs,
				    "Cancellation of Dispensation Item " + drugOrder.getDrug().getDisplayName());
			} else if (obs.getConcept().equals(quantityConcept)) {
				obsQuantity = obs;
			}
		}
		if ((obsQuantity != null) && (valueToCancel > 0.0)) {
			
			obsQuantity.setValueNumeric(obsQuantity.getValueNumeric() - valueToCancel);
			
			if (obsQuantity.getValueNumeric().doubleValue() == 0) {
				Context.getEncounterService().voidEncounter(filaEncounter,
				    "retired resulting from cancellation of regimen Dispensation");
				
				final PrescriptionDispensation prescriptionDispensation = Context
				        .getService(PrescriptionDispensationService.class)
				        .findPrescriptionDispensationByDispensation(drugOrder.getEncounter());
				Context.getService(PrescriptionDispensationService.class).retire(drugOrder.getCreator(),
				    prescriptionDispensation, "retired resulting from cancellation of regimen Dispensation");
			}
		}
	}
	
	private Map<Encounter, List<DispensationItem>> groupDispensationItemsByPrescription(
	        final Dispensation dispensation) {
		
		final Map<Encounter, List<DispensationItem>> mapPrescription = new HashMap<>();
		
		final Map<String, Encounter> mapCachedPrescription = new HashMap<>();
		
		for (final DispensationItem dispensationItem : dispensation.getDispensationItems()) {
			
			final Encounter prescription = mapCachedPrescription.get(dispensationItem.getPrescriptionUuid()) != null
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
	public List<Dispensation> findFilaDispensationByPatientAndDateInterval(final Patient patient, final Date startDate,
	        final Date endDate) throws PharmacyBusinessException {
		
		final EncounterType filaEncounter = Context.getEncounterService()
		        .getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);
		
		final List<Encounter> filas = this.dispensationDAO
		        .findEncountersByPatientAndEncounterTypeAndDateInterval(patient, filaEncounter, startDate, endDate);
		
		final Concept arvDrugConcept = Context.getConceptService()
		        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		final List<Dispensation> dispensations = new ArrayList<>();
		for (final Encounter fila : filas) {
			
			final PrescriptionDispensation prescriptionDispensation = this.prescriptionDispensationService
			        .findPrescriptionDispensationByFila(fila);
			
			final Dispensation dispensation = new Dispensation();
			dispensation.setProviderUuid(prescriptionDispensation.getDispensation().getEncounterProviders().iterator()
			        .next().getProvider().getUuid());
			final List<DrugOrder> drugOrders = this.dispensationDAO.findDrugOrderByEncounterAndOrderActionAndVoided(
			    prescriptionDispensation.getPrescription(), Action.NEW, false);
			
			final Date prescriptionExpirationDate = this.prescriptionUtils
			        .calculatePrescriptionExpirationDate(drugOrders);
			
			final List<DispensationItem> dispensationItems = new ArrayList<>();
			for (final Obs obs : fila.getAllObs()) {
				
				if (arvDrugConcept.equals(obs.getConcept())) {
					final DispensationItem dispensationItem = new DispensationItem();
					DrugOrder drugOrder = this.dispensationDAO.findDrugOrderByOrderUuid(obs.getOrder().getUuid());
					if (Action.DISCONTINUE.equals(drugOrder.getAction())) {
						drugOrder = (DrugOrder) drugOrder.getPreviousOrder();
					}
					dispensationItem.setDrugOrder(drugOrder);
					dispensationItem.setQuantityDispensed(obs.getValueNumeric());
					dispensationItem.setDateOfNextPickUp(obs.getValueDatetime());
					dispensationItem
					        .setPrescription(this.preparePrescription(prescriptionDispensation.getPrescription()));
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
	
	private Prescription preparePrescription(final Encounter prescriptionEncounter) {
		
		final Order anyOrder = prescriptionEncounter.getOrders().iterator().next();
		final Prescription prescription = new Prescription();
		prescription.setProvider(anyOrder.getOrderer());
		prescription.setPrescriptionEncounter(prescriptionEncounter);
		prescription.setPatient(prescriptionEncounter.getPatient());
		prescription.setLocation(prescriptionEncounter.getLocation());
		final Set<Obs> allObs = prescriptionEncounter.getAllObs();
		final Concept precriptionDateConcept = this.conceptService
		        .getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE);
		for (final Obs obs : allObs) {
			if (obs.getConcept().equals(precriptionDateConcept)) {
				prescription.setPrescriptionDate(obs.getValueDatetime());
				break;
			}
		}
		return prescription;
	}
	
	@Override
	public void setBatchService(final BatchService batchService) {
		this.batchService = batchService;
	}
	
}
