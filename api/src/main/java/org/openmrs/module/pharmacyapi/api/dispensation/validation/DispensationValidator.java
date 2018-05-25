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
package org.openmrs.module.pharmacyapi.api.dispensation.validation;

import java.util.ArrayList;
import java.util.Date;
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
	
	private final List<IDispensationRuleValidation> rulesForCreateDispensation = new ArrayList<>();
	
	private final List<IDispensationRuleValidation> rulesForCancellationDispensation = new ArrayList<>();
	
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
		
		this.rulesForCreateDispensation.add(this.patientRule);
		this.rulesForCreateDispensation.add(this.locationRule);
		this.rulesForCreateDispensation.add(this.providerRule);
		this.rulesForCreateDispensation.add(this.dispensationItemCreationRule);
		
		this.rulesForCancellationDispensation.add(this.patientRule);
		this.rulesForCancellationDispensation.add(this.providerRule);
		this.rulesForCancellationDispensation.add(this.locationRule);
		this.rulesForCancellationDispensation.add(this.dispensationItemCancelationRule);
		
	}
	
	public void validateCreation(final Dispensation dispensation) throws PharmacyBusinessException {
		
		for (final IDispensationRuleValidation rule : this.rulesForCreateDispensation) {
			
			rule.validate(dispensation);
		}
	}
	
	public void validateCancellation(final Dispensation dispensation, final Date date)
	        throws PharmacyBusinessException {
		
		for (final IDispensationRuleValidation rule : this.rulesForCancellationDispensation) {
			
			rule.validate(dispensation);
		}
	}
}
