/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

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
	public PrescriptionDispensation savePrescriptionDispensation(PrescriptionDispensation prescriptionDispensation) {
		
		return this.prescriptionDispensationDAO.save(prescriptionDispensation);
	}
	
	@Override
	public PrescriptionDispensation findByUuid(String uuid) {
		
		return this.prescriptionDispensationDAO.findByUuid(uuid);
	}
}
