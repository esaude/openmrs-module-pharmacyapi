package org.openmrs.module.pharmacyapi.web.resource;

import java.util.Arrays;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.entity.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
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
@Resource(name = RestConstants.VERSION_1 + "/dispensation", order = 1, supportedClass = Dispensation.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DispensationResource extends DataDelegatingCrudResource<Dispensation> {
	
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
	public Dispensation newDelegate() {
		final Dispensation dispensation = new Dispensation();
		return dispensation;
	}
	
	@Override
	public Dispensation save(final Dispensation dispensation) {
		
		try {
			final DispensationService dispensationService = Context.getService(DispensationService.class);
			
			dispensationService.dispense(dispensation);
			
		}
		catch (Exception e) {
			throw new APIException(e);
		}
		return dispensation;
	}
	
	/**
	 * Has the Approach for cancelation a dispensation will be made canceling each DrugOrder for a
	 * especific Dispensation, the getByUniqueId will return a specific DrugOrder so will be
	 * possible to make a cancelation for the found drugOrder
	 * 
	 * @param orderUuid the uuid of specific DrugOrder
	 * @return {@Dispensation} a dispensation with a @Collection<DispensationItem>
	 *         containing one @DispensationItem with the found DrugOrder
	 */
	@Override
	public Dispensation getByUniqueId(final String orderUuid) {
		
		Order order = Context.getOrderService().getOrderByUuid(orderUuid);
		if (order != null) {
			Dispensation dispensation = new Dispensation();
			DispensationItem dispensationItem = new DispensationItem();
			dispensationItem.setOrderUuid(order.getUuid());
			dispensation.setDispensationItems(Arrays.asList(dispensationItem));
			dispensation.setPatientUuid(order.getPatient().getUuid());
			dispensation.setLocationUuid(order.getEncounter().getLocation().getUuid());
			dispensation.setProviderUuid(order.getEncounter().getProvider().getUuid());
			
			Concept arvConceptQuestion = Context.getConceptService().getConceptByUuid(
			    MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
			
			Set<Obs> allObs = order.getEncounter().getAllObs(isRetirable());
			
			for (Obs obs : allObs) {
				
				if (obs.getConcept().equals(arvConceptQuestion)) {
					
					dispensationItem.setRegimeUuid(obs.getValueCoded().getUuid());
					break;
				}
			}
			return dispensation;
		}
		
		throw new APIException("Order with uuid: " + orderUuid + " not found");
		
	}
	
	@Override
	protected void delete(final Dispensation dispensation, final String reason, final RequestContext context)
	        throws ResponseException {
		
		try {
			Context.getService(DispensationService.class).cancelDispensationItems(dispensation, reason);
		}
		catch (Exception e) {
			
			throw new APIException(e);
		}
		
	}
	
	@Override
	public void purge(final Dispensation dispensation, final RequestContext context) throws ResponseException {
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
