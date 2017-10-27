/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.dispensation.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.DrugOrder;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;

/**
 * @author Stélio Moiane
 */
public class DispensationItem extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer dispensationItemId;
	
	private String orderUuid;
	
	private DrugOrder drugOrder;
	
	private Double quantityToDispense;
	
	private Double quantityDispensed;
	
	private Date dateOfNextPickUp;
	
	private String regimeUuid;
	
	private String prescriptionUuid;
	
	private Prescription prescription;
	
	private Date dispensationItemCreationDate;
	
	private Date prescriptionExpirationDate;
	
	private Dispensation dispensation;
	
	@Override
	public Integer getId() {
		return this.dispensationItemId;
	}
	
	@Override
	public void setId(final Integer dispensationItemId) {
		this.dispensationItemId = dispensationItemId;
	}
	
	public Integer getDispensationItemId() {
		return this.dispensationItemId;
	}
	
	public void setDispensationItemId(final Integer dispensationItemId) {
		this.dispensationItemId = dispensationItemId;
	}
	
	public String getOrderUuid() {
		return this.orderUuid;
	}
	
	public void setOrderUuid(final String orderUuid) {
		this.orderUuid = orderUuid;
	}
	
	public Double getQuantityToDispense() {
		return this.quantityToDispense;
	}
	
	public void setQuantityToDispense(final Double quantityToDispense) {
		this.quantityToDispense = quantityToDispense;
	}
	
	public Double getQuantityDispensed() {
		return this.quantityDispensed;
	}
	
	public void setQuantityDispensed(final Double quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	
	public Date getDateOfNextPickUp() {
		return this.dateOfNextPickUp;
	}
	
	public void setDateOfNextPickUp(final Date dateOfNextPickUp) {
		this.dateOfNextPickUp = dateOfNextPickUp;
	}
	
	public Double getTotalDispensed() {
		return this.quantityDispensed + this.quantityToDispense;
	}
	
	public String getRegimeUuid() {
		return regimeUuid;
	}
	
	public Date getDispensationItemCreationDate() {
		return dispensationItemCreationDate;
	}
	
	public void setDispensationItemCreationDate(Date dispensationItemCreationDate) {
		this.dispensationItemCreationDate = dispensationItemCreationDate;
	}
	
	public Date getPrescriptionExpirationDate() {
		return prescriptionExpirationDate;
	}
	
	public void setPrescriptionExpirationDate(Date prescriptionExpirationDate) {
		this.prescriptionExpirationDate = prescriptionExpirationDate;
	}
	
	public void setRegimeUuid(String regimeUuid) {
		this.regimeUuid = regimeUuid;
	}
	
	public String getPrescriptionUuid() {
		return prescriptionUuid;
	}
	
	public void setPrescriptionUuid(String prescriptionUuid) {
		this.prescriptionUuid = prescriptionUuid;
	}
	
	public Dispensation getDispensation() {
		return dispensation;
	}
	
	public void setDispensation(Dispensation dispensation) {
		this.dispensation = dispensation;
	}
	
	public DrugOrder getDrugOrder() {
		return drugOrder;
	}
	
	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}
	
	public Prescription getPrescription() {
		return prescription;
	}
	
	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	
}
