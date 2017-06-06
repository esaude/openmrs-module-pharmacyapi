package org.openmrs.module.pharmacyapi.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.Field;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.OpenmrsMetadata;
import org.openmrs.User;

@MappedSuperclass
public abstract class BaseOpenmrsMetadataWrapper extends BaseOpenmrsObject implements OpenmrsMetadata {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "creator")
	private User creator;
	
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@Column(name = "retired", nullable = false)
	@Field
	private Boolean retired = Boolean.FALSE;
	
	@Column(name = "date_retired")
	private Date dateRetired;
	
	@ManyToOne
	@JoinColumn(name = "retired_by")
	private User retiredBy;
	
	@Column(name = "retire_reason", length = 255)
	private String retireReason;
	
	@ManyToOne
	@JoinColumn(name = "changed_by")
	private User changedBy;
	
	@Column(name = "date_changed")
	private Date dateChanged;
	
	//
	// ***** Constructors *****
	
	/**
	 * Default Constructor
	 */
	public BaseOpenmrsMetadataWrapper() {
	}
	
	// ***** Property Access *****
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return null;
	}
	
	/**
	 * @param name the name to set
	 */
	@Override
	public void setName(final String name) {
	}
	
	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return null;
	}
	
	/**
	 * @param description the description to set
	 */
	@Override
	public void setDescription(final String description) {
	}
	
	/**
	 * @see org.openmrs.Auditable#getCreator()
	 */
	@Override
	public User getCreator() {
		return this.creator;
	}
	
	/**
	 * @see org.openmrs.Auditable#setCreator(org.openmrs.User)
	 */
	@Override
	public void setCreator(final User creator) {
		this.creator = creator;
	}
	
	/**
	 * @see org.openmrs.Auditable#getDateCreated()
	 */
	@Override
	public Date getDateCreated() {
		return this.dateCreated;
	}
	
	/**
	 * @see org.openmrs.Auditable#setDateCreated(java.util.Date)
	 */
	@Override
	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * @see org.openmrs.Retireable#isRetired()
	 */
	@Override
	public Boolean isRetired() {
		return this.retired;
	}
	
	/**
	 * This method delegates to {@link #isRetired()}. This is only needed for jstl syntax like
	 * ${fieldType.retired} because the return type is a Boolean object instead of a boolean
	 * primitive type.
	 * 
	 * @see org.openmrs.Retireable#isRetired()
	 */
	public Boolean getRetired() {
		return this.isRetired();
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetired(java.lang.Boolean)
	 */
	@Override
	public void setRetired(final Boolean retired) {
		this.retired = retired;
	}
	
	/**
	 * @see org.openmrs.Retireable#getDateRetired()
	 */
	@Override
	public Date getDateRetired() {
		return this.dateRetired;
	}
	
	/**
	 * @see org.openmrs.Retireable#setDateRetired(java.util.Date)
	 */
	@Override
	public void setDateRetired(final Date dateRetired) {
		this.dateRetired = dateRetired;
	}
	
	/**
	 * @see org.openmrs.Retireable#getRetiredBy()
	 */
	@Override
	public User getRetiredBy() {
		return this.retiredBy;
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetiredBy(org.openmrs.User)
	 */
	@Override
	public void setRetiredBy(final User retiredBy) {
		this.retiredBy = retiredBy;
	}
	
	/**
	 * @see org.openmrs.Retireable#getRetireReason()
	 */
	@Override
	public String getRetireReason() {
		return this.retireReason;
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetireReason(java.lang.String)
	 */
	@Override
	public void setRetireReason(final String retireReason) {
		this.retireReason = retireReason;
	}
	
	@Override
	public User getChangedBy() {
		return this.changedBy;
	}
	
	@Override
	public void setChangedBy(final User changedBy) {
		this.changedBy = changedBy;
	}
	
	@Override
	public Date getDateChanged() {
		return this.dateChanged;
	}
	
	@Override
	public void setDateChanged(final Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
}
