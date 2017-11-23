/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.pharmacyheuristic.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.DateUtils;

public class PharmacyHeuristicDAOImpl implements PharmacyHeuristicDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Drug findDrugByOrderUuid(final String uuid) {
		
		final String Sql = "select drug.* from drug join drug_order on drug_order.drug_inventory_id = drug.drug_id join orders on orders.order_id = drug_order.order_id where orders.uuid = :orderuuid";
		final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(Sql).addEntity(Drug.class)
		        .setParameter("orderuuid", uuid);
		
		return (Drug) query.uniqueResult();
	}
	
	@Override
	public Encounter findEncounterByPatientAndEncounterTypeAndOrder(final Patient patient,
	        final EncounterType encounterType, final Order order) {
		
		final String hql = "select distinct(o.encounter) from Obs o where o.order = :order and o.encounter.encounterType = :encounterType and o.encounter.patient = :patient and o.voided is false";
		final Query query = this.sessionFactory.getCurrentSession().createQuery(hql).setParameter("patient", patient)
		        .setParameter("encounterType", encounterType).setParameter("order", order);
		
		return (Encounter) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Encounter findLastEncounterByPatientAndEncounterTypeAndLocationAndDateAndStatus(final Patient patient,
	        final EncounterType encounterType, final Location location, final Date encounterDateTime,
	        final boolean status) throws PharmacyBusinessException {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Encounter.class, "enc");
		searchCriteria.add(Restrictions.eq("enc.patient", patient));
		searchCriteria.add(Restrictions.eq("enc.encounterType", encounterType));
		searchCriteria.add(Restrictions.eq("enc.location", location));
		searchCriteria.add(Restrictions.ge("enc.encounterDatetime", DateUtils.lowDateTime(encounterDateTime)));
		searchCriteria.add(Restrictions.le("enc.encounterDatetime", DateUtils.highDateTime(encounterDateTime)));
		searchCriteria.add(Restrictions.eq("enc.voided", status));
		searchCriteria.addOrder(org.hibernate.criterion.Order.desc("encounterDatetime"));
		searchCriteria.addOrder(org.hibernate.criterion.Order.desc("encounterId"));
		
		final List<Encounter> result = searchCriteria.list();
		if (result.isEmpty()) {
			throw new PharmacyBusinessException("pharmacyapi.error.encounter.not.found.for.patient",
			        patient.getGivenName());
		}
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Visit findLastVisitByPatientAndDateAndState(final Patient patient, final Date date, final boolean voided)
	        throws PharmacyBusinessException {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Visit.class, "visit");
		searchCriteria.add(Restrictions.eq("visit.patient", patient));
		searchCriteria.add(Restrictions.eq("visit.voided", voided));
		searchCriteria.add(Restrictions.ge("visit.startDatetime", DateUtils.lowDateTime(date)));
		searchCriteria.add(Restrictions.le("visit.startDatetime", DateUtils.highDateTime(date)));
		searchCriteria.addOrder(org.hibernate.criterion.Order.desc("visit.startDatetime"));
		searchCriteria.addOrder(org.hibernate.criterion.Order.desc("visit.visitId"));
		
		final List<Visit> result = searchCriteria.list();
		if (result.isEmpty()) {
			throw new PharmacyBusinessException("pharmacyapi.error.visit.not.found.for.patient",
			        patient.getGivenName());
		}
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Obs> findObservationsByOrder(final Order order, final boolean voided) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Obs.class, "obs");
		searchCriteria.add(Restrictions.eq("obs.order", order));
		searchCriteria.add(Restrictions.eq("obs.voided", voided));
		
		return searchCriteria.list();
	}
	
	@Override
	public void updateOrder(final Order order, final Concept orderReason) {
		
		final SQLQuery sqlQuery = this.sessionFactory.getCurrentSession()
		        .createSQLQuery("update orders set order_reason = :orderReason where order_id = :orderId");
		sqlQuery.setParameter("orderReason", orderReason.getConceptId());
		sqlQuery.setParameter("orderId", order.getOrderId());
		sqlQuery.executeUpdate();
	}
}
