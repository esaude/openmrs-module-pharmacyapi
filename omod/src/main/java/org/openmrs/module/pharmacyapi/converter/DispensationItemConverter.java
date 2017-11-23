/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
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
