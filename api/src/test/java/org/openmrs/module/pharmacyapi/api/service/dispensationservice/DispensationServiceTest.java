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
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.dispensation.model.Dispensation;
import org.openmrs.module.pharmacyapi.api.dispensation.model.DispensationItem;
import org.openmrs.module.pharmacyapi.api.dispensation.service.DispensationService;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.model.PrescriptionDispensation;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;

/**
 * @author Stélio Moiane
 */
public class DispensationServiceTest extends BaseTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldDispenseOrdersForNonArvPrescription() throws Exception {
		this.executeDataSet("dispensationservice/shouldDispenseOrdersForNonArvPrescription-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2005);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		final Dispensation dispensation = new Dispensation();
		dispensation.setPatientUuid("5946f880-b197-400b-9caa-a3c661d23041");
		dispensation.setLocationUuid("8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
		dispensation.setProviderUuid("ba1b19c2-3ed6-4f63-b8c0-f762dc8d7562");
		dispensation.setDispensationDate(calendar.getTime());
		final DispensationItem dispensationItem = new DispensationItem();
		dispensationItem.setQuantityDispensed(0d);
		dispensationItem.setQuantityToDispense(3d);
		
		final String orderUuid = "921de0a3-05c4-444a-be03-0001";
		final String encounterPrescriptionUuid = "eec646cb-c847-4ss-enc-who-adult";
		
		dispensationItem.setOrderUuid(orderUuid);
		dispensationItem.setPrescriptionUuid(encounterPrescriptionUuid);
		dispensation.setDispensationItems(Arrays.asList(dispensationItem));
		
		final Dispensation createdDispensation = Context.getService(DispensationService.class).dispense(dispensation);
		
		Assert.assertNotNull(createdDispensation);
		
		final List<PrescriptionDispensation> prescriptionDispensations = Context
		        .getService(PrescriptionDispensationService.class)
		        .findPrescriptionDispensationByPrescription(new Encounter(1000));
		
		Assert.assertTrue(!prescriptionDispensations.isEmpty());
		Assert.assertEquals(1, prescriptionDispensations.size());
		
		final PrescriptionDispensation prescriptionDispensation = prescriptionDispensations.iterator().next();
		final Encounter dispensationEncounter = prescriptionDispensation.getDispensation();
		
		Assert.assertEquals(MappedEncounters.DISPENSATION_ENCOUNTER_TYPE,
		    dispensationEncounter.getEncounterType().getUuid());
		
		final List<Obs> observations = Context.getObsService().getObservations(null,
		    Arrays.asList(dispensationEncounter), null, null, null, null, null, null, null, null, null, false);
		
		MatcherAssert.assertThat(observations, IsCollectionWithSize.hasSize(3));
		
		final Collection<Concept> concepts = CollectionUtils.collect(observations,
		    TransformerUtils.invokerTransformer("getConcept"));
		final Collection<String> uuids = CollectionUtils.collect(concepts,
		    TransformerUtils.invokerTransformer("getUuid"));
		
		MatcherAssert.assertThat(uuids, Matchers.hasItems(MappedConcepts.DISPENSATION_SET,
		    MappedConcepts.MEDICATION_QUANTITY, MappedConcepts.DATE_OF_NEXT_PICK_UP));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldDispenseOrdersForArvPrescription() throws Exception {
		this.executeDataSet("dispensationservice/shouldDispenseOrdersForArvPrescription-dataset.xml");
		
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, 10);
		calendar.set(Calendar.DAY_OF_MONTH, 8);
		
		final Dispensation dispensation = new Dispensation();
		dispensation.setPatientUuid("5946f880-b197-400b-9caa-a3c661d23041");
		dispensation.setLocationUuid("8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
		dispensation.setProviderUuid("ba1b19c2-3ed6-4f63-b8c0-f762dc8d7562");
		dispensation.setDispensationDate(calendar.getTime());
		final DispensationItem dispensationItem = new DispensationItem();
		dispensationItem.setQuantityDispensed(0d);
		dispensationItem.setQuantityToDispense(3d);
		
		final String orderUuid = "921de0a3-05c4-444a-be03-0001";
		final String encounterPrescriptionUuid = "eec646cb-c847-4ss-enc-who-adult";
		
		dispensationItem.setOrderUuid(orderUuid);
		dispensationItem.setRegimeUuid("9dc17c1b-7b6d-488e-a38d-505a7b6xxx1");
		dispensationItem.setPrescriptionUuid(encounterPrescriptionUuid);
		dispensation.setDispensationItems(Arrays.asList(dispensationItem));
		
		final Dispensation createdDispensation = Context.getService(DispensationService.class).dispense(dispensation);
		
		Assert.assertNotNull(createdDispensation);
		
		final List<PrescriptionDispensation> prescriptionDispensations = Context
		        .getService(PrescriptionDispensationService.class)
		        .findPrescriptionDispensationByPrescription(new Encounter(1000));
		
		Assert.assertTrue(!prescriptionDispensations.isEmpty());
		Assert.assertEquals(1, prescriptionDispensations.size());
		
		final PrescriptionDispensation prescriptionDispensation = prescriptionDispensations.iterator().next();
		final Encounter dispensationEncounter = prescriptionDispensation.getDispensation();
		
		Assert.assertEquals(MappedEncounters.DISPENSATION_ENCOUNTER_TYPE,
		    dispensationEncounter.getEncounterType().getUuid());
		
		final List<Obs> observations = Context.getObsService().getObservations(null,
		    Arrays.asList(dispensationEncounter), null, null, null, null, null, null, null, null, null, false);
		
		MatcherAssert.assertThat(observations, IsCollectionWithSize.hasSize(4));
		
		final Collection<Concept> concepts = CollectionUtils.collect(observations,
		    TransformerUtils.invokerTransformer("getConcept"));
		final Collection<String> uuids = CollectionUtils.collect(concepts,
		    TransformerUtils.invokerTransformer("getUuid"));
		
		MatcherAssert.assertThat(uuids,
		    Matchers.hasItems(MappedConcepts.DISPENSATION_SET, MappedConcepts.MEDICATION_QUANTITY,
		        MappedConcepts.DATE_OF_NEXT_PICK_UP, MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS));
		
	}
}
