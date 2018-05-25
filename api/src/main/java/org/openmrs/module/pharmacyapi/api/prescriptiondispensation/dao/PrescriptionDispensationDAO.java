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

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;

/**
 *
 */
public interface PrescriptionDispensationDAO {
	
	public interface QUERY_NAME {
		
		String findByUuid = "PrescriptionDispensation.findByUuid";
		
		String findByPrescription = "PrescriptionDispensation.findByPrescription";
		
		String findByPatientUuid = "PrescriptionDispensation.findByPatientUuid";
		
		String findByDispensationEncounter = "PrescriptionDispensation.findByDispensationEncounter";
		
		String findLastByPrescription = "PrescriptionDispensation.findLastByPrescription";
		
		String findByFila = "PrescriptionDispensation.findByFila";
	}
	
	public interface QUERY {
		
		String findByUuid = "select pd from PrescriptionDispensation pd where pd.uuid = :uuid";
		
		String findByPrescription = "select pd from PrescriptionDispensation pd where pd.prescription = :prescription and pd.retired = :retired";
		
		String findByPatientUuid = "select pd from PrescriptionDispensation pd join fetch pd.patient patient where patient.uuid = :uuid";
		
		String findByDispensationEncounter = "select pd from PrescriptionDispensation pd where pd.dispensation = :dispensation";
		
		String findLastByPrescription = "select pd from PrescriptionDispensation pd where pd.prescription = :prescription and pd.retired is false and pd.prescriptionDispensationId = (select max(pdSub.prescriptionDispensationId) from PrescriptionDispensation pdSub where pd.prescription = pdSub.prescription and pdSub.retired is false) ";
		
		String findByFila = "select pd from PrescriptionDispensation pd where pd.fila = :fila";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation);
	
	void update(PrescriptionDispensation prescriptionDispensation);
	
	void retire(PrescriptionDispensation prescriptionDispensation);
	
	PrescriptionDispensation findByUuid(String uuid);
	
	PrescriptionDispensation findByDispensationEncounter(Encounter dispensation) throws PharmacyBusinessException;
	
	PrescriptionDispensation findByFila(Encounter fila) throws PharmacyBusinessException;
	
	List<PrescriptionDispensation> findByPrescription(Encounter prescription, boolean retired);
	
}
