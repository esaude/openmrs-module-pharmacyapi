/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.prescription.service;

import java.util.List;

import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;

/**
 * @author Stélio Moiane
 */
public interface PrescriptionService extends OpenmrsService {
	
	void setConceptService(final ConceptService conceptService) throws APIException;
	
	void setDispensationDAO(DispensationDAO dispensationDAO);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
	
	void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService);
	
	Prescription createPrescription(Prescription prescription) throws PharmacyBusinessException;
	
	List<Prescription> findAllPrescriptionsByPatient(final Patient patient);
	
	List<Prescription> findPrescriptionsByPatientAndActiveStatus(final Patient patient);
	
	EncounterType getEncounterTypeByPatientAge(Patient patient);
	
	void cancelPrescriptionItem(PrescriptionItem prescriptionItem, String cancelationReason)
	        throws PharmacyBusinessException;
	
}
