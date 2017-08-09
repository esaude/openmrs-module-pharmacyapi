package org.openmrs.module.pharmacyapi.api.common.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.openmrs.OpenmrsObject;

/**
 * 
 *
 */
@MappedSuperclass
public abstract class BaseOpenmrsObjectWrapper implements Serializable, OpenmrsObject {
	
	private static final long serialVersionUID = 933059070810131693L;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
