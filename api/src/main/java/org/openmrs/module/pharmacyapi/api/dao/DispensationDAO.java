/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;

/**
 * @author Guimino Neves
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findLastDrugOrdersByLastPatientEncounter(final Patient patient);
	
	void updateDrugOrder(DrugOrder drugOrder);
}
