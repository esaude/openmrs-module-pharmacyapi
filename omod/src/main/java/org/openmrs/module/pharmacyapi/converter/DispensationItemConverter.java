package org.openmrs.module.pharmacyapi.converter;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacyapi.api.model.DispensationWrapperItem;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingConverter;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * @author Stélio Moiane
 */
@Handler(supports = DispensationWrapperItem.class, order = 0)
public class DispensationItemConverter extends BaseDelegatingConverter<DispensationWrapperItem> {
	
	@Override
	public DispensationWrapperItem newInstance(final String type) {
		return new DispensationWrapperItem();
	}
	
	@Override
	public DispensationWrapperItem getByUniqueId(final String string) {
		return null;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		return null;
	}
}
