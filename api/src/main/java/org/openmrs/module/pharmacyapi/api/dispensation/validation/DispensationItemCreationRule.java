/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order.Action;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.springframework.stereotype.Component;

@Component
public class DispensationItemCreationRule implements IDispensationRuleValidation {
	
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
			
			if (dispensationItem.getQuantityToDispense() == null
			        || dispensationItem.getQuantityToDispense().doubleValue() <= 0) {
				
				throw new PharmacyBusinessException(
				        "The Order to be Dispensed must have valid quantity to Dispense. Order : " + order
				                + " Quantity passed: " + dispensationItem.getQuantityToDispense());
			}
			
			if (Action.DISCONTINUE.equals(order.getAction())) {
				
				throw new PharmacyBusinessException(
				        "The Order to be Dispensed must have Order Action in 'NEW' or 'REVISE'. order uuid: "
				                + dispensationItem.getOrderUuid());
			}
			
			if (dispensationItem.getQuantityToDispense().doubleValue() > order.getQuantity().doubleValue()) {
				
				throw new PharmacyBusinessException(
				        "The quantity to Be Dispensed must be less or equals the Order  Quantity : " + order
				                + " Quantity passed: " + dispensationItem.getQuantityToDispense());
			}
			
			if (order.isVoided()) {
				throw new PharmacyBusinessException("Cannot Dispense A voided Item" + order);
			}
			
			Encounter prescriptionEncounter = Context.getEncounterService().getEncounterByUuid(
			    dispensationItem.getPrescriptionUuid());
			
			if (prescriptionEncounter == null) {
				
				throw new PharmacyBusinessException("Encounter of Prescription not Found for Dispensation Item " + order);
			}
			
			Patient patient = Context.getPatientService().getPatientByUuid(dispensation.getPatientUuid());
			
			EncounterType encounterType = Context.getService(PharmacyHeuristicService.class).getEncounterTypeByPatientAge(
			    patient);
			
			if (!encounterType.equals(prescriptionEncounter.getEncounterType())) {
				throw new PharmacyBusinessException(" Encounter of prescription must be of " + encounterType.getName()
				        + " for DispensationItem " + order);
			}
		}
	}
}
