/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.advice;

import java.lang.reflect.Method;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionService;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author Stélio Moiane
 */
public class ParseObsToOrderAdvice implements MethodBeforeAdvice {
	
	@Override
	public void before(final Method method, final Object[] args, final Object target) {
		
		if (method.getName().equals("findPrescriptionsByPatient")) {
			
			final Patient patient = (Patient) args[0];
			final PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
			
			// prescriptionService.parseObsToOrders(patient);
		}
	}
}
