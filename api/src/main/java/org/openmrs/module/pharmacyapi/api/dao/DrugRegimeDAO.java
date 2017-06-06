/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.module.pharmacyapi.api.exception.EntityNotFoundException;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.model.DrugRegime;

/**
 *
 */
public interface DrugRegimeDAO {
	
	public interface QUERY_NAME {
		
		String findByRegime = "DrugRegime.findByRegime";
		
		String findByRegimeAndDrugItem = "DrugRegime.findByRegimeAndDrugItem";
		
		String findByDrugUuid = "DrugRegime.findByDrugUuid";
	}
	
	public interface QUERY {
		
		String findByRegime = "select distinct drugRegime from DrugRegime drugRegime join fetch drugRegime.drugItem join fetch drugRegime.regime where drugRegime.regime =:regime and drugRegime.retired = :retired";
		
		String findByRegimeAndDrugItem = "select distinct drugRegime from DrugRegime drugRegime where drugRegime.regime = :regime and drugRegime.drugItem = :drugItem";
		
		String findByDrugUuid = "select distinct drugRegime from DrugRegime drugRegime where drugRegime.drugItem.drug.uuid =:drugUuid";
	}
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<DrugRegime> findByRegime(Concept regime, boolean retired);
	
	List<DrugRegime> findAll(boolean retired);
	
	DrugRegime findByUuid(String uuid);
	
	DrugRegime findByRegimeAndDrugItem(Concept regime, DrugItem drugItem) throws EntityNotFoundException;
	
	List<DrugRegime> findByDrugUuid(String drugUuid);
	
}
