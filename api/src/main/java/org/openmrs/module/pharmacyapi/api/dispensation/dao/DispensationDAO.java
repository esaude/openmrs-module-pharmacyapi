/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dispensation.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order.Action;
import org.openmrs.Patient;

/**
 *
 */
public interface DispensationDAO {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugOrder> findNotDispensedDrugOrdersByPatient(Patient patient, EncounterType encounterType);
	
	List<DrugOrder> findDispensedDrugOrdersByPatient(Patient patient);
	
	List<Encounter> findEncountersByPatientAndEncounterTypeAndDateInterval(Patient patient, EncounterType encounterType,
	        Date startDate, Date endDate);
	
	DrugOrder findDrugOrderByOrderUuid(String orderUuid);
	
	List<DrugOrder> findDrugOrderByEncounterAndOrderActionAndVoided(Encounter encounter, Action orderAction, boolean voided);
	
}
