/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.BaseOpenmrsData;

/**
 * @author Stélio Moiane
 */
public class DispensationWrapper extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer dispensationId;
	
	private String providerUuid;
	
	private String patientUuid;
	
	private String locationUuid;
	
	private List<DispensationWrapperItem> dispensationItems;
	
	public DispensationWrapper() {
		this.dispensationItems = new ArrayList<>();
	}
	
	@Override
	public Integer getId() {
		return this.dispensationId;
	}
	
	@Override
	public void setId(final Integer dispensationId) {
		this.dispensationId = dispensationId;
	}
	
	public Integer getDispensationId() {
		return this.dispensationId;
	}
	
	public void setDispensationId(final Integer dispensationId) {
		this.dispensationId = dispensationId;
	}
	
	public void setProviderUuid(final String providerUuid) {
		this.providerUuid = providerUuid;
	}
	
	public String getProviderUuid() {
		return this.providerUuid;
	}
	
	public List<DispensationWrapperItem> getDispensationItems() {
		return this.dispensationItems;
	}
	
	public void setDispensationItems(final List<DispensationWrapperItem> dispensationItems) {
		this.dispensationItems = dispensationItems;
	}
	
	public void setPatientUuid(final String patientUuid) {
		this.patientUuid = patientUuid;
	}
	
	public String getPatientUuid() {
		return this.patientUuid;
	}
	
	public void setLocationUuid(final String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getLocationUuid() {
		return this.locationUuid;
	}
}
