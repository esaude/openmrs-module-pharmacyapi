/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsData;

/**
 * @author Stélio Moiane
 */
public class DispensationItem extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer dispensationItemId;
	
	private String orderUuid;
	
	private Double quantityToDispense;
	
	private Double quantityDispensed;
	
	private Date dateOfNextPickUp;
	
	private DrugRegime drugRegime;
	
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
	
	public DrugRegime getDrugRegime() {
		return this.drugRegime;
	}
	
	public void setDrugRegime(final DrugRegime drugRegime) {
		this.drugRegime = drugRegime;
	}
	
	public Double getTotalDispensed() {
		return this.quantityDispensed + this.quantityToDispense;
	}
}
