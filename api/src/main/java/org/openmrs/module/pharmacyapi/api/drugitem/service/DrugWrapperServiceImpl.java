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
package org.openmrs.module.pharmacyapi.api.drugitem.service;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public class DrugWrapperServiceImpl extends BaseOpenmrsService implements DrugWrapperService {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Drug> findDrugsByNameLike(String phrase) {
		
		if (phrase == null || phrase.length() < 3) {
			return Collections.emptyList();
		}
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Drug.class, "drug");
		searchCriteria.add(Restrictions.eq("drug.retired", false));
		searchCriteria.add(Restrictions.like("drug.name", phrase, MatchMode.START));
		
		return searchCriteria.list();
	}
}
