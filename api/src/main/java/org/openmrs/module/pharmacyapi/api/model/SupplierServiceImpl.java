/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacyapi.api.model.stock.Supplier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guimino Neves
 */
@Transactional
public class SupplierServiceImpl extends BaseOpenmrsService implements SupplierService {
	
	private DbSessionFactory sessionFactory;
	
	@Override
	public Supplier create(final Supplier supplier) throws PharmacyBusinessException {
		
		// TODO: call ComponetValidator Here for Business/Rules validations
		
		this.sessionFactory.getCurrentSession().save(supplier);
		return supplier;
	}
	
	@Override
	public void setSessionFactory(final DbSessionFactory dbSessionFactory) {
		
		this.sessionFactory = dbSessionFactory;
	}
}
