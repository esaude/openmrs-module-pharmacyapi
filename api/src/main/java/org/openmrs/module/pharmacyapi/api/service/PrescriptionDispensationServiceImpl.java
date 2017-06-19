/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public class PrescriptionDispensationServiceImpl extends BaseOpenmrsService implements PrescriptionDispensationService {
	
	private PrescriptionDispensationDAO prescriptionDispensationDAO;
	
	@Override
	public void setPrescriptionDispensationDAO(PrescriptionDispensationDAO prescriptionDispensationDAO) {
		
		this.prescriptionDispensationDAO = prescriptionDispensationDAO;
	}
	
	@Override
	public PrescriptionDispensation savePrescriptionDispensation(Patient patient, Encounter prescription,
	        Encounter dispensation) {
		
		PrescriptionDispensation prescriptionDispensation = new PrescriptionDispensation(patient, prescription, dispensation);
		
		// TODO: Should creates a Component validator to move the rules
		// validator a put the mensagem validator in a resource bundle
		if (patient == null || prescription == null || dispensation == null) {
			throw new IllegalArgumentException("The arguments passed for dispensation is not valid ");
		}
		
		if (!patient.getUuid().equals(prescription.getPatient().getUuid())
		        || !patient.getUuid().equals(dispensation.getPatient().getUuid())) {
			throw new IllegalArgumentException(
			        "The Patient passed  on the argument must be the same with the prescription and dispensation patient");
		}
		
		this.prescriptionDispensationDAO.save(prescriptionDispensation);
		
		return prescriptionDispensation;
	}
}
