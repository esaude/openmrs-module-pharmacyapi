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

import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.drugitem.service.DrugWrapperService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_10.DrugResource1_10;

@Resource(name = RestConstants.VERSION_1 + "/drugresource", order = 2, supportedClass = Drug.class, supportedOpenmrsVersions = {
        "1.11.*", "1.12.*" })
public class DrugWrapResource extends DrugResource1_10 {
	
	protected PageableResult doSearch(RequestContext ctx) {
		
		return new NeedsPaging<>(
		        Context.getService(DrugWrapperService.class).findDrugsByNameLike(ctx.getParameter("q")), ctx);
	}
}
