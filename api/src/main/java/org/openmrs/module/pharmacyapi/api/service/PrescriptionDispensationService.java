/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationService extends OpenmrsService {
	
	void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO);
	
	PrescriptionDispensation savePrescriptionDispensation(PrescriptionDispensation prescriptionDispensation);
	
	PrescriptionDispensation findByUuid(String uuid);
}
