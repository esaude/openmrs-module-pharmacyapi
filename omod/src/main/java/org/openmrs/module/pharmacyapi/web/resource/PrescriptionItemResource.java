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

import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
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

@SubResource(parent = PrescriptionResource.class, path = "prescriptionItem", supportedClass = PrescriptionItem.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class PrescriptionItemResource
        extends DelegatingSubResource<PrescriptionItem, Prescription, PrescriptionResource> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("display");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("status");
			description.addProperty("expectedNextPickUpDate");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (rep instanceof FullRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("display");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("status");
			description.addProperty("expectedNextPickUpDate");
			return description;
		}
		return null;
	}
	
	@Override
	public PrescriptionItem newDelegate() {
		return new PrescriptionItem();
	}
	
	@Override
	public PrescriptionItem save(final PrescriptionItem arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@PropertyGetter("display")
	public String getDisplayString(final PrescriptionItem item) {
		return item.getDrugOrder().getDrug().getName();
	}
	
	@Override
	public PageableResult doGetAll(final Prescription parent, final RequestContext context) throws ResponseException {
		
		final List<PrescriptionItem> items = new ArrayList<>();
		if (parent != null) {
			items.addAll(parent.getPrescriptionItems());
		}
		return new NeedsPaging<>(items, context);
	}
	
	@Override
	public Prescription getParent(final PrescriptionItem child) {
		
		return child.getPrescription();
	}
	
	@Override
	public void setParent(final PrescriptionItem child, final Prescription parent) {
		
		if (child != null) {
			child.setPrescription(parent);
		}
	}
	
	@Override
	protected void delete(final PrescriptionItem arg0, final String arg1, final RequestContext arg2)
	        throws ResponseException {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public PrescriptionItem getByUniqueId(final String arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(final PrescriptionItem arg0, final RequestContext arg1) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
}
