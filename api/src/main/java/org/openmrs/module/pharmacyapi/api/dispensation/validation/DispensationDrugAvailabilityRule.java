package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispensationDrugAvailabilityRule implements IDispensationRuleValidation {
	
	@Autowired
	private BatchService batchService;
	
	@Override
	public void validate(final Dispensation dispensation) throws PharmacyBusinessException {
		
		final List<DispensationItem> dispensationItems = dispensation.getDispensationItems();
		final Location location = Context.getLocationService().getLocationByUuid(dispensation.getLocationUuid());
		
		final List<Drug> drugsWithoutStock = new ArrayList<>();
		for (final DispensationItem dispensationItem : dispensationItems) {
			
			final DrugOrder drugOrder = (DrugOrder) Context.getOrderService()
			        .getOrderByUuid(dispensationItem.getOrderUuid());
			final Drug drug = Context.getConceptService().getDrug(drugOrder.getDrug().getDrugId());
			
			final List<Batch> batches = this.batchService.findBatchesByDrugAndLocationAndNotExpiredDate(drug, location,
			    dispensation.getDispensationDate());
			
			if (batches.isEmpty()) {
				drugsWithoutStock.add(drugOrder.getDrug());
			}
		}
		
		if (!drugsWithoutStock.isEmpty()) {
			
			throw new PharmacyBusinessException("pharmacyapi.error.insufficent.stock.for.dispensation",
			        this.getFormattedMessage(drugsWithoutStock));
			
		}
	}
	
	private String getFormattedMessage(final List<Drug> drugsWithoutStock) {
		
		String formattedString = StringUtils.EMPTY;
		for (final Drug drug : drugsWithoutStock) {
			formattedString += ", " + drug.getDisplayName();
		}
		return formattedString.replaceFirst(",", "");
	}
}
