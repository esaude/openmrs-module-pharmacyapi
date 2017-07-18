/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;

/**
 * @author Stélio Moiane
 */
public interface PrescriptionService extends OpenmrsService {
	
	List<Prescription> findPrescriptionsByPatient(final Patient patient) throws PharmacyBusinessException;
	
	void setConceptService(final ConceptService conceptService) throws APIException;
	
	Double calculateDrugPikckedUp(final DrugOrder order) throws APIException;
	
	void setDispensationDAO(DispensationDAO dispensationDAO);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
	
	void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService);
	
	Prescription createPrescription(Prescription prescription) throws PharmacyBusinessException;
}
