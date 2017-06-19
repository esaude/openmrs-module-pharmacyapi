package org.openmrs.module.pharmacyapi.web.resource;

import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.service.DrugWrapperService;
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
