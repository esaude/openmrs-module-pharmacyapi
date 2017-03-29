/**
 *  UCSF -Global Programs 2017
 *
 */
package org.openmrs.module.pharmacyapi.api.model.stock;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.module.pharmacyapi.api.model.DrugItem;

/**
 * @author Guimino Neves
 */
public class Lot extends BaseOpenmrsMetadata implements Serializable {
	
	private static final long serialVersionUID = -8595553958148774238L;
	
	private Integer loteId;
	
	private String code;
	
	private DrugItem drugItem;
	
	private DrugMeasureUnit measureUnit;
	
	private Date expirationDate;
	
	private Date manufactureDate;
	
	@Override
	public Integer getId() {
		
		return this.loteId;
	}
	
	@Override
	public void setId(final Integer id) {
		this.loteId = id;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setCode(final String code) {
		this.code = code;
	}
	
	public DrugItem getDrugItem() {
		return this.drugItem;
	}
	
	public void setDrugItem(final DrugItem drugItem) {
		this.drugItem = drugItem;
	}
	
	public Date getManufactureDate() {
		return this.manufactureDate;
	}
	
	public void setManufactureDate(final Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	
	public Date getExpirationDate() {
		return this.expirationDate;
	}
	
	public void setExpirationDate(final Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public DrugMeasureUnit getMeasureUnit() {
		return this.measureUnit;
	}
	
	public void setMeasureUnit(final DrugMeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null) {
			return false;
		}
		
		// Vamos deixar que seja deste Jeito, apenas verificar o codigo, pois
		// este deve ser UNIQUE
		if (obj instanceof Lot) {
			final Lot other = (Lot) obj;
			return new EqualsBuilder().append(this.getCode(), other.getCode()).isEquals();
		}
		
		return false;
	}
}
