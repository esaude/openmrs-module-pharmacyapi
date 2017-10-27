package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class ConceptReferenceTermTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		Fixture.of(ConceptReferenceTerm.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("conceptReferenceTermId", 4);
				this.add("code", "7345699");
				this.add("conceptSource", this.one(ConceptSource.class, ConceptSourceTemplate.VALID));
			}
		});
	}
}
