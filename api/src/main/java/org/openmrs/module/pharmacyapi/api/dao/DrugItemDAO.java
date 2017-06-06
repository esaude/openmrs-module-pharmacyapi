/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.module.pharmacyapi.api.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;

/**
 *
 */
public interface DrugItemDAO {
	
	public interface QUERY_NAME {
		
		String findByUUID = "DrugItem.findByUUID";
		
		String findByDrug = "DrugItem.findByDrug";
	}
	
	public interface QUERY {
		
		String findByUUID = "select di from DrugItem di where di.uuid = :uuid";
		
		String findByDrug = "select di from DrugItem di where di.drug = :drug";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	DrugItem findByUuid(String uuid);
	
	List<DrugItem> findAll(boolean retired);
	
	void save(DrugItem drugItem);
	
	DrugItem findByDrug(Drug drug) throws EntityNotFoundException;
	
	List<DrugItem> findAll(Boolean retired);
	
}
