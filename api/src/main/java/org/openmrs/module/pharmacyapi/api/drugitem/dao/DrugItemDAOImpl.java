/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 *
 */
package org.openmrs.module.pharmacyapi.api.drugitem.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.pharmacyapi.api.common.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;

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
	public DrugItem findByDrugId(Integer drugId) throws EntityNotFoundException {
		
		final Query query = this.sessionFactory.getCurrentSession().getNamedQuery(DrugItemDAO.QUERY_NAME.findByDrugId)
		        .setParameter("drugId", drugId);
		
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
