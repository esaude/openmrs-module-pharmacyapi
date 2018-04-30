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

import java.util.Calendar;
import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem.PrescriptionItemStatus;
import org.springframework.stereotype.Component;

@Component
public class NewPrescriptionItemGenerator extends AbstractPrescriptionItemGenerator {

	@Override
	public PrescriptionItem generate(final DrugOrder drugOrder, final Date creationDate)
			throws PharmacyBusinessException {

		final DrugOrder fetchedDO = this.fetchDrugOrder(drugOrder);

		final PrescriptionItem prescriptionItem = new PrescriptionItem(fetchedDO);
		prescriptionItem.setStatus(this.calculatePrescriptionItemStatus(prescriptionItem, creationDate));
		prescriptionItem.setDrugPickedUp(0d);
		prescriptionItem.setDrugToPickUp(fetchedDO.getQuantity());
		this.setPrescriptionInstructions(prescriptionItem, fetchedDO);
		this.setArvDataFields(drugOrder, prescriptionItem);

		return prescriptionItem;
	}

	@Override
	protected PrescriptionItemStatus calculatePrescriptionItemStatus(final PrescriptionItem item,
			final Date consultationDate) {

		return this.isOrderExpired(item, consultationDate) ? PrescriptionItemStatus.EXPIRED
				: PrescriptionItemStatus.NEW;
	}

	@Override
	public boolean isOrderExpired(final PrescriptionItem item, final Date consultationDate) {

		final Date expirationDate = item.getExpirationDate();

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(expirationDate);
		calendar.add(Calendar.DAY_OF_MONTH, 10);

		while ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				|| (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {

			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return consultationDate.after(calendar.getTime());
	}
}
