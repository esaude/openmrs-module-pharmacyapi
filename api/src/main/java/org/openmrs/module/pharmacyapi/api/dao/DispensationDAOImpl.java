/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.DrugOrder;
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
	public List<DrugOrder> findLastDrugOrdersByLastPatientEncounter(final Patient patient) {
		
		final String hql = "select distinct drugOrder from DrugOrder drugOrder " + " join fetch drugOrder.drug drug "
		        + " join fetch drugOrder.encounter encounter " + " join fetch encounter.patient patient "
		        + " where patient = :patient " + " and drugOrder.dispenseAsWritten is false "
		        + " and drugOrder.action not in('DISCONTINUE') "
		        + " and drugOrder.dateStopped is null order by drugOrder.action desc, drugOrder.dateCreated desc";
		
		final Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("patient", patient);
		
		return query.list();
	}
	
	@Override
	public void updateDrugOrder(final DrugOrder drugOrder) {
		
		this.sessionFactory.getCurrentSession().update(drugOrder);
	}
}
