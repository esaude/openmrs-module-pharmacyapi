/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationDAO {
	
	public interface QUERY_NAME {
		
		String findByUuid = "PrescriptionDispensation.findByUuid";
		
	}
	
	public interface QUERY {
		
		String findByUuid = "select pd from PrescriptionDispensation pd where pd.uuid = :uuid";
		
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation);
	
	PrescriptionDispensation findByUuid(String uuid);
	
}
