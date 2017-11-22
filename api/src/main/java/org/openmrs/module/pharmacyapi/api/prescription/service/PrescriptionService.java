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

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;

/**
 * @author Stélio Moiane
 */
public interface PrescriptionService extends OpenmrsService {
	
	void setEncounterService(EncounterService encounterService);
	
	void setDispensationDAO(DispensationDAO dispensationDAO);
	
	void setPharmacyHeuristicService(PharmacyHeuristicService pharmacyHeuristicService);
	
	Prescription createPrescription(Prescription prescription, final Date date) throws PharmacyBusinessException;
	
	List<Prescription> findAllPrescriptionsByPatient(final Patient patient, Date actualDate)
	        throws PharmacyBusinessException;
	
	List<Prescription> findActivePrescriptionsByPatient(final Patient patient, Date actualDate)
	        throws PharmacyBusinessException;
	
	void cancelPrescriptionItem(PrescriptionItem prescriptionItem, String cancelationReason)
	        throws PharmacyBusinessException;
	
	List<Prescription> findNotExpiredArvPrescriptions(Patient patient, Date actualDate)
	        throws PharmacyBusinessException;
}
