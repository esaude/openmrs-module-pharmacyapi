/**
 * 
 * UCSF -Global Programs 2017
 * 
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guimino Neves
 */
@Transactional
public interface DrugItemService extends OpenmrsService {
	
	void setSessionFactory(final DbSessionFactory dbSessionFactory);
	
	void setConceptService(final ConceptService conceptService) throws APIException;
	
	void save(DrugItem drugItem);
	
}
