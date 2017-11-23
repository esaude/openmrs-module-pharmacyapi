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

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;

/**
 * Wrapper used as a workaround to a problem we are having with the DrugResource rest
 */
public interface DrugWrapperService extends OpenmrsService {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<Drug> findDrugsByNameLike(String phrase);
}
