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

import org.openmrs.CareSetting;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CareSettingTemplate implements TemplateLoader {
	
	public static final String INPATIENT = "c365e560-c3ec-11e3-9c1a-0800200c9a66";
	
	@Override
	public void load() {
		
		Fixture.of(CareSetting.class).addTemplate(INPATIENT, new Rule() {
			
			{
				this.add("uuid", INPATIENT);
			}
		});
	}
}
