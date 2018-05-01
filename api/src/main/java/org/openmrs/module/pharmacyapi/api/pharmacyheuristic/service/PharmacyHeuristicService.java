/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service;

import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.dao.PharmacyHeuristicDAO;

public interface PharmacyHeuristicService extends OpenmrsService {
	
	void setPharmacyHeuristicDAO(PharmacyHeuristicDAO pharmacyHeuristicDAO);
	
	EncounterType getEncounterTypeByPatientAge(Patient patient);
	
	Drug findDrugByOrderUuid(String uuid);
	
	Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order);
	
	Encounter findLastEncounterByPatientAndEncounterTypeAndLocationAndDate(Patient patient, EncounterType encounterType,
	        Location location, Date encounterDateTime) throws PharmacyBusinessException;
	
	Form getFormByPatientAge(Patient patient) throws PharmacyBusinessException;
	
	Visit findLastVisitByPatientAndEncounterDate(final Patient patient, Date encounterDate)
	        throws PharmacyBusinessException;
	
	List<Obs> findObservationsByOrder(Order order);
	
	void updateOrder(Order order, Concept orderReason);
	
	Order findOrderByPreviousOrder(Order previousOrder);
}
