/**
 * 
 */
package org.openmrs.module.pharmacyapi.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionDispensationService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 *
 */
public class PrescriptionDispensationResource extends BaseDelegatingResource<PrescriptionDispensation> {

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

		return Context.getService(PrescriptionDispensationService.class)
				.savePrescriptionDispensation(prescriptionDispensation);
	}

	@Override
	protected void delete(PrescriptionDispensation arg0, String arg1, RequestContext arg2) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();

	}

	@Override
	public PrescriptionDispensation getByUniqueId(String uniqueId) {

		return Context.getService(PrescriptionDispensationService.class).findByUuid(uniqueId);

	}

	@Override
	public void purge(PrescriptionDispensation arg0, RequestContext arg1) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
}
