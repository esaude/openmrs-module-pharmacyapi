/**
 *
 *  UCSF -Global Programs 2017
 *
 */
package org.openmrs.module.pharmacyapi.api.model.stock;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Location;

/**
 * @author Guimino Neves
 */
public class StockEntry extends BaseOpenmrsMetadata implements Serializable {
	
	private static final long serialVersionUID = -6798023671384203411L;
	
	private Integer stockEntryId;
	
	private Lot lot;
	
	private Integer quantity;
	
	private Location location;
	
	private Supplier provenance;
	
	private Date receptionDate;
	
	@Override
	public Integer getId() {
		return this.stockEntryId;
	}
	
	@Override
	public void setId(final Integer id) {
		this.stockEntryId = id;
	}
	
	public Integer getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(final Integer quantity) {
		this.quantity = quantity;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}
	
	public Lot getLot() {
		return this.lot;
	}
	
	public void setLot(final Lot lot) {
		this.lot = lot;
	}
	
	public Supplier getProvenance() {
		return this.provenance;
	}
	
	public void setProvenance(final Supplier provenance) {
		this.provenance = provenance;
	}
	
	public Date getReceptionDate() {
		return this.receptionDate;
	}
	
	public void setReceptionDate(final Date receptionDate) {
		this.receptionDate = receptionDate;
	}
}
