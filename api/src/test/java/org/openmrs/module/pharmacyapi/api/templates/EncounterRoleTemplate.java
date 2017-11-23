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

import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EncounterRoleTemplate implements TemplateLoader {
	
	public static final String DEFAULT_ENCONTER_ROLE = "a0b03050-c99b-11e0-9572-0800200c9a66";
	
	@Override
	public void load() {
		//
		// Fixture.of(EncounterType.class).addTemplate(ARV_FOLLOW_UP_ADULT, new
		// Rule() {
		//
		// {
		// this.add("name", "S.TARV: ADULTO SEGUIMENTO");
		// this.add("description", "seguimento visita do paciente adulto");
		// this.add("dateCreated", this.instant("now"));
		// this.add("retired", false);
		// this.add("uuid", MappedEncounters.ARV_FOLLOW_UP_ADULT);
		// }
		// });
		
	}
}
