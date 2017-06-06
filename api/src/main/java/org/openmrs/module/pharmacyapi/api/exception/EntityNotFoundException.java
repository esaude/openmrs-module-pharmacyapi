/**
 *
 */
package org.openmrs.module.pharmacyapi.api.exception;

/**
 *
 */
public class EntityNotFoundException extends PharmacyBusinessException {
	
	private static final long serialVersionUID = -9192113343142419344L;
	
	public EntityNotFoundException(final Class<?> object, final String... parameters) {
		super("Entity [" + object + "] not found for parameters = " + parameters);
	}
	
}
