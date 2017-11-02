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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;

public class PharmacyHeuristicDAOImpl implements PharmacyHeuristicDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Drug findDrugByOrderUuid(String uuid) {
		
		String Sql = "select drug.* from drug join drug_order on drug_order.drug_inventory_id = drug.drug_id join orders on orders.order_id = drug_order.order_id where orders.uuid = :orderuuid";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(Sql).addEntity(Drug.class)
		        .setParameter("orderuuid", uuid);
		
		return (Drug) query.uniqueResult();
	}
	
	@Override
	public Encounter findEncounterByPatientAndEncounterTypeAndOrder(Patient patient, EncounterType encounterType, Order order) {
		
		String hql = "select distinct(o.encounter) from Obs o where o.order = :order and o.encounter.encounterType = :encounterType and o.encounter.patient = :patient and o.voided is false";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql).setParameter("patient", patient)
		        .setParameter("encounterType", encounterType).setParameter("order", order);
		
		return (Encounter) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Obs> findObsByOrder(Order order) {
		
		Query query = this.sessionFactory.getCurrentSession()
		        .createQuery("select distinct o from Obs o where o.order = :order").setParameter("order", order);
		return query.list();
	}
}
