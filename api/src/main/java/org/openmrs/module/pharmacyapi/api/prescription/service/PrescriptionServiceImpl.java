/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.prescription.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription.PrescriptionStatus;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem.PrescriptionItemStatus;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionUtils;
import org.openmrs.module.pharmacyapi.api.prescription.validation.PrescriptionValidator;
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
public class PrescriptionServiceImpl extends BaseOpenmrsService implements PrescriptionService {
	
	private ConceptService conceptService;
	
	private DispensationDAO dispensationDAO;
	
	private DbSessionManager dbSessionManager;
	
	private PrescriptionDispensationService prescriptionDispensationService;
	
	@Autowired
	private PrescriptionValidator prescriptionValidator;
	
	@Autowired
	private PrescriptionUtils prescriptionUtils;
	
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
	public List<Prescription> findPrescriptionsByPatientAndActiveStatus(final Patient patient) {

		List<PrescriptionItem> prescriptionItems = getNotDispensedPrescriptionItems(patient);

		prescriptionItems.addAll(getDispensedPrescriptionItems(patient));

		Map<Encounter, List<PrescriptionItem>> mapPrescriptionItemsByPrescriptionEncounter = new HashMap<>();

		for (PrescriptionItem prescriptionItem : prescriptionItems) {

			List<PrescriptionItem> lst = mapPrescriptionItemsByPrescriptionEncounter
					.get(prescriptionItem.getPrescription().getPrescriptionEncounter());

			if (lst == null) {
				mapPrescriptionItemsByPrescriptionEncounter
						.put(prescriptionItem.getPrescription().getPrescriptionEncounter(), lst = new ArrayList<>());
			}
			lst.add(prescriptionItem);
		}

		List<Prescription> result = new ArrayList<>();

		for (Encounter prescriptionEncounter : mapPrescriptionItemsByPrescriptionEncounter.keySet()) {

			List<PrescriptionItem> items = mapPrescriptionItemsByPrescriptionEncounter.get(prescriptionEncounter);

			items = this.prescriptionUtils.filterPrescriptionItemsByStatus(items,
					Arrays.asList(PrescriptionItemStatus.NEW, PrescriptionItemStatus.ACTIVE));

			if (!items.isEmpty()) {

				Prescription prescription = preparePrescription(prescriptionEncounter);
				prescription.setPrescriptionItems(items);
				prescription.setPrescriptionStatus(PrescriptionStatus.ACTIVE);
				result.add(prescription);
			}
		}
		return result;
	}
	
	private List<PrescriptionItem> getDispensedPrescriptionItems(Patient patient) {

		final List<PrescriptionItem> prescriptionItems = new ArrayList<>();

		final List<DrugOrder> dispensedOrders = this.dispensationDAO.findDispensedDrugOrdersByPatient(patient);

		if (!dispensedOrders.isEmpty()) {

			Map<Encounter, List<DrugOrder>> mapDrugOrdersByDispensation = mapDrugOrdersByEncounter(dispensedOrders);

			for (Encounter dispensationEncounter : mapDrugOrdersByDispensation.keySet()) {

				try {
					Encounter prescriptionEncounter = this.prescriptionDispensationService
							.findPrescriptionDispensationByDispensation(dispensationEncounter).getPrescription();
					Prescription prescription = preparePrescription(prescriptionEncounter);

					prescriptionItems.addAll(prescriptionUtils.preparePrescriptionItems(prescription,
							prescriptionEncounter, mapDrugOrdersByDispensation.get(dispensationEncounter)));

				} catch (PharmacyBusinessException e) {
				}
			}
		}
		return prescriptionItems;
	}
	
