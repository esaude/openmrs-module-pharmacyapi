/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.drugitem.service.DrugItemService;
import org.openmrs.module.pharmacyapi.api.drugregime.service.DrugRegimeService;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public class PrescriptionDispensationServiceImpl extends BaseOpenmrsService implements PrescriptionDispensationService {
	
	private PrescriptionDispensationDAO prescriptionDispensationDAO;
	
	@Override
	public void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO) {
		
		this.prescriptionDispensationDAO = prescriptionDispensationDAO;
	}
	
	@Override
	public PrescriptionDispensation savePrescriptionDispensation(Patient patient, Encounter prescription,
	        Encounter dispensation) {
		
		PrescriptionDispensation prescriptionDispensation = new PrescriptionDispensation(patient, prescription, dispensation);
		
		// TODO: Should creates a Component validator to move the rules
		// validator a put the mensagem validator in a resource bundle
		if (patient == null || prescription == null || dispensation == null) {
			throw new IllegalArgumentException("The arguments passed for dispensation is not valid ");
		}
		
		if (!patient.getUuid().equals(prescription.getPatient().getUuid())
		        || !patient.getUuid().equals(dispensation.getPatient().getUuid())) {
			throw new IllegalArgumentException(
			        "The Patient passed  on the argument must be the same with the prescription and dispensation patient");
		}
		
		this.prescriptionDispensationDAO.save(prescriptionDispensation);
		
		return prescriptionDispensation;
	}
	
	@Override
	public boolean isArvDrug(final PrescriptionItem prescriptionItem, final DrugOrder drugOrder)
	        throws PharmacyBusinessException {
		
		Concept regime = getArvRegimeByEncounterDrugOrder(drugOrder);
		
		if (regime != null) {
			
			DrugItem drugItem = Context.getService(DrugItemService.class).findDrugItemByDrugId(
			    drugOrder.getDrug().getDrugId());
			
			try {
				Context.getService(DrugRegimeService.class).findDrugRegimeByRegimeAndDrugItem(regime, drugItem);
				
				prescriptionItem.setRegime(regime);
				return Boolean.TRUE;
				
			}
			catch (EntityNotFoundException e) {}
		}
		return Boolean.FALSE;
	}
	
	private Concept getArvRegimeByEncounterDrugOrder(DrugOrder drugOrder) {
		
		Concept arvConceptQuestion = Context.getConceptService().getConceptByUuid(
		    MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		
		Encounter encounter = Context.getEncounterService().getEncounter(drugOrder.getEncounter().getEncounterId());
		
		Set<Obs> allObs = encounter.getAllObs();
		
		for (Obs obs : allObs) {
			
			if (arvConceptQuestion.equals(obs.getConcept())) {
				
				return obs.getValueCoded();
			}
		}
		
		return null;
	}
	
	@Override
	public PrescriptionDispensation findPrescriptionDispensationByDispensation(Encounter dispensation)
	        throws PharmacyBusinessException {
		
		return this.prescriptionDispensationDAO.findByDispensationEncounter(dispensation);
	}
	
	public boolean isTheSameConceptAndSameDrug(final DrugOrder order, Obs observation) {
		
		Drug obsDrug = findDrugByOrderUuid(observation.getOrder().getUuid());
		
		return MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())
		        && order.getDrug().getUuid().equals(obsDrug.getUuid());
	}
	
	@Override
	public Drug findDrugByOrderUuid(String uuid) {
		
		return this.prescriptionDispensationDAO.findDrugByOrderUuid(uuid);
	}
	
	@Override
	public Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order) {
		
		return this.prescriptionDispensationDAO
		        .findEncounterByPatientAndEncounterTypeAndOrder(patient, encounterType, order);
	}
	
	@Override
	public List<Obs> findObsByOrder(Order order) {
		
		return this.prescriptionDispensationDAO.findObsByOrder(order);
	}
	
	@Override
	public void retire(User user, PrescriptionDispensation prescriptionDispensation, String reason)
	        throws PharmacyBusinessException {
		
		PrescriptionDispensation found = this.prescriptionDispensationDAO.findByUuid(prescriptionDispensation.getUuid());
		
		found.setRetired(true);
		found.setRetireReason(reason);
		found.setRetiredBy(user);
		found.setDateRetired(new Date());
		
		this.prescriptionDispensationDAO.retire(found);
	}
}
