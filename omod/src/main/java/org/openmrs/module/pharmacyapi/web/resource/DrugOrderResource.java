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

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_10.OrderResource1_10;

@Resource(name = RestConstants.VERSION_1 + "/drugorderresource", supportedClass = Order.class, supportedOpenmrsVersions = {
        "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*" })
public class DrugOrderResource extends OrderResource1_10 {
	
	@Override
	protected void delete(Order delegate, String reason, RequestContext context) throws ResponseException {
		
		try {
			Context.getService(PrescriptionService.class).cancelPrescriptionItem(new PrescriptionItem((DrugOrder) delegate),
			    reason);
		}
		catch (PharmacyBusinessException e) {
			
			throw new APIException(e);
		}
	}
}
