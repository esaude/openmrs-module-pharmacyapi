package org.openmrs.module.pharmacyapi.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.model.DispensationWrapper;
import org.openmrs.module.pharmacyapi.api.service.DispensationService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author Stélio Moiane
 */
@Resource(name = RestConstants.VERSION_1 + "/dispensation", order = 1, supportedClass = DispensationWrapper.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DispensationResource extends DataDelegatingCrudResource<DispensationWrapper> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		
		if (rep instanceof RefRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (rep instanceof FullRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addSelfLink();
			return description;
		} else {
			return null;
		}
	}
	
	@Override
	public DispensationWrapper newDelegate() {
		final DispensationWrapper dispensation = new DispensationWrapper();
		return dispensation;
	}
	
	@Override
	public DispensationWrapper save(final DispensationWrapper dispensation) {
		
		final DispensationService dispensationService = Context.getService(DispensationService.class);
		
		dispensationService.dispense(dispensation);
		
		return dispensation;
	}
	
	@Override
	public DispensationWrapper getByUniqueId(final String uniqueId) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(final DispensationWrapper dispensation, final String reason, final RequestContext context)
	        throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(final DispensationWrapper dispensation, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		
		final DelegatingResourceDescription description = new DelegatingResourceDescription();
		
		description.addProperty("dispensationId");
		description.addProperty("providerUuid");
		description.addProperty("patientUuid");
		description.addProperty("locationUuid");
		description.addProperty("dispensationItems");
		
		return description;
		
	}
}
