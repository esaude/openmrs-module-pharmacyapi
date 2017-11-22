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

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationService extends OpenmrsService {
	
	void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO);
	
	void setPharmacyHeuristicService(PharmacyHeuristicService pharmacyHeuristicService);
	
	PrescriptionDispensation savePrescriptionDispensation(Patient patient, Encounter prescription,
	        Encounter dispensation);
	
	void updatePrescriptionDispensation(PrescriptionDispensation prescriptionDispensation);
	
	void retire(User user, PrescriptionDispensation prescriptionDispensation, String reason)
	        throws PharmacyBusinessException;
	
	public boolean isArvDrug(final DrugOrder drugOrder) throws PharmacyBusinessException;
	
	PrescriptionDispensation findPrescriptionDispensationByDispensation(Encounter dispensation)
	        throws PharmacyBusinessException;
	
	PrescriptionDispensation findPrescriptionDispensationByFila(Encounter fila) throws PharmacyBusinessException;
	
}
