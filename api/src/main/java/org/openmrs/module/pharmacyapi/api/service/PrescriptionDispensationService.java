/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionItem;

/**
 *
 */
public interface PrescriptionDispensationService extends OpenmrsService {
	
	void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO);
	
	PrescriptionDispensation savePrescriptionDispensation(Patient patient, Encounter prescription, Encounter dispensation);
	
	public boolean isArvDrug(final PrescriptionItem prescription, final DrugOrder drugOrder)
	        throws PharmacyBusinessException;
	
	PrescriptionDispensation findPrescriptionDispensationByDispensation(Encounter dispensation)
	        throws PharmacyBusinessException;
}
