/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class ProviderTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	public static final String TEST = "c2299800-cca9-11e0-9572-0800200c9a66";
	
	@Override
	public void load() {
		Fixture.of(Provider.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("person", this.one(Person.class, PersonTemplate.VALID));
			}
		});
		
		Fixture.of(Provider.class).addTemplate(TEST, new Rule() {
			
			{
				this.add("uuid", TEST);
			}
		});
	}
}
