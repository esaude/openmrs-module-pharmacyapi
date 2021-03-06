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
package org.openmrs.module.pharmacyapi.api.drugregime.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.module.pharmacyapi.api.common.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.drugregime.model.DrugRegime;

/**
 *
 */
public interface DrugRegimeDAO {
	
	public interface QUERY_NAME {
		
		String findByRegime = "DrugRegime.findByRegime";
		
		String findByRegimeAndDrugItem = "DrugRegime.findByRegimeAndDrugItem";
		
		String findByDrugUuid = "DrugRegime.findByDrugUuid";
	}
	
	public interface QUERY {
		
		String findByRegime = "select distinct drugRegime from DrugRegime drugRegime join fetch drugRegime.drugItem join fetch drugRegime.regime where drugRegime.regime =:regime and drugRegime.retired = :retired";
		
		String findByRegimeAndDrugItem = "select distinct drugRegime from DrugRegime drugRegime where drugRegime.regime = :regime and drugRegime.drugItem = :drugItem";
		
		String findByDrugUuid = "select distinct drugRegime from DrugRegime drugRegime where drugRegime.drugItem.drug.uuid =:drugUuid";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugRegime> findByRegime(Concept regime, boolean retired);
	
	List<DrugRegime> findAll(boolean retired);
	
	DrugRegime findByUuid(String uuid);
	
	DrugRegime findByRegimeAndDrugItem(Concept regime, DrugItem drugItem) throws EntityNotFoundException;
	
	List<DrugRegime> findByDrugUuid(String drugUuid);
	
}
