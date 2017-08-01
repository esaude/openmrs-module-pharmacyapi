/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.dispensation.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
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
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.validation.DispensationValidator;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.MappedEncounters;
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
	
	@Override
	public Dispensation dispense(final Dispensation dispensation) throws PharmacyBusinessException {
		
		try {
			
			// workaround to controll the hibernate sessions commits
			this.dbSessionManager.setManualFlushMode();
			
			dispensationValidator.validateCreation(dispensation);
			
			final Person person = this.personService.getPersonByUuid(dispensation.getProviderUuid());
			final Collection<Provider> providers = this.providerService.getProvidersByPerson(person);
			final Provider provider = providers.iterator().next();
			final Patient patient = this.patientService.getPatientByUuid(dispensation.getPatientUuid());
			
			final EncounterRole encounterRole = this.encounterService
			        .getEncounterRoleByUuid(MappedEncounters.DEFAULT_ENCONTER_ROLE);
			final EncounterType encounterType = this.encounterService
			        .getEncounterTypeByUuid(MappedEncounters.DISPENSATION_ENCOUNTER_TYPE);
			
			final Location location = this.locationService.getLocationByUuid(dispensation.getLocationUuid());
			
			Order arvOrder = null;
			DispensationItem arvDispensationItem = null;
			
			Concept arvConceptQuestion = Context.getConceptService().getConceptByUuid(
			    MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
			
			final Concept dispensationConceptSet = this.conceptService.getConceptByUuid(MappedConcepts.DISPENSATION_SET);
			final Concept quantityConcept = this.conceptService.getConceptByUuid(MappedConcepts.MEDICATION_QUANTITY);
			final Concept nextPickUpConcept = this.conceptService.getConceptByUuid(MappedConcepts.DATE_OF_NEXT_PICK_UP);
			
			Map<Encounter, List<DispensationItem>> mapDispensationItemByPrescription = groupDispensationItemsByPrescription(dispensation);
			
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
						arvOrder = order;
						arvDispensationItem = dispensationItem;
					}
					
					orderProcess.setOrderer(provider);
					
					this.prepareDispensation(orderProcess, dispensationEncounter, dispensationConceptSet, quantityConcept,
					    nextPickUpConcept, dispensationItem, arvConceptQuestion);
				}
				
				this.encounterService.saveEncounter(dispensationEncounter);
				
				this.prescriptionDispensationService.savePrescriptionDispensation(patient, prescription,
				    dispensationEncounter);
				
				if (arvOrder != null) {
					
					final EncounterType filaEncounterType = this.encounterService
					        .getEncounterTypeByUuid(MappedEncounters.FILA_ENCOUNTER_TYPE);
					
					final Encounter filaEncounter = this.createEncounter(provider, patient, encounterRole,
					    filaEncounterType, location);
					
					this.processFila(filaEncounter, arvOrder, arvDispensationItem, quantityConcept, nextPickUpConcept);
				}
			}
			
		}
		finally {
			this.dbSessionManager.setAutoFlushMode();
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
	
	private void processFila(final Encounter enconter, final Order arvOrder, final DispensationItem dispensationItem,
	        final Concept quantityConcept, final Concept nextPickUpConcept) {
		
		final Concept posologyConcept = this.conceptService.getConceptByUuid(MappedConcepts.POSOLOGY);
		final Concept regimenConcept = this.conceptService.getConceptByUuid(MappedConcepts.REGIMEN);
		Concept regime = this.conceptService.getConceptByUuid(dispensationItem.getRegimeUuid());
		
		final Obs obsQuantity = new Obs();
		obsQuantity.setConcept(quantityConcept);
		obsQuantity.setValueNumeric(dispensationItem.getQuantityToDispense());
		
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
		
		enconter.addObs(obsQuantity);
		enconter.addObs(obsNextPickUp);
		enconter.addObs(posologyObs);
		enconter.addObs(regimenObs);
		
		this.encounterService.saveEncounter(enconter);
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
		
		for (DispensationItem dispensationItem : dispensation.getDispensationItems()) {
			
			Order order = Context.getOrderService().getOrderByUuid(dispensationItem.getOrderUuid());
			
			Context.getOrderService().voidOrder(order, cancelationReason);
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
}
