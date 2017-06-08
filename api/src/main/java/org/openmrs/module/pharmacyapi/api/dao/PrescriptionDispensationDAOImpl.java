/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;

/**
 *
 */
public class PrescriptionDispensationDAOImpl implements PrescriptionDispensationDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation) {
		
		this.sessionFactory.getCurrentSession().save(prescriptionDispensation);
		return prescriptionDispensation;
	}
	
	@Override
	public PrescriptionDispensation findByUuid(String uuid) {
		final Query query = this.sessionFactory.getCurrentSession()
		        .getNamedQuery(PrescriptionDispensationDAO.QUERY_NAME.findByUuid).setParameter("uuid", uuid);
		
		return (PrescriptionDispensation) query.uniqueResult();
	}
}
