/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationDAO {
	
	public interface QUERY_NAME {
		
		String findByUuid = "PrescriptionDispensation.findByUuid";
		
		String findByPrescription = "PrescriptionDispensation.findByPrescription";
		
		String findByPatientUuid = "PrescriptionDispensation.findByPatientUuid";
		
		String findByDispensationEncounter = "PrescriptionDispensation.findByDispensationEncounter";
		
		String findLastByPrescription = "PrescriptionDispensation.findLastByPrescription";
	}
	
	public interface QUERY {
		
		String findByUuid = "select pd from PrescriptionDispensation pd where pd.uuid = :uuid";
		
		String findByPrescription = "select pd from PrescriptionDispensation pd where pd.prescription = :prescription and pd.retired is false and pd.dispensation.voided is false";
		
		String findByPatientUuid = "select pd from PrescriptionDispensation pd join fetch pd.patient patient where patient.uuid = :uuid";
		
		String findByDispensationEncounter = "select pd from PrescriptionDispensation pd where pd.dispensation = :dispensation";
		
		String findLastByPrescription = "select pd from PrescriptionDispensation pd where pd.prescription = :prescription and pd.retired is false and pd.prescriptionDispensationId = (select max(pdSub.prescriptionDispensationId) from PrescriptionDispensation pdSub where pd.prescription = pdSub.prescription and pdSub.retired is false) ";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation);
	
	void retire(PrescriptionDispensation prescriptionDispensation);
	
	PrescriptionDispensation findByUuid(String uuid);
	
	List<PrescriptionDispensation> findByPrescription(Encounter prescription);
	
	PrescriptionDispensation findByDispensationEncounter(Encounter dispensation) throws PharmacyBusinessException;
	
	List<PrescriptionDispensation> findByPatientUuid(String patientUuid);
	
	PrescriptionDispensation findLastByPrescription(Encounter prescription);
	
	Drug findDrugByOrderUuid(String uuid);
	
	Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order);
	
	List<Obs> findObsByOrder(Order order);
}
