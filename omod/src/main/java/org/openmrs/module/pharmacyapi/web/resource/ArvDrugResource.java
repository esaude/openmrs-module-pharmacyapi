/**
 *
 */
package org.openmrs.module.pharmacyapi.web.resource;

import java.util.List;

import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.model.DrugRegime;
import org.openmrs.module.pharmacyapi.api.service.DrugRegimeService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_11.DrugResource1_11;

/**
 */
@Resource(name = RestConstants.VERSION_1 + "/arvdrug", order = 2, supportedClass = Drug.class, supportedOpenmrsVersions = {
        "1.11.*", "1.12.*" })
public class ArvDrugResource extends DrugResource1_11 {
	
	@Override
	public Drug getByUniqueId(final String uniqueId) {
		
		final List<DrugRegime> drugRegime = Context.getService(DrugRegimeService.class).findDrugRegimeByDrugUuid(uniqueId);
		
		return drugRegime.isEmpty() ? null : drugRegime.get(0).getDrugItem().getDrug();
	}
	
	@Override
	protected NeedsPaging<Drug> doGetAll(final RequestContext context) {

		return new NeedsPaging<>(Context.getService(DrugRegimeService.class).findArvDrugs(), context);
	}
}
