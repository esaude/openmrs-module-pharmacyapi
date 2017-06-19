/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;

/**
 *
 */
public interface DrugWrapperService extends OpenmrsService {
	
	void setSessionFactory(SessionFactory sessionFactory);
	
	List<Drug> findDrugsByNameLike(String phrase);
}
