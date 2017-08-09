/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DispensationValidator {

	private List<IDispensationRuleValidation> rulesForCreateDispensation = new ArrayList<>();

	private List<IDispensationRuleValidation> rulesForCancellationDispensation = new ArrayList<>();

	@Autowired
	private LocationDispensationRule locationRule;

	@Autowired
	private PatientDispensationRule patientRule;

	@Autowired
	private ProviderDispensationRule providerRule;

	@Autowired
	private DispensationItemCreationRule dispensationItemCreationRule;

	@Autowired
	private DispensationItemCancelationRule dispensationItemCancelationRule;

	@PostConstruct
	private void initializeRules() {

		rulesForCreateDispensation.add(patientRule);
		rulesForCreateDispensation.add(locationRule);
		rulesForCreateDispensation.add(providerRule);
		rulesForCreateDispensation.add(dispensationItemCreationRule);

		rulesForCancellationDispensation.add(patientRule);
		rulesForCancellationDispensation.add(providerRule);
		rulesForCancellationDispensation.add(locationRule);
		rulesForCancellationDispensation.add(dispensationItemCancelationRule);

	}

	public void validateCreation(Dispensation dispensation) throws PharmacyBusinessException {

		for (IDispensationRuleValidation rule : rulesForCreateDispensation) {

			rule.validate(dispensation);
		}
	}

	public void validateCancellation(Dispensation dispensation) throws PharmacyBusinessException {

		for (IDispensationRuleValidation rule : rulesForCancellationDispensation) {

			rule.validate(dispensation);
		}
	}
}
