/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.common.validation;

import org.openmrs.OpenmrsObject;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;

/**
 *
 */
public interface IValidationRule<T extends OpenmrsObject> {
	
	public void validate(T t) throws PharmacyBusinessException;
	
}
