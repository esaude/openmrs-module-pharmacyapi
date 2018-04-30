/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacyapi.api.service.prescriptionservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription.PrescriptionStatus;
import org.openmrs.module.pharmacyapi.api.prescription.util.PrescriptionGenerator;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public class PrescriptionGeneratorTest extends BaseTest {
	
	@Autowired
	private PrescriptionGenerator prescriptionGenerator;
	
	@Test
	public void shouldGenerateNonArvPrescriptionWithActiveStatus() throws Exception {
		this.executeDataSet("prescriptionservice/shouldGenerateNonArvPrescriptionWithActiveStatus-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 10);
		calendar.set(Calendar.DAY_OF_MONTH, 8);
		final Date date = calendar.getTime();
		
		final List<DrugOrder> drugOrders = new ArrayList<>();
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(100));
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(101));
		
		final List<Prescription> prescriptions = this.prescriptionGenerator.generatePrescriptions(drugOrders, date);
		
		Assert.assertEquals(1, prescriptions.size());
		final Prescription prescription = prescriptions.get(0);
		Assert.assertEquals(PrescriptionStatus.ACTIVE, prescription.getPrescriptionStatus());
		Assert.assertFalse(prescription.isArv());
		Assert.assertEquals(2, prescription.getPrescriptionItems().size());
	}
	
	@Test
	public void shouldGenerateNonArvPrescriptionWithExpiredStatus() throws Exception {
		this.executeDataSet("prescriptionservice/shouldGenerateNonArvPrescriptionWithExpiredStatus-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 7);
		final Date date = calendar.getTime();
		
		final List<DrugOrder> drugOrders = new ArrayList<>();
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(100));
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(101));
		
		final List<Prescription> prescriptions = this.prescriptionGenerator.generatePrescriptions(drugOrders, date);
		
		Assert.assertEquals(1, prescriptions.size());
		final Prescription prescription = prescriptions.get(0);
		Assert.assertEquals(PrescriptionStatus.EXPIRED, prescription.getPrescriptionStatus());
		Assert.assertFalse(prescription.isArv());
		Assert.assertEquals(2, prescription.getPrescriptionItems().size());
	}
	
	@Test
	public void shouldGenerateNonArvPrescriptionWithFinalizedStatus() throws Exception {
		this.executeDataSet("prescriptionservice/shouldGenerateNonArvPrescriptionWithFinalizedStatus-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 10);
		calendar.set(Calendar.DAY_OF_MONTH, 16);
		final Date date = calendar.getTime();
		
		final List<DrugOrder> drugOrders = new ArrayList<>();
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(101));
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(103));
		
		final List<Prescription> prescriptions = this.prescriptionGenerator.generatePrescriptions(drugOrders, date);
		
		Assert.assertEquals(1, prescriptions.size());
		final Prescription prescription = prescriptions.get(0);
		Assert.assertEquals(PrescriptionStatus.FINALIZED, prescription.getPrescriptionStatus());
		Assert.assertFalse(prescription.isArv());
		Assert.assertEquals(2, prescription.getPrescriptionItems().size());
	}
	
	@Test
	public void shouldGenerateNonArvPrescriptionWithExpiredByFinalizedStatus() throws Exception {
		this.executeDataSet("prescriptionservice/shouldGenerateNonArvPrescriptionWithFinalizedStatus-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		final Date date = calendar.getTime();
		
		final List<DrugOrder> drugOrders = new ArrayList<>();
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(101));
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(103));
		
		final List<Prescription> prescriptions = this.prescriptionGenerator.generatePrescriptions(drugOrders, date);
		
		Assert.assertEquals(1, prescriptions.size());
		final Prescription prescription = prescriptions.get(0);
		Assert.assertEquals(PrescriptionStatus.EXPIRED, prescription.getPrescriptionStatus());
		Assert.assertFalse(prescription.isArv());
		Assert.assertEquals(2, prescription.getPrescriptionItems().size());
	}
	
	@Test
	public void shouldGenerateArvPrescriptionWithActiveStatus() throws Exception {
		this.executeDataSet("prescriptionservice/shouldGenerateArvPrescriptionWithActiveStatus-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, 7);
		calendar.set(Calendar.DAY_OF_MONTH, 18);
		final Date date = calendar.getTime();
		
		final List<DrugOrder> drugOrders = new ArrayList<>();
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(100));
		drugOrders.add((DrugOrder) Context.getOrderService().getOrder(101));
		
		final List<Prescription> prescriptions = this.prescriptionGenerator.generatePrescriptions(drugOrders, date);
		
		Assert.assertEquals(1, prescriptions.size());
		final Prescription prescription = prescriptions.get(0);
		Assert.assertEquals(PrescriptionStatus.ACTIVE, prescription.getPrescriptionStatus());
		Assert.assertTrue(prescription.isArv());
		Assert.assertEquals(2, prescription.getPrescriptionItems().size());
	}
}
