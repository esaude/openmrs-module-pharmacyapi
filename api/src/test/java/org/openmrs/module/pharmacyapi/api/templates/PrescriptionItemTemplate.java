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

import org.openmrs.DrugOrder;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PrescriptionItemTemplate implements TemplateLoader {
	
	public static final String VALID_01 = "VALID_01";
	
	@Override
	public void load() {
		
		Fixture.of(PrescriptionItem.class).addTemplate(VALID_01, new Rule() {
			
			{
				this.add("drugOrder", this.one(DrugOrder.class, DrugOrderTemplate.VALID_PROFLAXIA_01));
				
			}
		});
	}
}
