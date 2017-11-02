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
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.prescription.service;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;

/**
 * @author Stélio Moiane
 */
public interface PrescriptionService extends OpenmrsService {
	
	void setConceptService(final ConceptService conceptService);
	
	void setEncounterService(EncounterService encounterService);
	
	void setDispensationDAO(DispensationDAO dispensationDAO);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
	
	void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService);
	
	void setPharmacyHeuristicService(PharmacyHeuristicService pharmacyHeuristicService);
	
	Prescription createPrescription(Prescription prescription) throws PharmacyBusinessException;
	
	List<Prescription> findAllPrescriptionsByPatient(final Patient patient);
	
	List<Prescription> findPrescriptionsByPatientAndActiveStatus(final Patient patient);
	
	void cancelPrescriptionItem(PrescriptionItem prescriptionItem, String cancelationReason)
	        throws PharmacyBusinessException;
	
}
