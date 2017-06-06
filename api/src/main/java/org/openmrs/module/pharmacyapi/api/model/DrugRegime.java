/**
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

import org.openmrs.Concept;
import org.openmrs.module.pharmacyapi.api.dao.DrugRegimeDAO;

@NamedQueries(value = {
        @NamedQuery(name = DrugRegimeDAO.QUERY_NAME.findByRegime, query = DrugRegimeDAO.QUERY.findByRegime),
        @NamedQuery(name = DrugRegimeDAO.QUERY_NAME.findByRegimeAndDrugItem, query = DrugRegimeDAO.QUERY.findByRegimeAndDrugItem),
        @NamedQuery(name = DrugRegimeDAO.QUERY_NAME.findByDrugUuid, query = DrugRegimeDAO.QUERY.findByDrugUuid) })
@Entity
@Table(name = "phm_drug_regime", uniqueConstraints = { @UniqueConstraint(columnNames = { "drug_item_id", "regime_id" }) })
public class DrugRegime extends BaseOpenmrsMetadataWrapper implements Serializable {
	
	private static final long serialVersionUID = -3770809635357840242L;
	
	@Id
	@GeneratedValue
	@Column(name = "drug_regime_id")
	private Integer drugRegimeId;
	
	@ManyToOne
	@JoinColumn(name = "regime_id")
	private Concept regime;
	
	@ManyToOne
	@JoinColumn(name = "drug_item_id")
	private DrugItem drugItem;
	
	public DrugRegime() {
	}
	
	@Override
	public Integer getId() {
		return this.drugRegimeId;
	}
	
	@Override
	public void setId(final Integer id) {
		this.drugRegimeId = id;
	}
	
	public Concept getRegime() {
		return this.regime;
	}
	
	public void setRegime(final Concept regime) {
		
		this.regime = regime;
	}
	
	public DrugItem getDrugItem() {
		return this.drugItem;
	}
	
	public void setDrugItem(final DrugItem drugItem) {
		this.drugItem = drugItem;
	}
}
