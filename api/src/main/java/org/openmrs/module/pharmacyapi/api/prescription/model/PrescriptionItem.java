/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.prescription.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;

/**
 *
 *
 */
public class PrescriptionItem extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum PrescriptionItemStatus {
		NEW, ACTIVE, FINALIZED, INTERRUPTED, EXPIRED
	}
	
	private DrugOrder drugOrder;
	
	private Double drugToPickUp;
	
	private Double drugPickedUp;
	
	private String dosingInstructions;
	
	private Prescription prescription;
	
	private Concept arvPlan;
	
	private Concept regime;
	
	private Concept therapeuticLine;
	
	private Concept changeReason;
	
	private Concept interruptionReason;
	
	private Date expectedNextPickUpDate;
	
	private PrescriptionItemStatus status = PrescriptionItemStatus.NEW;
	
	public PrescriptionItem() {
		this.drugPickedUp = 0.0;
		this.drugToPickUp = 0.0;
	}
	
	public PrescriptionItem(final DrugOrder drugOrder) {
		this.drugPickedUp = 0.0;
		this.drugToPickUp = 0.0;
		this.drugOrder = drugOrder;
	}
	
	public DrugOrder getDrugOrder() {
		return this.drugOrder;
	}
	
	public void setDrugOrder(final DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}
	
	public Double getDrugToPickUp() {
		return this.drugToPickUp;
	}
	
	public void setDrugToPickUp(final Double drugToPickUp) {
		this.drugToPickUp = drugToPickUp;
	}
	
	public Double getDrugPickedUp() {
		return this.drugPickedUp;
	}
	
	public void setDrugPickedUp(final Double drugPickedUp) {
		this.drugPickedUp = drugPickedUp;
	}
	
	public Concept getRegime() {
		return this.regime;
	}
	
	public void setRegime(final Concept regime) {
		this.regime = regime;
	}
	
	public String getDosingInstructions() {
		return this.dosingInstructions;
	}
	
	public void setDosingInstructions(final String dosingInstructions) {
		this.dosingInstructions = dosingInstructions;
	}
	
	public Prescription getPrescription() {
		return this.prescription;
	}
	
	public void setPrescription(final Prescription prescription) {
		this.prescription = prescription;
	}
	
	public Concept getArvPlan() {
		return this.arvPlan;
	}
	
	public void setArvPlan(final Concept arvPlan) {
		this.arvPlan = arvPlan;
	}
	
	public Concept getChangeReason() {
		return this.changeReason;
	}
	
	public void setChangeReason(final Concept changeReason) {
		this.changeReason = changeReason;
	}
	
	public Concept getTherapeuticLine() {
		return this.therapeuticLine;
	}
	
	public void setTherapeuticLine(final Concept therapeuticLine) {
		this.therapeuticLine = therapeuticLine;
	}
	
	public Concept getInterruptionReason() {
		return this.interruptionReason;
	}
	
	public void setInterruptionReason(final Concept interruptionReason) {
		this.interruptionReason = interruptionReason;
	}
	
	public PrescriptionItemStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(final PrescriptionItemStatus status) {
		this.status = status;
	}
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(final Integer arg0) {
	}
	
	public Date getExpectedNextPickUpDate() {
		return this.expectedNextPickUpDate;
	}
	
	public void setExpectedNextPickUpDate(final Date expectedNextPickUpDate) {
		this.expectedNextPickUpDate = expectedNextPickUpDate;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
		        .append("uuid", this.getUuid()).append("status", this.getStatus())
		        .append("drugOrder", this.getDrugOrder()).append("regime", this.getRegime()).toString();
	}
}
