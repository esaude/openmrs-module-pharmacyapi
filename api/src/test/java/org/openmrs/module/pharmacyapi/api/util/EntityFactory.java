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
package org.openmrs.module.pharmacyapi.api.util;

import java.util.List;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.processor.Processor;

/**
 * @author Stélio Moiane
 */
public class EntityFactory {
	
	public static <T> T gimme(final Class<T> clazz, final String label) {
		return Fixture.from(clazz).gimme(label);
	}
	
	public static <T> T gimme(final Class<T> clazz, final String label, final Processor processor) {
		return Fixture.from(clazz).uses(processor).gimme(label);
	}
	
	public static <T> List<T> gimme(final Class<T> clazz, final int elements, final String... labels) {
		return Fixture.from(clazz).gimme(elements, labels);
	}
	
	public static <T> List<T> gimme(final Class<T> clazz, final int elements, final String label) {
		return Fixture.from(clazz).gimme(elements, label);
	}
}
