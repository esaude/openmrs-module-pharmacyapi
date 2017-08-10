/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.springframework.stereotype.Component;

@Component
public class DispensationItemCancelationRule implements IDispensationRuleValidation {
	
	@Override
	public void validate(Dispensation dispensation) throws PharmacyBusinessException {
		
		if (dispensation == null) {
			
			throw new PharmacyBusinessException(" Invalid dispensation argument");
		}
		
		if (dispensation.getDispensationItems() == null || dispensation.getDispensationItems().isEmpty()) {
			
			throw new PharmacyBusinessException("No provided Item(s) of drugOrder to be Dispensed");
		}
		
		for (DispensationItem dispensationItem : dispensation.getDispensationItems()) {
			
			DrugOrder order = (DrugOrder) Context.getOrderService().getOrderByUuid(dispensationItem.getOrderUuid());
			
			if (order == null) {
				
				throw new PharmacyBusinessException("No Order found for given uuid: " + dispensationItem.getOrderUuid());
			}
		}
	}
}
