/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DrugRegimeDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.model.DrugRegime;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public class DrugRegimeServiceImpl extends BaseOpenmrsService implements DrugRegimeService {
	
	private DrugRegimeDAO drugRegimeDAO;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public List<DrugRegime> findDrugRegimesByRegime(final Concept regime) {
		
		return this.drugRegimeDAO.findByRegime(regime, false);
	}
	
	@Override
	public void setDrugRegimeDAO(final DrugRegimeDAO drugRegimeDAO) {
		this.drugRegimeDAO = drugRegimeDAO;
	}
	
	@Override
	public List<DrugRegime> findAllDrugRegimes(final Boolean retired) {
		return this.drugRegimeDAO.findAll(retired);
	}
	
	@Override
	public DrugRegime findDrugRegimeByUuid(final String uuid) {
		
		return this.drugRegimeDAO.findByUuid(uuid);
	}
	
	@Override
	public DrugRegime findDrugRegimeByRegimeAndDrugItem(final Concept regime, final DrugItem drugItem)
	        throws PharmacyBusinessException {
		
		return this.drugRegimeDAO.findByRegimeAndDrugItem(regime, drugItem);
	}
	
	@Override
	public List<Drug> findArvDrugs() {

		final List<Drug> drugs = new ArrayList<>();
		final List<DrugRegime> drugRegimes = this.drugRegimeDAO.findAll(false);

		// TODO: Add Lambdaj dependency to do this job
		for (final DrugRegime drugRegime : drugRegimes) {

			drugs.add(drugRegime.getDrugItem().getDrug());
		}

		return drugs;
	}
	
	@Override
	public List<DrugRegime> findDrugRegimeByDrugUuid(final String drugUuid) {
		
		return this.drugRegimeDAO.findByDrugUuid(drugUuid);
	}
}
