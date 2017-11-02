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
package org.openmrs.module.pharmacyapi.api.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stélio Moiane
 */
public class MappedDurationUnits {
	
	private static Map<String, Integer> durationUnits;
	
	static {
		durationUnits = new HashMap<>();
		
		// Minutes
		durationUnits.put("1e5705ee-10f5-11e5-9009-0242ac110012", 1);
		
		// Hours
		durationUnits.put("9d956959-10e8-11e5-9009-0242ac110012", 1);
		
		// days
		durationUnits.put("9d6f51fb-10e8-11e5-9009-0242ac110012", 1);
		
		// weeks
		durationUnits.put("9d96489b-10e8-11e5-9009-0242ac110012", 7);
		
		// months
		durationUnits.put("9d96d012-10e8-11e5-9009-0242ac110012", 30);
	}
	
	public static int getDurationDays(final String durationUnit) {
		return durationUnits.get(durationUnit);
	}
}
