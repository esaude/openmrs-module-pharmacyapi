/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class PersonTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		Fixture.of(Person.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("gender", this.random("M", "F"));
				this.add("birthdate", this.instant("now"));
				this.add("names", this.has(1).of(PersonName.class, PersonNameTemplate.VALID));
			}
		});
	}
}
