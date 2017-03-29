/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacyapi.api.model.stock.Supplier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guimino Neves
 */
@Transactional
public interface SupplierService extends OpenmrsService {
	
	Supplier create(Supplier supplier) throws PharmacyBusinessException;
	
	void setSessionFactory(final DbSessionFactory dbSessionFactory);
}
