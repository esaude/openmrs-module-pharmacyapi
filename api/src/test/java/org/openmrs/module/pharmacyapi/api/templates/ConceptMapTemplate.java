package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class ConceptMapTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		
		Fixture.of(ConceptMap.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("concept", this.one(Concept.class, ConceptTemplate.DURATION_WEEKS));
				this.add("conceptMapType", this.one(ConceptMapType.class, ConceptMapTypeTemplate.VALID));
				this.add("conceptReferenceTerm", this.one(ConceptReferenceTerm.class, ConceptReferenceTermTemplate.VALID));
			}
		});
	}
}
