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

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.drugitem.service.DrugItemService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/drugitem", order = 1, supportedClass = DrugItem.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DrugItemResource extends MetadataDelegatingCrudResource<DrugItem> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		
		if ((rep instanceof RefRepresentation) || (rep instanceof FullRepresentation)) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drug", Representation.FULL);
			description.addProperty("pharmaceuticalForm", Representation.REF);
			description.addProperty("therapeuticGroup", Representation.REF);
			description.addProperty("therapeuticClass", Representation.REF);
			description.addProperty("fnmCode");
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drug", Representation.FULL);
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else {
			return null;
		}
	}
	
	@Override
	public DrugItem newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DrugItem save(final DrugItem delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DrugItem getByUniqueId(final String uniqueId) {
		return Context.getService(DrugItemService.class).findDrugItemByUuid(uniqueId);
	}
	
	@Override
	public void purge(final DrugItem delegate, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doGetAll(final RequestContext context) throws ResponseException {
		
		return new NeedsPaging<>(Context.getService(DrugItemService.class).findAllDrugItem(context.getIncludeAll()),
		        context);
	}
}
