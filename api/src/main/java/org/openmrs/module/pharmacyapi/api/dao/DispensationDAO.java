/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;

/**
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findLastDrugOrdersByLastPatientEncounter(final Patient patient);
	
	List<DrugOrder> findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(Patient patient);
	
	Drug findDrugByOrderUuid(String uuid);
	
	void updateDrugOrder(DrugOrder drugOrder);
}
