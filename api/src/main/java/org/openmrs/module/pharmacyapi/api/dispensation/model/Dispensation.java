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
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.dispensation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.BaseOpenmrsData;

/**
 * @author Stélio Moiane
 */
public class Dispensation extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer dispensationId;
	
	private String providerUuid;
	
	private String patientUuid;
	
	private String locationUuid;
	
	private Date dispensationDate;
	
	private List<DispensationItem> dispensationItems;
	
	public Dispensation() {
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
	
	public List<DispensationItem> getDispensationItems() {
		return this.dispensationItems;
	}
	
	public void setDispensationItems(final List<DispensationItem> dispensationItems) {
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
	
	public Date getDispensationDate() {
		return this.dispensationDate;
	}
	
	public void setDispensationDate(final Date dispensationDate) {
		this.dispensationDate = dispensationDate;
	}
	
}
