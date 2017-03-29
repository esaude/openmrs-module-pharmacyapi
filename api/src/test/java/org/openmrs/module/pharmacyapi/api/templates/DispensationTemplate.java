/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.module.pharmacyapi.api.model.DispensationWrapper;
import org.openmrs.module.pharmacyapi.api.model.DispensationWrapperItem;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class DispensationTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		
		Fixture.of(DispensationWrapper.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("providerUuid", "7013d271-1bc2-4a50-bed6-8932044bc18f");
				this.add(
				    "dispensationItems",
				    this.has(3).of(DispensationWrapperItem.class, DispensationItemTemplate.NON_ARV,
				        DispensationItemTemplate.NON_ARV, DispensationTemplate.VALID));
			}
		});
		
	}
}
