/**
 *
 */
package org.openmrs.module.pharmacyapi.web.resource;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.model.DrugRegime;
import org.openmrs.module.pharmacyapi.api.service.DrugRegimeService;
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
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 */
@Resource(name = RestConstants.VERSION_1 + "/drugregime", order = 1, supportedClass = DrugRegime.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DrugRegimeResource extends MetadataDelegatingCrudResource<DrugRegime> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		
		if ((rep instanceof RefRepresentation) || (rep instanceof FullRepresentation)) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drugItem", Representation.REF);
			description.addProperty("regime", Representation.REF);
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drugItem", Representation.REF);
			description.addProperty("regime", Representation.REF);
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else {
			return null;
		}
	}
	
	@Override
	public DrugRegime newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DrugRegime save(final DrugRegime delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(final DrugRegime delegate, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
		
	}
	
	@Override
	public DrugRegime getByUniqueId(final String uniqueId) {
		
		return Context.getService(DrugRegimeService.class).findDrugRegimeByUuid(uniqueId);
	}
	
	@Override
	protected NeedsPaging<DrugRegime> doGetAll(final RequestContext context) {

		final List<DrugRegime> drugRegimes = Context.getService(DrugRegimeService.class)
				.findAllDrugRegimes(context.getIncludeAll());
		return new NeedsPaging<>(drugRegimes, context);
	}
	
	@Override
	protected PageableResult doSearch(final RequestContext context) {

		final String regimeUuid = context.getRequest().getParameter("regime");

		if (regimeUuid != null) {

			final Concept regime = Context.getConceptService().getConceptByUuid(regimeUuid);
			if (regime == null) {
				return new EmptySearchResult();
			}
			final DrugRegimeService drugRegimeService = Context.getService(DrugRegimeService.class);

			return new NeedsPaging<>(drugRegimeService.findDrugRegimesByRegime(regime), context);
		}

		return new EmptySearchResult();
	}
}
