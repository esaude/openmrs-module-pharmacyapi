/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dispensation.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.EncounterType;
import org.openmrs.Patient;

/**
 *
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findNotDispensedDrugOrdersByPatient(Patient patient, EncounterType encounterType);
	
	List<DrugOrder> findDispensedDrugOrdersByPatient(Patient patient);
	
}
