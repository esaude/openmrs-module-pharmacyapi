/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 * 
 */
package org.openmrs.module.pharmacyapi.web.resource;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 *
 */
@Resource(name = RestConstants.VERSION_1 + "/prescriptiondispensation", order = 1, supportedClass = PrescriptionDispensation.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class PrescriptionDispensationResource extends MetadataDelegatingCrudResource<PrescriptionDispensation> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = null;
		if (rep instanceof RefRepresentation || rep instanceof DefaultRepresentation) {
			description = new DelegatingResourceDescription();
			description.addProperty("prescription", Representation.REF);
			description.addProperty("dispensation", Representation.REF);
		} else if (rep instanceof FullRepresentation) {
			description = new DelegatingResourceDescription();
			description.addProperty("prescription", Representation.REF);
			description.addProperty("dispensation", Representation.REF);
		}
		
		return description;
	}
	
	@Override
	public PrescriptionDispensation newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public PrescriptionDispensation save(PrescriptionDispensation prescriptionDispensation) {
		
		// return Context.getService(PrescriptionDispensationService.class)
		// .savePrescriptionDispensation(prescriptionDispensation);
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public PrescriptionDispensation getByUniqueId(String uniqueId) {
		
		return null;// Context.getService(PrescriptionDispensationService.class).findPrescriptionDispensationByUuid(uniqueId);
		
	}
	
	@Override
	protected PageableResult doGetAll(final RequestContext context) throws ResponseException {
		
		return new EmptySearchResult();
	}
	
	@Override
	protected PageableResult doSearch(final RequestContext context) {
		
		final String patientUuid = context.getRequest().getParameter("patient");
		
		if (patientUuid != null) {
			
			final Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
			
			// List<PrescriptionDispensation> result =
			// Context.getService(PrescriptionDispensationService.class)
			// .findPrescriptionDispensationByPatientUuid(patientUuid);
			
			// return new NeedsPaging<>(result, context);
		}
		return new EmptySearchResult();
	}
	
	@Override
	public void purge(PrescriptionDispensation arg0, RequestContext arg1) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
}
