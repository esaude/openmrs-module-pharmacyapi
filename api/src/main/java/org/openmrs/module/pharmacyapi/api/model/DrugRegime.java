/**
 *  UCSF -Global Programs 2017
 *  
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.util.List;

import org.openmrs.BaseOpenmrsMetadata;

/**
 * @author Guimino Neves
 */
public class DrugRegime extends BaseOpenmrsMetadata {
	
	private Integer drugRegimenId;
	
	private DrugItem drugItem;
	
	List<DrugGroup> drugGroups;
	
	@Override
	public Integer getId() {
		return this.drugRegimenId;
	}
	
	@Override
	public void setId(final Integer drugRegimenId) {
		this.drugRegimenId = drugRegimenId;
	}
	
	public DrugItem getDrugItem() {
		return this.drugItem;
	}
	
	public void setDrugItem(final DrugItem drugItem) {
		this.drugItem = drugItem;
	}
	
	public List<DrugGroup> getDrugGroups() {
		return this.drugGroups;
	}
	
	public void setDrugGroups(final List<DrugGroup> drugGroups) {
		this.drugGroups = drugGroups;
	}
}
