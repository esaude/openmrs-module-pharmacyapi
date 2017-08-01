/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
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
}
