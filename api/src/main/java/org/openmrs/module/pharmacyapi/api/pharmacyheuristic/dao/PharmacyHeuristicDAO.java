/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.pharmacyheuristic.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;

public interface PharmacyHeuristicDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	Drug findDrugByOrderUuid(String uuid);
	
	Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order);
	
	Encounter findLastEncounterByPatientAndEncounterTypeAndLocationAndDateAndStatus(final Patient patient,
	        final EncounterType encounterType, final Location location, final Date encounterDateTime, boolean status)
	        throws PharmacyBusinessException;
	
	Visit findLastVisitByPatientAndDateAndState(Patient patient, Date date, boolean voided)
	        throws PharmacyBusinessException;
	
	List<Obs> findObservationsByOrder(final Order order, boolean voided);
	
	void updateOrder(Order order, Concept orderReason);
	
	Order findOrderByPreviousOrder(Order previousOrder);
}
