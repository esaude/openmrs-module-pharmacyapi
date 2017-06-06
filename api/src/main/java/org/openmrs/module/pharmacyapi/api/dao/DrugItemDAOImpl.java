/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.module.pharmacyapi.api.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;

/**
 *
 */
public class DrugItemDAOImpl implements DrugItemDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public DrugItem findByUuid(final String uuid) {
		final Query query = this.sessionFactory.getCurrentSession().getNamedQuery(DrugItemDAO.QUERY_NAME.findByUUID)
		        .setParameter("uuid", uuid);
		
		return (DrugItem) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugItem> findAll(final boolean retired) {
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DrugItem.class, "drugItem");
		
		if (!retired) {
			searchCriteria.add(Restrictions.eq("drugItem.retired", false));
		}
		return searchCriteria.list();
	}
	
	@Override
	public void save(final DrugItem drugItem) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(drugItem);
	}
	
	@Override
	public DrugItem findByDrug(final Drug drug) throws EntityNotFoundException {
		
		final Query query = this.sessionFactory.getCurrentSession().getNamedQuery(DrugItemDAO.QUERY_NAME.findByDrug)
		        .setParameter("drug", drug);
		
		final DrugItem drugItem = (DrugItem) query.uniqueResult();
		
		if (drugItem == null) {
			throw new EntityNotFoundException(DrugItem.class);
		}
		
		return drugItem;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugItem> findAll(final Boolean retired) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DrugItem.class, "drugItem");
		
		if (!retired) {
			searchCriteria.add(Restrictions.eq("drugItem.retired", false));
		}
		return searchCriteria.list();
		
	}
}
