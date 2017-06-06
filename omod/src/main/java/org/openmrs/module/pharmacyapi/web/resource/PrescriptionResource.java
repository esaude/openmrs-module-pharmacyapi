package org.openmrs.module.pharmacyapi.web.resource;

import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author Stélio Moiane
 */
@Resource(name = RestConstants.VERSION_1 + "/prescription", order = 1, supportedClass = Prescription.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class PrescriptionResource extends DataDelegatingCrudResource<Prescription> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		
		if (rep instanceof RefRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("provider");
			description.addProperty("prescriptionDate");
			description.addProperty("conceptParentUuid");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugRegime");
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("provider");
			description.addProperty("prescriptionDate");
			description.addProperty("conceptParentUuid");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugRegime");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			return description;
		} else if (rep instanceof FullRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("drugOrder");
			description.addProperty("dosingInstructions");
			description.addProperty("provider");
			description.addProperty("prescriptionDate");
			description.addProperty("conceptParentUuid");
			description.addProperty("drugToPickUp");
			description.addProperty("drugPickedUp");
			description.addProperty("drugRegime");
			description.addSelfLink();
			return description;
		} else {
			return null;
		}
	}
	
	@Override
	public Prescription newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public Prescription save(final Prescription delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(final Prescription delegate, final String reason, final RequestContext context)
	        throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(final Prescription delegate, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public Prescription getByUniqueId(final String uniqueId) {
		final DrugOrder drugOrder = (DrugOrder) Context.getOrderService().getOrderByUuid(uniqueId);
		
		return new Prescription(drugOrder);
	}
	
	@Override
	protected PageableResult doSearch(final RequestContext context) {

		final String patientUuid = context.getRequest().getParameter("patient");

		if (patientUuid == null) {
			return new EmptySearchResult();
		}

		final Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);

		if (patient == null) {
			return new EmptySearchResult();
		}

		final PrescriptionService prescriptionService = Context.getService(PrescriptionService.class);
		try {
			final List<Prescription> prescriptions = prescriptionService.findPrescriptionsByPatient(patient);
			return new NeedsPaging<>(prescriptions, context);

		} catch (final PharmacyBusinessException e) {

		}

		return new EmptySearchResult();
	}
}
