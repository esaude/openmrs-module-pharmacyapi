/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.module.pharmacyapi.api.model.DispensationWrapperItem;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class DispensationItemTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	public static final String NON_ARV = "NON_ARV";
	
	@Override
	public void load() {
		Fixture.of(DispensationWrapperItem.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("orderUuid", this.random("1b92a25c-5747-471c-8275-3d150243d9c9",
				    "ed35ed32-bf2c-4cf4-ab7c-38c83a37c548", "0120ed22-f2e9-4818-b9bd-536ac9aec13a"));
				this.add("quantityDispensed", this.random(Double.class, this.range(1.0, 100.0)));
				this.add("dateOfNextPickUp", this.instant("now"));
				this.add("conceptParentUuid", this.random("e1d83d4a-1d5f-11e0-b929-000c29ad1d07", null));
			}
		});
		
		Fixture.of(DispensationWrapperItem.class).addTemplate(NON_ARV).inherits(VALID, new Rule() {
			
			{
				this.add("dateOfNextPickUp", null);
			}
		});
	}
}
