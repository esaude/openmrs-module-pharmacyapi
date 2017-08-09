/**
 *
 */
package org.openmrs.module.pharmacyapi.api.drugitem.service;

import java.util.List;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.drugitem.dao.DrugItemDAO;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;

/**
 */
public interface DrugItemService extends OpenmrsService {
	
	void setDrugItemDAO(final DrugItemDAO drugItemDAO);
	
	DrugItem findDrugItemByUuid(String uuid);
	
	List<DrugItem> findAllDrugItem(Boolean retired);
	
	DrugItem findDrugItemByDrugId(Integer drugId) throws PharmacyBusinessException;
	
	void saveDrugItem(DrugItem drugItem);
	
}
