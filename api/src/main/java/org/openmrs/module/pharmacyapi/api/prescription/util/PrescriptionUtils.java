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
package org.openmrs.module.pharmacyapi.api.prescription.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.OrderFrequency;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.common.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.common.util.MappedDurationUnits;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;
import org.openmrs.module.pharmacyapi.api.drugregime.service.DrugRegimeService;
import org.openmrs.module.pharmacyapi.api.pharmacyheuristic.service.PharmacyHeuristicService;
import org.openmrs.module.pharmacyapi.api.prescription.model.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescriptiondispensation.service.PrescriptionDispensationService;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionUtils {
	
	public void prepareObservations(final Prescription prescription, final Encounter encounter)
	        throws PharmacyBusinessException {
		
		final Obs obsPrescriptionDate = new Obs();
		obsPrescriptionDate
		        .setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE));
		obsPrescriptionDate.setValueDatetime(prescription.getPrescriptionDate());
		encounter.addObs(obsPrescriptionDate);
		
		if (prescription.getRegime() != null) {
			
			final Obs obsRegime = new Obs();
			obsRegime.setConcept(
			        Context.getConceptService().getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS));
			obsRegime.setValueCoded(this.getArvRegimeByPrescriptionRegimeUuid(prescription));
			encounter.addObs(obsRegime);
			
			final Obs obsPlan = new Obs();
			obsPlan.setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_PLAN));
			obsPlan.setValueCoded(this.getArvPlanByPrescriptionArvPlanUuid(prescription));
			encounter.addObs(obsPlan);
			
			final Obs obsTherapeuticLine = new Obs();
			obsTherapeuticLine
			        .setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_THERAPEUTIC_LINE));
			obsTherapeuticLine.setValueCoded(this.getArvTherapeuticLineByPrescriptionTherapeuticLineUuid(prescription));
			encounter.addObs(obsTherapeuticLine);
			
			if (prescription.getInterruptionReason() != null) {
				
				final Obs obsInterruptionReason = new Obs();
				obsInterruptionReason.setConcept(
				        Context.getConceptService().getConceptByUuid(MappedConcepts.REASON_ANTIRETROVIRALS_STOPPED));
				obsInterruptionReason.setValueCoded(
				        Context.getConceptService().getConceptByUuid(prescription.getInterruptionReason().getUuid()));
				encounter.addObs(obsInterruptionReason);
				
			} else if (prescription.getChangeReason() != null) {
				
				final Obs obsChangeReason = new Obs();
				obsChangeReason.setConcept(Context.getConceptService()
				        .getConceptByUuid(MappedConcepts.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT));
				obsChangeReason.setValueCoded(
				        Context.getConceptService().getConceptByUuid(prescription.getChangeReason().getUuid()));
				encounter.addObs(obsChangeReason);
			}
			
			this.addOtherMedications(encounter, prescription);
			
		} else {
			
			for (final PrescriptionItem item : prescription.getPrescriptionItems()) {
				
				final Concept conceptDrug = Context.getConceptService()
				        .getDrugByUuid(item.getDrugOrder().getDrug().getUuid()).getConcept();
				
				final Obs obsTreatmentPrescribe = new Obs();
				obsTreatmentPrescribe
				        .setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.TREATMENT_PRESCRIBED));
				obsTreatmentPrescribe.setValueCoded(conceptDrug);
				encounter.addObs(obsTreatmentPrescribe);
			}
		}
	}
	
	private void addOtherMedications(final Encounter encounter, final Prescription prescription) {
		
		final Concept conceptTreatmentPrescribed = Context.getConceptService()
		        .getConceptByUuid(MappedConcepts.TREATMENT_PRESCRIBED);
		final List<Drug> allArvDrugs = Context.getService(DrugRegimeService.class).findArvDrugs();
		
		for (final PrescriptionItem item : prescription.getPrescriptionItems()) {
			
			if (!allArvDrugs.contains(item.getDrugOrder().getDrug())) {
				
				final Concept conceptDrug = Context.getConceptService()
				        .getDrugByUuid(item.getDrugOrder().getDrug().getUuid()).getConcept();
				final Obs obsOtherMedications = new Obs();
				obsOtherMedications.setConcept(conceptTreatmentPrescribed);
				obsOtherMedications.setValueCoded(conceptDrug);
				encounter.addObs(obsOtherMedications);
			}
		}
	}
	
	public void prepareOrders(final Prescription prescription, final Encounter encounter) {
		
		for (final PrescriptionItem item : prescription.getPrescriptionItems()) {
			
			final DrugOrder itemDrugOrder = item.getDrugOrder();
			
			final Drug drug = Context.getConceptService().getDrugByUuid(itemDrugOrder.getDrug().getUuid());
			final Concept doseUnits = Context.getConceptService()
			        .getConceptByUuid(itemDrugOrder.getDoseUnits().getUuid());
			final OrderFrequency frequency = Context.getOrderService()
			        .getOrderFrequencyByUuid(itemDrugOrder.getFrequency().getUuid());
			final Concept quantityUnits = Context.getConceptService()
			        .getConceptByUuid(itemDrugOrder.getQuantityUnits().getUuid());
			final Concept durationUnits = Context.getConceptService()
			        .getConceptByUuid(itemDrugOrder.getDurationUnits().getUuid());
			final Concept route = Context.getConceptService().getConceptByUuid(itemDrugOrder.getRoute().getUuid());
			final Concept concept = drug.getConcept();
			final CareSetting careSetting = Context.getOrderService()
			        .getCareSettingByUuid(itemDrugOrder.getCareSetting().getUuid());
			
			itemDrugOrder.setDrug(drug);
			itemDrugOrder.setDoseUnits(doseUnits);
			itemDrugOrder.setFrequency(frequency);
			itemDrugOrder.setQuantityUnits(quantityUnits);
			itemDrugOrder.setDurationUnits(durationUnits);
			itemDrugOrder.setRoute(route);
			itemDrugOrder.setConcept(concept);
			itemDrugOrder.setPatient(prescription.getPatient());
			itemDrugOrder.setOrderer(prescription.getProvider());
			itemDrugOrder.setCareSetting(careSetting);
			itemDrugOrder.setEncounter(encounter);
			itemDrugOrder.setQuantity(this.calculateDrugQuantity(itemDrugOrder));
			itemDrugOrder.setNumRefills(0);
			
			encounter.addOrder(itemDrugOrder);
		}
	}
	
	public Encounter preparePrescriptionEncounter(final Prescription prescription, final Date prescriptionDate)
	        throws PharmacyBusinessException {
		
		final Location location = Context.getLocationService().getLocationByUuid(prescription.getLocation().getUuid());
		
		final PharmacyHeuristicService pharmacyHeuristicService = Context.getService(PharmacyHeuristicService.class);
		
		final Encounter encounter = this.generateEncounterDuePrescriptionRules(prescription, location);
		
		encounter.setForm(pharmacyHeuristicService.getFormByPatientAge(prescription.getPatient()));
		if (encounter.getVisit() == null) {
			encounter.setVisit(pharmacyHeuristicService
			        .findLastVisitByPatientAndEncounterDate(prescription.getPatient(), prescriptionDate));
		}
		return encounter;
	}
	
	private Encounter generateEncounterDuePrescriptionRules(final Prescription prescription, final Location location)
	        throws PharmacyBusinessException {
		
		EncounterType encounterType;
		if (this.prescriptionHasARVDrugs(prescription)) {
			
			encounterType = Context.getService(PharmacyHeuristicService.class)
			        .getEncounterTypeByPatientAge(prescription.getPatient());
			try {
				
				final Encounter encounter = Context.getService(PharmacyHeuristicService.class)
				        .findLastEncounterByPatientAndEncounterTypeAndLocationAndDate(prescription.getPatient(),
				            encounterType, location, prescription.getPrescriptionDate());
				Context.getService(PrescriptionDispensationService.class)
				        .findPrescriptionDispensationByDispensation(encounter);
				return encounter;
			}
			catch (final PharmacyBusinessException e) {}
		} else {
			encounterType = Context.getEncounterService().getEncounterTypeByUuid(MappedEncounters.GENERAL_PRESCRIPTION);
		}
		
		final EncounterRole encounterRole = Context.getEncounterService()
		        .getEncounterRoleByUuid(MappedEncounters.DEFAULT_ENCONTER_ROLE);
		
		final Encounter encounter = new Encounter();
		encounter.setEncounterType(encounterType);
		encounter.setPatient(prescription.getPatient());
		encounter.addProvider(encounterRole, prescription.getProvider());
		encounter.setLocation(location);
		encounter.setEncounterDatetime(prescription.getPrescriptionDate());
		
		return encounter;
	}
	
	private boolean prescriptionHasARVDrugs(final Prescription prescription) {
		
		for (final PrescriptionItem prescriptionItem : prescription.getPrescriptionItems()) {
			
			if (prescriptionItem.getRegime() != null) {
				return true;
			}
		}
		return false;
	}
	
	public Concept getArvRegimeByPrescriptionRegimeUuid(final Prescription prescription) throws PharmacyBusinessException {
		
		Concept regime = null;
		
		if (prescription.getRegime() != null) {
			
			regime = Context.getConceptService().getConceptByUuid(prescription.getRegime().getUuid());
			
			if (regime != null) {
				
				return regime;
			}
		}
		
		throw new PharmacyBusinessException(" Prescription of ARV must have an ARV Regime " + prescription);
	}
	
	public Double calculateDrugQuantity(final DrugOrder drugOrder) {
		
		final int durationUnitsDays = MappedDurationUnits.getDurationDays(drugOrder.getDurationUnits().getUuid());
		return drugOrder.getDose() * drugOrder.getDuration() * durationUnitsDays
		        * drugOrder.getFrequency().getFrequencyPerDay();
	}
	
	private Concept getArvPlanByPrescriptionArvPlanUuid(final Prescription prescription) throws PharmacyBusinessException {
		
		Concept arvPlan = null;
		
		if (prescription.getArvPlan() != null) {
			
			arvPlan = Context.getConceptService().getConceptByUuid(prescription.getArvPlan().getUuid());
			
			if (arvPlan != null) {
				return arvPlan;
			}
		}
		
		throw new PharmacyBusinessException(
		        "Prescription wWith ARV Drug without ARV Plan  cannot be created " + prescription);
	}
	
	private Concept getArvTherapeuticLineByPrescriptionTherapeuticLineUuid(final Prescription prescription)
	        throws PharmacyBusinessException {
		
		Concept therapeuticLine = null;
		
		if (prescription.getTherapeuticLine() != null) {
			
			therapeuticLine = Context.getConceptService().getConceptByUuid(prescription.getTherapeuticLine().getUuid());
			
			if (therapeuticLine != null) {
				return therapeuticLine;
			}
		}
		
		throw new PharmacyBusinessException(
		        "Prescription with ARV Drug without therapeutic line cannot be created " + prescription);
	}
	
	public Date calculatePrescriptionExpirationDate(final List<DrugOrder> drugOrders) {
		
		final Calendar maximumCalendarExpirationDate = Calendar.getInstance();
		maximumCalendarExpirationDate.setTime(new Date(Long.MIN_VALUE));
		
		Date maximumExpirationDate = maximumCalendarExpirationDate.getTime();
		
		for (final DrugOrder drugOrder : drugOrders) {
			
			if (drugOrder.getAutoExpireDate().after(maximumExpirationDate)) {
				
				maximumCalendarExpirationDate.setTime(drugOrder.getAutoExpireDate());
				maximumExpirationDate = maximumCalendarExpirationDate.getTime();
			}
		}
		
		return maximumExpirationDate;
	}
}
