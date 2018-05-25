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
import org.openmrs.DrugOrder;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PrescriptionItemTemplate implements TemplateLoader {
	
	public static final String VALID_PROFLAXIA_TRIOMUNE30 = DrugOrderTemplate.VALID_PROFLAXIA_TRIOMUNE30;
	
	public static final String VALID_PROFLAXIA_ASPIRIN = DrugOrderTemplate.VALID_PROFLAXIA_ASPIRIN;
	
	public static final String VALID_ARV_NEVIRAPINA = DrugOrderTemplate.VALID_ARV_NEVIRAPINA;
	
	@Override
	public void load() {
		
		Fixture.of(PrescriptionItem.class).addTemplate(PrescriptionItemTemplate.VALID_PROFLAXIA_TRIOMUNE30, new Rule() {
			
			{
				this.add("drugOrder", this.one(DrugOrder.class, DrugOrderTemplate.VALID_PROFLAXIA_TRIOMUNE30));
				
			}
		});
		
		Fixture.of(PrescriptionItem.class).addTemplate(PrescriptionItemTemplate.VALID_PROFLAXIA_ASPIRIN, new Rule() {
			
			{
				this.add("drugOrder", this.one(DrugOrder.class, DrugOrderTemplate.VALID_PROFLAXIA_ASPIRIN));
				
			}
		});
		
		Fixture.of(PrescriptionItem.class).addTemplate(PrescriptionItemTemplate.VALID_ARV_NEVIRAPINA, new Rule() {
			
			{
				this.add("drugOrder", this.one(DrugOrder.class, DrugOrderTemplate.VALID_ARV_NEVIRAPINA));
				this.add("regime", this.one(Concept.class, ConceptTemplate.AZT_3TC_NVP));
				this.add("therapeuticLine", this.one(Concept.class, ConceptTemplate.ARV_FIRST_LINE_PLAN));
				this.add("arvPlan", this.one(Concept.class, ConceptTemplate.START_DRUGS_ARV_PLAN));
				
			}
		});
	}
}
