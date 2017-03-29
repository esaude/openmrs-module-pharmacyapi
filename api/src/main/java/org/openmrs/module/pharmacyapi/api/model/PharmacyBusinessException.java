/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import org.openmrs.api.APIException;

/**
 * To handle exceptions related to the Pharmacy Business, for each specific rule validation should
 * be created a specific exception that extends @PharmacyBusinessException, also we can consider
 * this class
 * 
 * @author Guimino Neves
 */
public class PharmacyBusinessException extends APIException {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -1163889206769856060L;
	
	public PharmacyBusinessException(final String message) {
		super(message);
	}
}
