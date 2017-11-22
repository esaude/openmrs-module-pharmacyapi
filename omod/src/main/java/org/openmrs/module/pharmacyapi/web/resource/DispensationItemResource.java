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

import java.util.ArrayList;
import java.util.List;

import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 *
 */

@SubResource(parent = DispensationResource.class, path = "dispensationItem", supportedClass = DispensationItem.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DispensationItemResource
        extends DelegatingSubResource<DispensationItem, Dispensation, DispensationResource> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("drugOrder");
			description.addProperty("quantityDispensed");
			description.addProperty("dateOfNextPickUp");
			description.addProperty("prescription");
			description.addProperty("dispensationItemCreationDate");
			description.addProperty("prescriptionExpirationDate");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (rep instanceof FullRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("drugOrder");
			description.addProperty("quantityDispensed");
			description.addProperty("dateOfNextPickUp");
			description.addProperty("prescription");
			description.addProperty("dispensationItemCreationDate");
			description.addProperty("prescriptionExpirationDate");
			return description;
		}
		return null;
	}
	
	@Override
	public DispensationItem newDelegate() {
		return new DispensationItem();
	}
	
	@Override
	public DispensationItem save(final DispensationItem arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public PageableResult doGetAll(final Dispensation parent, final RequestContext context) throws ResponseException {
		
		final List<DispensationItem> items = new ArrayList<>();
		if (parent != null) {
			items.addAll(parent.getDispensationItems());
		}
		return new NeedsPaging<>(items, context);
	}
	
	@Override
	public Dispensation getParent(final DispensationItem child) {
		
		return child.getDispensation();
	}
	
	@Override
	public void setParent(final DispensationItem child, final Dispensation parent) {
		
		if (child != null) {
			child.setDispensation(parent);
		}
	}
	
	@Override
	protected void delete(final DispensationItem arg0, final String arg1, final RequestContext arg2)
	        throws ResponseException {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DispensationItem getByUniqueId(final String arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(final DispensationItem arg0, final RequestContext arg1) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
}
