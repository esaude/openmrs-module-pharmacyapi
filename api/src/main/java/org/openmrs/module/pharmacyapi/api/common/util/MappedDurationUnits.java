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
