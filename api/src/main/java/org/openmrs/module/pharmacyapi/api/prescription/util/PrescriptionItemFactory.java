/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.prescription.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Order.Action;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionItemFactory {
	
	@Autowired
	private NewPrescriptionItemGenerator newPrescriptionItemGenerator;
	
	@Autowired
	private RevisePrescriptionItemGenerator revisePrescriptionItemGenerator;
	
	@Autowired
	private DiscountinuePrescriptionItemGenerator dIscountinuePrescriptionItemGenerator;
	
	public List<PrescriptionItem> generatePrescriptionItems(final Prescription prescription, final Date creationDate,
	        final List<DrugOrder> drugOrders) throws PharmacyBusinessException {
		
		final List<PrescriptionItem> items = new ArrayList<>();
		
		for (final DrugOrder drugOrder : drugOrders) {
			final PrescriptionItem item = this.generatePrescriptionItem(prescription, drugOrder, creationDate);
			item.setPrescription(prescription);
			items.add(item);
		}
		return items;
	}
	
	private PrescriptionItem generatePrescriptionItem(final Prescription prescription, final DrugOrder drugOrder,
	        final Date creationDate)
	        throws PharmacyBusinessException {
		
		if (Action.NEW.equals(drugOrder.getAction())) {
			return this.newPrescriptionItemGenerator.generate(prescription, drugOrder, creationDate);
		}
		
		if (Action.REVISE.equals(drugOrder.getAction())) {
			return this.revisePrescriptionItemGenerator.generate(prescription, drugOrder, creationDate);
		}
		
		if (Action.DISCONTINUE.equals(drugOrder.getAction())) {
			return this.dIscountinuePrescriptionItemGenerator.generate(prescription, drugOrder, creationDate);
		}
		throw new IllegalArgumentException("failed to parse drugOrder with order action " + drugOrder.getAction()
		        + " and uuid " + drugOrder.getUuid());
	}
}
