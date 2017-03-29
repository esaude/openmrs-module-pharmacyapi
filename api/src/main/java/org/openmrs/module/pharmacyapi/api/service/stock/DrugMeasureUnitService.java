/**
 *
 */
package org.openmrs.module.pharmacyapi.api.service.stock;

import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacyapi.api.model.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.stock.DrugMeasureUnit;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guimino Neves
 */
@Transactional
public interface DrugMeasureUnitService extends OpenmrsService {
	
	DrugMeasureUnit create(DrugMeasureUnit drugMeasureUnit) throws PharmacyBusinessException;
	
	void setSessionFactory(final DbSessionFactory dbSessionFactory);
}
