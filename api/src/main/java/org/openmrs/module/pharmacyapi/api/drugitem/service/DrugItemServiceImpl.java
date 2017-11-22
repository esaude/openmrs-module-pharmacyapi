/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.drugitem.service;

import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.drugitem.dao.DrugItemDAO;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 */

@Transactional
public class DrugItemServiceImpl extends BaseOpenmrsService implements DrugItemService {
	
	private DrugItemDAO drugItemDAO;
	
	@Override
	public void setDrugItemDAO(final DrugItemDAO drugItemDAO) {
		this.drugItemDAO = drugItemDAO;
	}
	
	@Override
	public DrugItem findDrugItemByUuid(final String uuid) {
		return this.drugItemDAO.findByUuid(uuid);
	}
	
	@Override
	public List<DrugItem> findAllDrugItem(final Boolean retired) {
		return this.drugItemDAO.findAll(retired);
	}
	
	@Override
	public DrugItem findDrugItemByDrugId(final Integer drugId) throws PharmacyBusinessException {
		return this.drugItemDAO.findByDrugId(drugId);
	}
}
