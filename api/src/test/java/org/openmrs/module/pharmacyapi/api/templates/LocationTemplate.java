/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Location;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class LocationTemplate implements TemplateLoader {
	
	public static final String XANADU = "9356400c-a5a2-4532-8f2b-2361b3446eb8";
	
	@Override
	public void load() {
		
		Fixture.of(Location.class).addTemplate(XANADU, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", XANADU);
			}
		});
	}
}
