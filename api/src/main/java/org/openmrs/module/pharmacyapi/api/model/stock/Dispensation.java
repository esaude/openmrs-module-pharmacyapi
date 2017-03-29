/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model.stock;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Order;

/**
 * Responsible to register all dispensed Drugs, has a redundancy on quantity
 * atributte so that will help the queries.
 *
 * @author Guimino Neves
 */
public class Dispensation extends BaseOpenmrsMetadata implements Serializable {

	private Integer dispensationId;

	private Order order;

	private StockEntry stockEntry;

	private Integer quantity;

	private static final long serialVersionUID = 7261168854447802473L;

	@Override
	public Integer getId() {

		return this.dispensationId;
	}

	@Override
	public void setId(final Integer id) {

		this.dispensationId = id;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(final Order order) {
		this.order = order;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(final Integer quantity) {
		this.quantity = quantity;

	}

	public StockEntry getStockEntry() {
		return this.stockEntry;
	}

	public void setStockEntry(final StockEntry stockEntry) {
		this.stockEntry = stockEntry;
	}
}
