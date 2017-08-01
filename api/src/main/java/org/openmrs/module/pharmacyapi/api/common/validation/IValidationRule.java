/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.common.validation;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;

/**
 *
 */
public interface IValidationRule<T extends BaseOpenmrsData> {
	
	public void validate(T t) throws PharmacyBusinessException;
	
}
