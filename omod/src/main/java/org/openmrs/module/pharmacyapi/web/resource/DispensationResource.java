/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.web.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
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
@Resource(name = RestConstants.VERSION_1
        + "/dispensation", order = 1, supportedClass = Dispensation.class, supportedOpenmrsVersions = { "1.8.*",
        "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class DispensationResource extends DataDelegatingCrudResource<Dispensation> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		
		if (rep instanceof RefRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("dispensationItems");
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("dispensationItems");
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
		catch (final Exception e) {
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
		
		final Order order = Context.getOrderService().getOrderByUuid(orderUuid);
		if (order != null) {
			final Dispensation dispensation = new Dispensation();
			final DispensationItem dispensationItem = new DispensationItem();
			dispensationItem.setOrderUuid(order.getUuid());
			dispensation.setDispensationItems(Arrays.asList(dispensationItem));
			dispensation.setPatientUuid(order.getPatient().getUuid());
			dispensation.setLocationUuid(order.getEncounter().getLocation().getUuid());
			dispensation.setProviderUuid(order.getEncounter().getProvider().getUuid());
			
			final Concept arvConceptQuestion = Context.getConceptService()
			        .getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS);
			
			final Set<Obs> allObs = order.getEncounter().getAllObs(this.isRetirable());
			
			for (final Obs obs : allObs) {
				
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
	protected PageableResult doSearch(final RequestContext context) {
		
		final String patientUuid = context.getRequest().getParameter("patient");
		final String startDate = context.getRequest().getParameter("startDate");
		final String endDate = context.getRequest().getParameter("endDate");
		
		final Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
		
		if ((patientUuid == null) || (patient == null)) {
			return new EmptySearchResult();
		}
		
		if ((startDate != null) && (endDate != null)) {
			
			try {
				
				return new NeedsPaging<>(this.findFilaByPatientAndDateInterval(patient, startDate, endDate), context);
			}
			catch (ParseException | PharmacyBusinessException e) {
				
				throw new APIException(e);
			}
		}
		
		return new EmptySearchResult();
	}
	
	private List<Dispensation> findFilaByPatientAndDateInterval(final Patient patient, final String startDateText,
	        final String endDateText) throws ParseException, PharmacyBusinessException {
		
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		final Date stardDate = formatter.parse(startDateText);
		final Date endDate = formatter.parse(endDateText);
		
		final List<Dispensation> dispensations = Context.getService(DispensationService.class)
		        .findFilaDispensationByPatientAndDateInterval(patient, stardDate, endDate);
		
		return dispensations;
	}
	
	@Override
	protected void delete(final Dispensation dispensation, final String reason, final RequestContext context)
	        throws ResponseException {
		
		try {
			Context.getService(DispensationService.class).cancelDispensationItems(dispensation, reason);
		}
		catch (final Exception e) {
			
			throw new APIException(e.getMessage());
		}
		
	}
	
	@Override
	public void purge(final Dispensation dispensation, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@PropertySetter("dispensationItems")
	public static void prescriptionItems(final Dispensation instance, final List<DispensationItem> items) {
		for (final DispensationItem item : items) {
			item.setDispensation(instance);
		}
		instance.setDispensationItems(items);
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
	
	@Override
	public List<String> getPropertiesToExposeAsSubResources() {
		return Arrays.asList("dispensationItems");
	}
}
