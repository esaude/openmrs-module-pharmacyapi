/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service;

import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.OrderService;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Stélio Moiane
 */
@Transactional
public interface PrescriptionService extends OpenmrsService {
	
	void parseObsToOrders(final Patient patient) throws APIException;
	
	List<Prescription> findPrescriptionsByPatient(final Patient patient) throws APIException;
	
	void setOrderService(final OrderService orderService) throws APIException;
	
	void setConceptService(final ConceptService conceptService) throws APIException;
	
	Double calculateDrugPikckedUp(final DrugOrder order) throws APIException;
	
}
