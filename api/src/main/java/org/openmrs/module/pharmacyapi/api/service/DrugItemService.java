/**
 *
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.List;

import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacyapi.api.dao.DrugItemDAO;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;

/**
 */
public interface DrugItemService extends OpenmrsService {
	
	void setDrugItemDAO(final DrugItemDAO drugItemDAO);
	
	DrugItem findDrugItemByUuid(String uuid);
	
	List<DrugItem> findAllDrugItem(Boolean retired);
	
	DrugItem findDrugItemByDrug(Drug drug) throws PharmacyBusinessException;
	
	void saveDrugItem(DrugItem drugItem);
	
}
