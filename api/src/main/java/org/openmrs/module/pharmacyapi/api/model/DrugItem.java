/**
 *
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.module.pharmacyapi.api.dao.DrugItemDAO;

/**
 *
 */
@NamedQueries(value = { @NamedQuery(name = DrugItemDAO.QUERY_NAME.findByDrug, query = DrugItemDAO.QUERY.findByDrug),
        @NamedQuery(name = DrugItemDAO.QUERY_NAME.findByUUID, query = DrugItemDAO.QUERY.findByUUID) })
@Entity
@Table(name = "phm_drug_items", uniqueConstraints = { @UniqueConstraint(columnNames = { "drug_id" }),
        @UniqueConstraint(columnNames = { "fnm_code" }) })
public class DrugItem extends BaseOpenmrsMetadataWrapper implements Serializable {
	
	private static final long serialVersionUID = -2778691803288646029L;

	@Id
	@GeneratedValue
	@Column(name = "drug_item_id")
	private Integer drugItemId;
	
	@ManyToOne
	@JoinColumn(name = "drug_id")
	private Drug drug;
	
	@Column(name = "fnm_code")
	private String fnmCode;

	@ManyToOne
	@JoinColumn(name = "pharmaceutical_form_id")
	private Concept pharmaceuticalForm;

	@ManyToOne
	@JoinColumn(name = "therapeutic_group_id")
	private Concept therapeuticGroup;

	@ManyToOne
	@JoinColumn(name = "therapeutic_class_id")
	private Concept therapeuticClass;
	
	@Override
	public Integer getId() {
		return this.drugItemId;
	}
	
	@Override
	public void setId(final Integer drugItemId) {
		this.drugItemId = drugItemId;
	}
	
	public Drug getDrug() {
		return this.drug;
	}
	
	public void setDrug(final Drug drug) {
		this.drug = drug;
	}

	public Integer getDrugItemId() {
		return this.drugItemId;
	}
	
	public void setDrugItemId(final Integer drugItemId) {
		this.drugItemId = drugItemId;
	}
	
	public String getFnmCode() {
		return this.fnmCode;
	}
	
	public void setFnmCode(final String fnmCode) {
		this.fnmCode = fnmCode;
	}
	
	public Concept getPharmaceuticalForm() {
		return this.pharmaceuticalForm;
	}
	
	public void setPharmaceuticalForm(final Concept pharmaceuticalForm) {
		this.pharmaceuticalForm = pharmaceuticalForm;
	}
	
	public Concept getTherapeuticGroup() {
		return this.therapeuticGroup;
	}
	
	public void setTherapeuticGroup(final Concept therapeuticGroup) {
		this.therapeuticGroup = therapeuticGroup;
	}

	public Concept getTherapeuticClass() {
		return this.therapeuticClass;
	}

	public void setTherapeuticClass(final Concept therapeuticClass) {
		this.therapeuticClass = therapeuticClass;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == null) {
			return false;
		}

		if (obj instanceof DrugItem) {
			final DrugItem other = (DrugItem) obj;
			return new EqualsBuilder().append(this.getDrug(), other.getDrug()).isEquals();
		}

		return false;
	}
}
