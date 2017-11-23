/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/**
 *
 */
package org.openmrs.module.pharmacyapi.api.drugregime.model;

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
import org.openmrs.module.pharmacyapi.api.common.model.BaseOpenmrsMetadataWrapper;
import org.openmrs.module.pharmacyapi.api.drugitem.model.DrugItem;
import org.openmrs.module.pharmacyapi.api.drugregime.dao.DrugRegimeDAO;

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
