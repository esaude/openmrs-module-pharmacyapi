/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationDAO {
	
	public interface QUERY_NAME {
		
		String findByUuid = "PrescriptionDispensation.findByUuid";
		
		String findByPrescription = "PrescriptionDispensation.findByPrescription";
		
		String findByPatientUuid = "PrescriptionDispensation.findByPatientUuid";
	}
	
	public interface QUERY {
		
		String findByUuid = "select pd from PrescriptionDispensation pd where pd.uuid = :uuid";
		
		String findByPrescription = "select pd from PrescriptionDispensation pd where pd.prescription = :prescription";
		
		String findByPatientUuid = "select pd from PrescriptionDispensation pd join fetch pd.patient patient where patient.uuid = :uuid";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation);
	
	PrescriptionDispensation findByUuid(String uuid);
	
	List<PrescriptionDispensation> findByPrescription(Encounter prescription);
	
	List<PrescriptionDispensation> findByPatientUuid(String patientUuid);
}
