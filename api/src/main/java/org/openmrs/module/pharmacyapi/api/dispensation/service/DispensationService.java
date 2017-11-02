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
package org.openmrs.module.pharmacyapi.api.dispensation.service;

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public interface DispensationService extends OpenmrsService {
	
	Dispensation dispense(final Dispensation dispensation) throws PharmacyBusinessException;
	
	void setProviderService(final ProviderService providerService);
	
	void setOrderService(final OrderService orderService);
	
	void setPatientService(final PatientService patientService);
	
	void setEncounterService(final EncounterService encounterService);
	
	void setLocationService(final LocationService locationService);
	
	void setConceptService(final ConceptService conceptService);
	
	void setPersonService(final PersonService personService);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
	
	void setDispensationDAO(DispensationDAO dispensationDAO);
	
	void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService);
	
	void setPharmacyHeuristicService(PharmacyHeuristicService pharmacyHeuristicService);
	
	void cancelDispensationItems(Dispensation dispensation, String cancelationReason) throws PharmacyBusinessException;
	
	List<Dispensation> findFilaDispensationByPatientAndDateInterval(Patient patient, Date startDate, Date endDate)
	        throws PharmacyBusinessException;
	
}
