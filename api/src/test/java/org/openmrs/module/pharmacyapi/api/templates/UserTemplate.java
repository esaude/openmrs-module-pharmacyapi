package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.User;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class UserTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "valid";
	
	@Override
	public void load() {
		
		Fixture.of(User.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("userId", 501);
				this.add("uuid", "c1d8f5c2-e131-11de-babe-001e378eb67e");
			}
		});
	}
}
