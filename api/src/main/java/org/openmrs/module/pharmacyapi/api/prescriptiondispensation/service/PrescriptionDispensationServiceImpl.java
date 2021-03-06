/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
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
import org.openmrs.Obs;
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
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public class PrescriptionDispensationServiceImpl extends BaseOpenmrsService implements PrescriptionDispensationService {
	
	private PrescriptionDispensationDAO prescriptionDispensationDAO;
	
	private PharmacyHeuristicService pharmacyHeuristicService;
	
	@Override
	public void setPrescriptionDispensationDAO(final PrescriptionDispensationDAO prescriptionDispensationDAO) {
		
		this.prescriptionDispensationDAO = prescriptionDispensationDAO;
	}
	
	@Override
	public void setPharmacyHeuristicService(final PharmacyHeuristicService pharmacyHeuristicService) {
		
		this.pharmacyHeuristicService = pharmacyHeuristicService;
	}
	
	@Override
	public PrescriptionDispensation savePrescriptionDispensation(final Patient patient, final Encounter prescription,
	        final Encounter dispensation) {
		
		final PrescriptionDispensation prescriptionDispensation = new PrescriptionDispensation(patient, prescription,
		        dispensation);
		
		// TODO: Should creates a Component validator to move the rules
		// validator a put the mensagem validator in a resource bundle
		if ((patient == null) || (prescription == null) || (dispensation == null)) {
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
	public boolean isArvDrug(final DrugOrder drugOrder) throws PharmacyBusinessException {
		
		final Concept regime = this.getArvRegimeByEncounterDrugOrder(drugOrder);
		
		if (regime != null) {
			
			final DrugItem drugItem = Context.getService(DrugItemService.class)
			        .findDrugItemByDrugId(drugOrder.getDrug().getDrugId());
			try {
				Context.getService(DrugRegimeService.class).findDrugRegimeByRegimeAndDrugItem(regime, drugItem);
				return Boolean.TRUE;
			}
			catch (final EntityNotFoundException e) {}
		}
		return Boolean.FALSE;
	}
	
	private Concept getArvRegimeByEncounterDrugOrder(final DrugOrder drugOrder) {
		
		final Concept arvConceptQuestion = Context.getConceptService()
		        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
		
		final Encounter encounter = Context.getEncounterService()
		        .getEncounter(drugOrder.getEncounter().getEncounterId());
		
		final Set<Obs> allObs = encounter.getAllObs();
		
		for (final Obs obs : allObs) {
			
			if (arvConceptQuestion.equals(obs.getConcept())) {
				
				return obs.getValueCoded();
			}
		}
		
		return null;
	}
	
	@Override
	public PrescriptionDispensation findPrescriptionDispensationByDispensation(final Encounter dispensation)
	        throws PharmacyBusinessException {
		
		return this.prescriptionDispensationDAO.findByDispensationEncounter(dispensation);
	}
	
	public boolean isTheSameConceptAndSameDrug(final DrugOrder order, final Obs observation) {
		
		final Drug obsDrug = this.pharmacyHeuristicService.findDrugByOrderUuid(observation.getOrder().getUuid());
		
		return MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())
		        && order.getDrug().getUuid().equals(obsDrug.getUuid());
	}
	
	@Override
	public void retire(final User user, final PrescriptionDispensation prescriptionDispensation, final String reason)
	        throws PharmacyBusinessException {
		
		final PrescriptionDispensation found = this.prescriptionDispensationDAO
		        .findByUuid(prescriptionDispensation.getUuid());
		
		found.setRetired(true);
		found.setRetireReason(reason);
		found.setRetiredBy(user);
		found.setDateRetired(new Date());
		
		this.prescriptionDispensationDAO.retire(found);
	}
	
	@Override
	public void updatePrescriptionDispensation(final PrescriptionDispensation prescriptionDispensation) {
		this.prescriptionDispensationDAO.update(prescriptionDispensation);
	}
	
	@Override
	public PrescriptionDispensation findPrescriptionDispensationByFila(final Encounter fila)
	        throws PharmacyBusinessException {
		
		return this.prescriptionDispensationDAO.findByFila(fila);
	}
	
	@Override
	public List<PrescriptionDispensation> findPrescriptionDispensationByPrescription(final Encounter prescription)
	        throws PharmacyBusinessException {
		return this.prescriptionDispensationDAO.findByPrescription(prescription, false);
	}
	
}
