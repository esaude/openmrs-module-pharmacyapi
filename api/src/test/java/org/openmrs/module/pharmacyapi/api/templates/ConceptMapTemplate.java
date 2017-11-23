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
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.module.pharmacyapi.api.util.BaseTemplateLoader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;

public class ConceptMapTemplate implements BaseTemplateLoader {
	
	public static final String VALID = "VALID";
	
	@Override
	public void load() {
		
		Fixture.of(ConceptMap.class).addTemplate(VALID, new Rule() {
			
			{
				this.add("concept", this.one(Concept.class, ConceptTemplate.DURATION_WEEKS));
				this.add("conceptMapType", this.one(ConceptMapType.class, ConceptMapTypeTemplate.VALID));
				this.add("conceptReferenceTerm", this.one(ConceptReferenceTerm.class, ConceptReferenceTermTemplate.VALID));
			}
		});
	}
}
