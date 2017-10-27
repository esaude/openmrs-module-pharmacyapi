package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.ConceptMapType;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class ConceptMapTypeTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		
		Fixture.of(ConceptMapType.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("conceptMapTypeId", 200);
				this.add("name", "same-XX");
				this.add("uuid", "35543629-7d8c-11e1-909d-c80aa9edcXXX");
			}
		});
	}
	
}
