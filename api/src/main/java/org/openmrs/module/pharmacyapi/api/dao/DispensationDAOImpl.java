/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
import org.openmrs.EncounterType;
import org.openmrs.Patient;

/**
 */
public class DispensationDAOImpl implements DispensationDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugOrder> findNotDispensedDrugOrdersByPatient(Patient patient, EncounterType encounterType) {
		
		Query query = this.sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "select distinct o from DrugOrder o join fetch o.encounter enc where o.patient = :patient "
		                    + " and o.voided is false "
		                    + " and o.drug not in "
		                    + " (select distinct dispensedOrder.drug from PrescriptionDispensation pd, DrugOrder dispensedOrder "
		                    + "   where pd.dispensation = dispensedOrder.encounter and dispensedOrder.patient = :patient and dispensedOrder.encounter.encounterType <> :encounterType and pd.retired is false and dispensedOrder.voided is false ) and o.dateStopped is null");
		
		return query.setParameter("patient", patient).setParameter("encounterType", encounterType).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugOrder> findDispensedDrugOrdersByPatient(Patient patient) {
		
		String hql = "select distinct o from DrugOrder o where o.patient = :patient and o.voided is false "
		        + " and o.orderId = ( select max(dispensationDrugOrder.orderId) "
		        + "	from PrescriptionDispensation pd "
		        + " join pd.dispensation dispensation, DrugOrder dispensationDrugOrder where dispensationDrugOrder.encounter = dispensation and dispensationDrugOrder.drug = o.drug and pd.retired is false and dispensationDrugOrder.voided is false) ";
		
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.setParameter("patient", patient).list();
	}
}
