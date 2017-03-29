/**
 *
 */
package org.openmrs.module.pharmacyapi.api.service.stock;

import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.model.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.stock.DrugMeasureUnit;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guimino Neves
 */
@Transactional
public class DrugMeasureUnitServiceImpl extends BaseOpenmrsService implements DrugMeasureUnitService {
	
	private DbSessionFactory sessionFactory;
	
	@Override
	public DrugMeasureUnit create(final DrugMeasureUnit drugMeasureUnit) throws PharmacyBusinessException {
		
		// TODO: call ComponetValidator Here for Business/Rules validations
		
		this.sessionFactory.getCurrentSession().save(drugMeasureUnit);
		
		return drugMeasureUnit;
	}
	
	@Override
	public void setSessionFactory(final DbSessionFactory dbSessionFactory) {
		
		this.sessionFactory = dbSessionFactory;
	}
}
