/**
 *
 */
package org.openmrs.module.pharmacyapi.api.drugitem.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.module.pharmacyapi.api.common.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;

/**
 *
 */
public interface DrugItemDAO {
	
	public interface QUERY_NAME {
		
		String findByUUID = "DrugItem.findByUUID";
		
		String findByDrugId = "DrugItem.findByDrugId";
	}
	
	public interface QUERY {
		
		String findByUUID = "select di from DrugItem di where di.uuid = :uuid";
		
		String findByDrugId = "select di from DrugItem di where di.drugId = :drugId";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	DrugItem findByUuid(String uuid);
	
	List<DrugItem> findAll(boolean retired);
	
	void save(DrugItem drugItem);
	
	DrugItem findByDrugId(Integer drugId) throws EntityNotFoundException;
	
	List<DrugItem> findAll(Boolean retired);
	
}
