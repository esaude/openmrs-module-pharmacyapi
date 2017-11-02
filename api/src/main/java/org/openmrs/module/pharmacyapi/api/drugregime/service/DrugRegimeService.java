/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.drugregime.service;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.drugregime.dao.DrugRegimeDAO;
import org.openmrs.module.pharmacyapi.api.drugregime.model.DrugRegime;

/**
 *
 */
public interface DrugRegimeService extends OpenmrsService {
	
	void setDrugRegimeDAO(DrugRegimeDAO drugRegimeDAO);
	
	List<DrugRegime> findDrugRegimesByRegime(Concept regime);
	
	List<DrugRegime> findAllDrugRegimes(Boolean retired);
	
	DrugRegime findDrugRegimeByUuid(String uuid);
	
	DrugRegime findDrugRegimeByRegimeAndDrugItem(Concept regime, DrugItem drugItem) throws PharmacyBusinessException;
	
	List<DrugRegime> findDrugRegimeByDrugUuid(String drugUuid);
	
	List<Drug> findArvDrugs();
	
}
