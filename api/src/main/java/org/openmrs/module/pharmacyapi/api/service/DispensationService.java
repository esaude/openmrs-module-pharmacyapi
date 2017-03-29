/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.module.pharmacyapi.api.model.DispensationWrapper;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public interface DispensationService extends OpenmrsService {
	
	DispensationWrapper dispense(final DispensationWrapper dispensation) throws APIException;
	
	void setProviderService(final ProviderService providerService);
	
	void setOrderService(final OrderService orderService);
	
	void setPatientService(final PatientService patientService);
	
	void setEncounterService(final EncounterService encounterService);
	
	void setLocationService(final LocationService locationService);
	
	void setConceptService(final ConceptService conceptService);
	
	void setPersonService(final PersonService personService);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
}
