/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class DrugTemplate implements BaseTemplateLoader {
	
	public final static String TRIOMUNE30 = "3cfcf118-931c-46f7-8ff6-7b876f0d4202";
	
	public final static String ASPIRIN = "05ec820a-d297-44e3-be6e-698531d9dd3f";
	
	public final static String NYQUIL = "7e2323fa-0fa0-461f-9b59-6765997d849e";
	
	public final static String NEVIRAPINA = ConceptTemplate.NEVIRAPINA;
	
	@Override
	public void load() {
		
		Fixture.of(Drug.class).addTemplate(DrugTemplate.TRIOMUNE30, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.TRIOMUNE30));
				this.add("uuid", DrugTemplate.TRIOMUNE30);
			}
		});
		
		Fixture.of(Drug.class).addTemplate(DrugTemplate.ASPIRIN, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.ASPIRIN));
				this.add("uuid", DrugTemplate.ASPIRIN);
			}
		});
		
		Fixture.of(Drug.class).addTemplate(DrugTemplate.NYQUIL, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.NYQUIL));
				this.add("uuid", DrugTemplate.NYQUIL);
			}
		});
		
		Fixture.of(Drug.class).addTemplate(DrugTemplate.NEVIRAPINA, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("concept", this.one(Concept.class, ConceptTemplate.NEVIRAPINA));
				this.add("uuid", DrugTemplate.NEVIRAPINA);
			}
		});
	}
	
}
