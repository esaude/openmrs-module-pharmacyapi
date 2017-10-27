package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.ConceptSource;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class ConceptSourceTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		
		Fixture.of(ConceptSource.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("conceptSourceId", 200);
				// this.add("uniqueId", "2.16.840.1.113883.6.99");
				this.add("uuid", "j3nfjk33-639f-4cb4-961f-1e025b908XXX");
			}
		});
		
	}
}