	private List<PrescriptionItem> getNotDispensedPrescriptionItems(Patient patient) {

		final List<PrescriptionItem> prescriptionItems = new ArrayList<>();
		EncounterType encounterType = this.getEncounterTypeByPatientAge(patient);

		List<DrugOrder> ordersNotDispensed = this.dispensationDAO.findNotDispensedDrugOrdersByPatient(patient,
				encounterType);

		if (!ordersNotDispensed.isEmpty()) {

			Map<Encounter, List<DrugOrder>> mapDrugOrdersByPrescription = mapDrugOrdersByEncounter(ordersNotDispensed);

			for (Encounter prescriptionEncounter : mapDrugOrdersByPrescription.keySet()) {

				if (encounterType.equals(prescriptionEncounter.getEncounterType())) {

					Prescription prescription = preparePrescription(prescriptionEncounter);

					prescriptionItems.addAll(prescriptionUtils.preparePrescriptionItems(prescription,
							prescriptionEncounter, mapDrugOrdersByPrescription.get(prescriptionEncounter)));
				}
			}
		}
		return prescriptionItems;
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
	public Prescription createPrescription(Prescription prescription) throws PharmacyBusinessException {
		
		prescriptionValidator.validateCreation(prescription);
		
		Patient patient = Context.getPatientService().getPatientByUuid(prescription.getPatient().getUuid());
		Provider provider = Context.getProviderService().getProviderByUuid(prescription.getProvider().getUuid());
		prescription.setPatient(patient);
		prescription.setProvider(provider);
		
		Encounter encounter = this.prescriptionUtils.prepareEncounter(prescription);
		this.prescriptionUtils.prepareObservations(prescription, encounter);
		this.prescriptionUtils.prepareOrders(prescription, encounter);
		
		Context.getEncounterService().saveEncounter(encounter);
		prescription.setPrescriptionEncounter(encounter);
		
		return prescription;
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

		List<PrescriptionItem> prescriptionItems = getNotDispensedPrescriptionItems(patient);
		prescriptionItems.addAll(getDispensedPrescriptionItems(patient));

		Map<Encounter, List<PrescriptionItem>> mapPrescriptionItemsByPrescriptionEncounter = new HashMap<>();

		for (PrescriptionItem prescriptionItem : prescriptionItems) {

			List<PrescriptionItem> lst = mapPrescriptionItemsByPrescriptionEncounter
					.get(prescriptionItem.getPrescription().getPrescriptionEncounter());

			if (lst == null) {
				mapPrescriptionItemsByPrescriptionEncounter
						.put(prescriptionItem.getPrescription().getPrescriptionEncounter(), lst = new ArrayList<>());
			}
			lst.add(prescriptionItem);
		}

		List<Prescription> result = new ArrayList<>();

		for (Encounter prescriptionEncounter : mapPrescriptionItemsByPrescriptionEncounter.keySet()) {

			List<PrescriptionItem> items = mapPrescriptionItemsByPrescriptionEncounter.get(prescriptionEncounter);

			if (!items.isEmpty()) {
				Prescription prescription = preparePrescription(prescriptionEncounter);
				prescription.setPrescriptionItems(items);
				prescription.setPrescriptionStatus(this.prescriptionUtils.calculatePrescriptioStatus(items));

				result.add(prescription);
			}
		}
		return result;
	}
	
	private Prescription preparePrescription(Encounter prescriptionEncounter) {
		
		Order anyOrder = prescriptionEncounter.getOrders().iterator().next();
		final Prescription prescription = new Prescription();
		prescription.setProvider(anyOrder.getOrderer());
		prescription.setPrescriptionEncounter(prescriptionEncounter);
		prescription.setPatient(prescriptionEncounter.getPatient());
		prescription.setLocation(prescriptionEncounter.getLocation());
		setPrescriptionDate(prescription, prescriptionEncounter);
		
		return prescription;
	}
	
	private Map<Encounter, List<DrugOrder>> mapDrugOrdersByEncounter(List<DrugOrder> drugOrders) {

		Map<Encounter, List<DrugOrder>> mapped = new HashMap<>();

		for (DrugOrder drugOrder : drugOrders) {

			List<DrugOrder> list = mapped.get(drugOrder.getEncounter());
			if (list == null) {
				mapped.put(drugOrder.getEncounter(), list = new ArrayList<>());
			}
			list.add(drugOrder);
		}
		return mapped;
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
}
