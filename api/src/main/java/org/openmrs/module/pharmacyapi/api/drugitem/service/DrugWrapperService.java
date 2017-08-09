/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.drugitem.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;

/**
 * Wrapper used as a workaround to a problem we are having with the DrugResource rest
 */
public interface DrugWrapperService extends OpenmrsService {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<Drug> findDrugsByNameLike(String phrase);
}
