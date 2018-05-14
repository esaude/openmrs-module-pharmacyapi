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
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.service.dispensationservice;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.pharmacyapi.api.templates.LocationTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PatientTemplate;
import org.openmrs.module.pharmacyapi.api.templates.ProviderTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;

/**
 * @author Stélio Moiane
 */
public class DispensationServiceTest extends BaseTest {
	
	@Test
	@Ignore
	public void shouldDispenseOrdersForNonArvPrescription() throws Exception {
		this.executeDataSet("dispensationservice/shouldDispenseOrdersForNonArvPrescription-dataset.xml");
		
		final String drugOrderUuid1 = "921de0a3-05c4-444a-be03-0001";
		final String drugOrderUuid2 = "921de0a3-05c4-444a-be03-0002";
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2005);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		final Dispensation dispensation = new Dispensation();
		
		dispensation.setPatientUuid(PatientTemplate.MR_HORATIO);
		dispensation.setLocationUuid(LocationTemplate.XANADU);
		dispensation.setProviderUuid(ProviderTemplate.TEST);
		
		final DispensationItem item1 = new DispensationItem();
		item1.setDrugOrder((DrugOrder) Context.getOrderService().getOrderByUuid(drugOrderUuid1));
		item1.setQuantityToDispense(10d);
		item1.setDateOfNextPickUp(new Date());
		
		final DispensationItem item2 = new DispensationItem();
		item2.setDrugOrder((DrugOrder) Context.getOrderService().getOrderByUuid(drugOrderUuid2));
		item2.setQuantityToDispense(5d);
		item2.setDateOfNextPickUp(new Date());
		dispensation.setDispensationItems(Arrays.asList(item1, item2));
		
		final Dispensation createdDispensation = Context.getService(DispensationService.class).dispense(dispensation);
		
		Assert.assertNotNull(createdDispensation);
	}
}
