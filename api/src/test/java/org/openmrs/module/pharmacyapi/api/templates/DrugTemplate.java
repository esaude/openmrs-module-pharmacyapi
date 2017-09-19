package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class DrugTemplate implements BaseTemplateLoader {
	
	final static String TRIOMUNE30 = "3cfcf118-931c-46f7-8ff6-7b876f0d4202";
	
	final static String ASPIRIN = "05ec820a-d297-44e3-be6e-698531d9dd3f";
	
	final static String NYQUIL = "7e2323fa-0fa0-461f-9b59-6765997d849e";
	
	@Override
	public void load() {
		
		Fixture.of(Drug.class).addTemplate(TRIOMUNE30, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.TRIOMUNE30));
				this.add("uuid", TRIOMUNE30);
			}
		});
		
		Fixture.of(Drug.class).addTemplate(ASPIRIN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.ASPIRIN));
				this.add("uuid", ASPIRIN);
			}
		});
		
		Fixture.of(Drug.class).addTemplate(NYQUIL, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.NYQUIL));
				this.add("uuid", NYQUIL);
			}
		});
	}
	
}
