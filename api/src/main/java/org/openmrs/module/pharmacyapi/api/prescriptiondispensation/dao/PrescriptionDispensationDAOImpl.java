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
package org.openmrs.module.pharmacyapi.api.prescriptiondispensation.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;

/**
 *
 */
public class PrescriptionDispensationDAOImpl implements PrescriptionDispensationDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public PrescriptionDispensation save(final PrescriptionDispensation prescriptionDispensation) {
		
		this.sessionFactory.getCurrentSession().save(prescriptionDispensation);
		return prescriptionDispensation;
	}
	
	@Override
	public PrescriptionDispensation findByUuid(final String uuid) {
		final Query query = this.sessionFactory.getCurrentSession()
		        .getNamedQuery(PrescriptionDispensationDAO.QUERY_NAME.findByUuid).setParameter("uuid", uuid);
		
		return (PrescriptionDispensation) query.uniqueResult();
	}
	
	@Override
	public PrescriptionDispensation findByDispensationEncounter(final Encounter dispensation)
	        throws PharmacyBusinessException {
		
		final Query query = this.sessionFactory.getCurrentSession()
		        .getNamedQuery(PrescriptionDispensationDAO.QUERY_NAME.findByDispensationEncounter)
		        .setParameter("dispensation", dispensation);
		
		final PrescriptionDispensation prescriptionDispensation = (PrescriptionDispensation) query.uniqueResult();
		if (prescriptionDispensation != null) {
			
			return prescriptionDispensation;
		}
		
		throw new PharmacyBusinessException(
		        "Entity PrescriptionDispensation not Found for dispensation " + dispensation);
	}
	
	@Override
	public void retire(final PrescriptionDispensation prescriptionDispensation) {
		
		this.sessionFactory.getCurrentSession().saveOrUpdate(prescriptionDispensation);
	}
	
	@Override
	public void update(final PrescriptionDispensation prescriptionDispensation) {
		
		this.sessionFactory.getCurrentSession().update(prescriptionDispensation);
	}
	
	@Override
	public PrescriptionDispensation findByFila(final Encounter fila) throws PharmacyBusinessException {
		
		final Query query = this.sessionFactory.getCurrentSession()
		        .getNamedQuery(PrescriptionDispensationDAO.QUERY_NAME.findByFila).setParameter("fila", fila);
		
		final PrescriptionDispensation uniqueResult = (org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation) query
		        .uniqueResult();
		
		if (uniqueResult == null) {
			throw new PharmacyBusinessException(
			        "Entity PrescriptionDispensation not found for parameter fila = " + fila);
		}
		return uniqueResult;
	}
	
}
