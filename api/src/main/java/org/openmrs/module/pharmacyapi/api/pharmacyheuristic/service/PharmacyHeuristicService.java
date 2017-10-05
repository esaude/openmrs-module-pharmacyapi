package org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service;

import java.util.List;

import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.dao.PharmacyHeuristicDAO;

public interface PharmacyHeuristicService extends OpenmrsService {
	
	void setPharmacyHeuristicDAO(PharmacyHeuristicDAO pharmacyHeuristicDAO);
	
	EncounterType getEncounterTypeByPatientAge(Patient patient);
	
	Drug findDrugByOrderUuid(String uuid);
	
	Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order);
	
	List<Obs> findObsByOrder(Order order);
	
}
