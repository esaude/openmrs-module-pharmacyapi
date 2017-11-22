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
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.common.util.MappedForms;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.dao.PharmacyHeuristicDAO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PharmacyHeuristicServiceImpl extends BaseOpenmrsService implements PharmacyHeuristicService {
	
	private PharmacyHeuristicDAO pharmacyHeuristicDAO;
	
	@Override
	public void setPharmacyHeuristicDAO(final PharmacyHeuristicDAO pharmacyHeuristicDAO) {
		this.pharmacyHeuristicDAO = pharmacyHeuristicDAO;
	}
	
	@Override
	public EncounterType getEncounterTypeByPatientAge(final Patient patient) {
		
		if (patient != null) {
			
			return Context.getEncounterService().getEncounterTypeByUuid(patient.getAge() < 15
			        ? MappedEncounters.ARV_FOLLOW_UP_CHILD : MappedEncounters.ARV_FOLLOW_UP_ADULT);
		}
		
		throw new APIException("Cannot find encounterType for non given patient");
	}
	
	@Override
	public Drug findDrugByOrderUuid(final String uuid) {
		return this.pharmacyHeuristicDAO.findDrugByOrderUuid(uuid);
	}
	
	@Override
	public Encounter findEncounterByPatientAndEncounterTypeAndOrder(final Patient patient,
	        final EncounterType encounterType, final Order order) {
		return this.pharmacyHeuristicDAO.findEncounterByPatientAndEncounterTypeAndOrder(patient, encounterType, order);
	}
	
	@Override
	public Encounter findLastEncounterByPatientAndEncounterTypeAndLocationAndDate(final Patient patient,
	        final EncounterType encounterType, final Location location, final Date encounterDateTime)
	        throws PharmacyBusinessException {
		return this.pharmacyHeuristicDAO.findLastEncounterByPatientAndEncounterTypeAndLocationAndDateAndStatus(patient,
		    encounterType, location, encounterDateTime, false);
	}
	
	@Override
	public Form getFormByPatientAge(final Patient patient) throws PharmacyBusinessException {
		
		if (patient != null) {
			
			final Patient patientWithAge = Context.getPatientService().getPatient(patient.getId());
			return Context.getFormService().getFormByUuid(
			    patientWithAge.getAge() < 15 ? MappedForms.PEDIATRICS_FOLLOW_UP : MappedForms.ADULT_FOLLOW_UP);
			
		}
		throw new PharmacyBusinessException("Cannot find Form for non given patient");
	}
	
	@Override
	public Visit findLastVisitByPatientAndEncounterDate(final Patient patient, final Date encounterDate)
	        throws PharmacyBusinessException {
		
		return this.pharmacyHeuristicDAO.findLastVisitByPatientAndDateAndState(patient, encounterDate, false);
	}
	
	@Override
	public List<Obs> findObservationsByOrder(final Order order) {
		return this.pharmacyHeuristicDAO.findObservationsByOrder(order, false);
	}
	
	@Override
	public void updateOrder(final Order order, final Concept orderReason) {
		this.pharmacyHeuristicDAO.updateOrder(order, orderReason);
	}
}
