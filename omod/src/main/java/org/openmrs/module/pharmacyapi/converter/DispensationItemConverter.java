package org.openmrs.module.pharmacyapi.converter;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingConverter;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * @author Stélio Moiane
 */
@Handler(supports = DispensationItem.class, order = 0)
public class DispensationItemConverter extends BaseDelegatingConverter<DispensationItem> {
	
	@Override
	public DispensationItem newInstance(final String type) {
		return new DispensationItem();
	}
	
	@Override
	public DispensationItem getByUniqueId(final String string) {
		return null;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {
		return null;
	}
}
