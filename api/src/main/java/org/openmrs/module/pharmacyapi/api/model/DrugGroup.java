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
public class DrugGroup extends BaseOpenmrsMetadata {
	
	private Integer drugGroupId;
	
	List<DrugGroupMember> members;
	
	@Override
	public Integer getId() {
		return this.drugGroupId;
	}
	
	@Override
	public void setId(final Integer drugGroupId) {
		
		this.drugGroupId = drugGroupId;
	}
	
	public List<DrugGroupMember> getMembers() {
		return this.members;
	}
	
	public void setMembers(final List<DrugGroupMember> members) {
		this.members = members;
	}
}
