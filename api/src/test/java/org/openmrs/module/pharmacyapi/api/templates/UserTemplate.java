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

import org.openmrs.User;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class UserTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "valid";
	
	@Override
	public void load() {
		
		Fixture.of(User.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("userId", 501);
				this.add("uuid", "c1d8f5c2-e131-11de-babe-001e378eb67e");
			}
		});
	}
}
