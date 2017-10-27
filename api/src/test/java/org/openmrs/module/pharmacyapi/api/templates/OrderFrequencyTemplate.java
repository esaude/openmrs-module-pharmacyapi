package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Concept;
import org.openmrs.OrderFrequency;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class OrderFrequencyTemplate implements TemplateLoader {
	
	// public static final String FREQUENCY_ONCE_A_DAY =
	// "9d7127f9-10e8-11e5-9009-0242ac110012";
	
	public static final String FREQUENCY_ONCE_A_DAY = "38090760-7c38-11e3-baa7-0800200c9a66";
	
	public static final String FREQUENCY_TWICE_A_DAY = "9d717849-10e8-11e5-9009-0242ac110012";
	
	public static final String FREQUENCY_THRICE_A_DAY = "9d71d41e-10e8-11e5-9009-0242ac110012";
	
	@Override
	public void load() {
		
		Fixture.of(OrderFrequency.class).addTemplate(FREQUENCY_ONCE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("frequencyPerDay", new Double(1));
				this.add("concept", this.one(Concept.class, ConceptTemplate.FREQUENCY_ONCE_A_DAY));
				this.add("uuid", FREQUENCY_ONCE_A_DAY);
			}
		});
		
		Fixture.of(OrderFrequency.class).addTemplate(FREQUENCY_TWICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("frequencyPerDay", new Double(2));
				this.add("concept", this.one(Concept.class, ConceptTemplate.FREQUENCY_TWICE_A_DAY));
				this.add("uuid", FREQUENCY_TWICE_A_DAY);
			}
		});
		
		Fixture.of(OrderFrequency.class).addTemplate(FREQUENCY_THRICE_A_DAY, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("frequencyPerDay", new Double(3));
				this.add("concept", this.one(Concept.class, ConceptTemplate.FREQUENCY_THRICE_A_DAY));
				this.add("uuid", FREQUENCY_THRICE_A_DAY);
			}
		});
		
	}
}
