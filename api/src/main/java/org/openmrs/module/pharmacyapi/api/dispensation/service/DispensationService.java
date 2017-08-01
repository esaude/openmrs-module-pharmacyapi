/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.dispensation.service;

import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.db.DbSessionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public interface DispensationService extends OpenmrsService {
	
	Dispensation dispense(final Dispensation dispensation) throws PharmacyBusinessException;
	
	void setProviderService(final ProviderService providerService);
	
	void setOrderService(final OrderService orderService);
	
	void setPatientService(final PatientService patientService);
	
	void setEncounterService(final EncounterService encounterService);
	
	void setLocationService(final LocationService locationService);
	
	void setConceptService(final ConceptService conceptService);
	
	void setPersonService(final PersonService personService);
	
	void setDbSessionManager(final DbSessionManager dbSessionManager);
	
	void setPrescriptionDispensationService(PrescriptionDispensationService prescriptionDispensationService);
	
	void cancelDispensationItems(Dispensation dispensation, String cancelationReason) throws PharmacyBusinessException;
	
}
