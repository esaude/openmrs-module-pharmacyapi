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
package org.openmrs.module.pharmacyapi.api.dispensation.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.Order.Action;
import org.openmrs.module.pharmacyapi.api.common.util.DateUtils;

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
		            "select o from DrugOrder o where o.patient = :patient and o.encounter.encounterType = :encounterType and o.voided is false and o.dateStopped is null and o.action <> 'DISCONTINUE' ");
		
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Encounter> findEncountersByPatientAndEncounterTypeAndDateInterval(Patient patient,
	        EncounterType encounterType, Date startDate, Date endDate) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Encounter.class, "encounter");
		searchCriteria.add(Restrictions.eq("encounter.patient", patient));
		searchCriteria.add(Restrictions.eq("encounter.encounterType", encounterType));
		searchCriteria.add(Restrictions.eq("encounter.voided", false));
		searchCriteria.add(Restrictions.between("encounter.encounterDatetime", DateUtils.lowDateTime(startDate),
		    DateUtils.highDateTime(endDate)));
		
		return searchCriteria.list();
	}
	
	@Override
	public DrugOrder findDrugOrderByOrderUuid(String orderUuid) {
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DrugOrder.class, "drugOrder");
		searchCriteria.add(Restrictions.eq("drugOrder.uuid", orderUuid));
		
		return (DrugOrder) searchCriteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugOrder> findDrugOrderByEncounterAndOrderActionAndVoided(Encounter encounter, Action orderAction,
	        boolean voided) {
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DrugOrder.class, "drugOrder");
		searchCriteria.add(Restrictions.eq("drugOrder.encounter", encounter));
		searchCriteria.add(Restrictions.eq("drugOrder.action", orderAction));
		searchCriteria.add(Restrictions.eq("drugOrder.voided", voided));
		
		return searchCriteria.list();
	}
	
}
