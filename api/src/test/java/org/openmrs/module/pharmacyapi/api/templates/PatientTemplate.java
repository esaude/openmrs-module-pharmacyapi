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
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Patient;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

/**
 * @author Stélio Moiane
 */
public class PatientTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "valid";
	
	public static final String MR_HORATIO = "da7f524f-27ce-4bb2-86d6-6d1d05312bd5";
	
	@Override
	public void load() {
		Fixture.of(Patient.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("gender", this.random("M", "F"));
				this.add("birthdate", this.instant("now"));
				this.add("birthdateEstimated", this.random(true, false));
			}
		});
		
		Fixture.of(Patient.class).addTemplate(MR_HORATIO, new Rule() {
			
			{
				this.add("uuid", MR_HORATIO);
			}
		});
	}
}
