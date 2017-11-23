/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.templates;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;

import br.com.six2six.fixturefactory.processor.Processor;

/**
 * @author Stélio Moiane
 */
public class OrderProcessor implements Processor {
	
	@Override
	public void execute(final Object result) {
		
		if (!(result instanceof DrugOrder)) {
			return;
		}
		
		final DrugOrder order = (DrugOrder) result;
		final List<Obs> observations = new ArrayList<>();
		
		DrugOrder temOrder = order;
		while (temOrder.getPreviousOrder() != null) {
			observations.addAll(temOrder.getEncounter().getObs());
			temOrder = (DrugOrder) temOrder.getPreviousOrder();
		}
		
		for (final Obs observation : observations) {
			
			if (MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())) {
				observation.setOrder(order);
			}
		}
	}
}
