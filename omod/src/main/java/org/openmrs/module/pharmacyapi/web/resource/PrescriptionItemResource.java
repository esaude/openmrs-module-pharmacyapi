/**
 * 
 */
package org.openmrs.module.pharmacyapi.web.resource;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.api.model.PrescriptionItem;
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
public class PrescriptionItemResource extends DelegatingSubResource<PrescriptionItem, Prescription, PrescriptionResource> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (rep instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("display");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("regime", Representation.REF);
			description.addProperty("arvPlan", Representation.REF);
			description.addProperty("changeReason", Representation.REF);
			description.addProperty("interruptionReason", Representation.REF);
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (rep instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("display");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("regime");
			description.addProperty("arvPlan");
			description.addProperty("changeReason");
			description.addProperty("interruptionReason");
			
			return description;
		}
		return null;
	}
	
	@Override
	public PrescriptionItem newDelegate() {
		return new PrescriptionItem();
	}
	
	@Override
	public PrescriptionItem save(PrescriptionItem arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@PropertyGetter("display")
	public String getDisplayString(PrescriptionItem item) {
		return item.getDrugOrder().getDrug().getName();
	}
	
	@Override
	public PageableResult doGetAll(Prescription parent, RequestContext context) throws ResponseException {

		List<PrescriptionItem> items = new ArrayList<>();
		if (parent != null) {
			items.addAll(parent.getPrescriptionItems());
		}
		return new NeedsPaging<PrescriptionItem>(items, context);
	}
	
	@Override
	public Prescription getParent(PrescriptionItem child) {
		
		return child.getPrescription();
	}
	
	@Override
	public void setParent(PrescriptionItem child, Prescription parent) {
		
		if (child != null) {
			child.setPrescription(parent);
		}
	}
	
	@Override
	protected void delete(PrescriptionItem arg0, String arg1, RequestContext arg2) throws ResponseException {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public PrescriptionItem getByUniqueId(String arg0) {
		
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(PrescriptionItem arg0, RequestContext arg1) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
}
