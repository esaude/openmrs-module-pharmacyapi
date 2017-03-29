/**
 * 
 *  UCSF -Global Programs 2017
 *  
 */
package org.openmrs.module.pharmacyapi.api.model;

/**
 * @author Guimino Neves
 */
public class DrugGroupMember { // extends BaseOpenmrsMetadata {

	private Integer drugGroupMemberId;
	
	private DrugGroup drugGroup;
	
	private DrugItem drugItem;
	
	// @Override
	public Integer getId() {
		
		return this.drugGroupMemberId;
	}
	
	// @Override
	public void setId(final Integer id) {
		
		this.drugGroupMemberId = id;
	}
	
	public DrugGroup getDrugGroup() {
		return this.drugGroup;
	}
	
	public void setDrugGroup(final DrugGroup drugGroup) {
		this.drugGroup = drugGroup;
	}
	
	public DrugItem getDrugItem() {
		return this.drugItem;
	}
	
	public void setDrugItem(final DrugItem drugItem) {
		this.drugItem = drugItem;
	}
}
