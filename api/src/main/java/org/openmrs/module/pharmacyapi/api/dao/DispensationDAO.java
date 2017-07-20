/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;

/**
 * s
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(Patient patient);
	
	List<Encounter> findEncountersByPatientAndEnconterType(Patient patient, EncounterType encounterType);
	
	Drug findDrugByOrderUuid(String uuid);
}
