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
package org.openmrs.module.pharmacyapi.api.dispensation.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order.Action;
import org.openmrs.Patient;

/**
 *
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findNotDispensedDrugOrdersByPatient(Patient patient, EncounterType... encounterType);
	
	List<DrugOrder> findDispensedDrugOrdersByPatient(Patient patient);
	
	List<Encounter> findEncountersByPatientAndEncounterTypeAndDateInterval(Patient patient, EncounterType encounterType,
	        Date startDate, Date endDate);
	
	DrugOrder findDrugOrderByOrderUuid(String orderUuid);
	
	List<DrugOrder> findDrugOrderByEncounterAndOrderActionAndVoided(Encounter encounter, Action orderAction,
	        boolean voided);
	
}
