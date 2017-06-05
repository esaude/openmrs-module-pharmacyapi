/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Patient;

/**
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findLastDrugOrdersByLastPatientEncounter(final Patient patient);
	
	List<DrugOrder> findDrugOrderByEncounter(final Encounter encounter);
	
	void updateDrugOrderSetQuantity(DrugOrder drugOrder);
	
	void updateDrugOrderSetQuantityAndDispenseAsWritten(DrugOrder drugOrder);
}
