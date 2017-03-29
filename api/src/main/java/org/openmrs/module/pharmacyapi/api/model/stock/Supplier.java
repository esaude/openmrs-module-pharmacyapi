/**
 *
 *  UCSF -Global Programs 2017
 *
 */
package org.openmrs.module.pharmacyapi.api.model.stock;

import java.util.Date;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Location;

/**
 * @author Guimino Neves
 */
public class Supplier extends BaseOpenmrsMetadata {
	
	private Integer supplierId;
	
	private String name;
	
	private String address;
	
	private Date registrationDate;
	
	private Location location;
	
	@Override
	public Integer getId() {
		return this.supplierId;
	}
	
	@Override
	public void setId(final Integer id) {
		this.supplierId = id;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(final String address) {
		this.address = address;
	}
	
	public Date getRegistrationDate() {
		return this.registrationDate;
	}
	
	public void setRegistrationDate(final Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}
	
}
