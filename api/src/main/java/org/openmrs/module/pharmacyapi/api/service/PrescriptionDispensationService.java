/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.List;

import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;

/**
 *
 */
public interface PrescriptionDispensationService extends OpenmrsService {
	
	void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO);
	
	PrescriptionDispensation savePrescriptionDispensation(Patient patient, Encounter prescription, Encounter dispensation);
	
	void retire(User user, PrescriptionDispensation prescriptionDispensation, String reason)
	        throws PharmacyBusinessException;
	
	public boolean isArvDrug(final PrescriptionItem prescription, final DrugOrder drugOrder)
	        throws PharmacyBusinessException;
	
	PrescriptionDispensation findPrescriptionDispensationByDispensation(Encounter dispensation)
	        throws PharmacyBusinessException;
	
	Drug findDrugByOrderUuid(String uuid);
	
	Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order);
	
	List<Obs> findObsByOrder(Order order);
}
