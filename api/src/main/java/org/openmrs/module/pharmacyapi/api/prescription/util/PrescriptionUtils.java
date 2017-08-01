/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.prescription.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order.Action;
import org.openmrs.OrderFrequency;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.exception.PharmacyBusinessException;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription;
import org.openmrs.module.pharmacyapi.api.prescription.entity.Prescription.PrescriptionStatus;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem;
import org.openmrs.module.pharmacyapi.api.prescription.entity.PrescriptionItem.PrescriptionItemStatus;
import org.openmrs.module.pharmacyapi.api.prescription.service.PrescriptionService;
import org.openmrs.module.pharmacyapi.api.service.PrescriptionDispensationService;
import org.openmrs.module.pharmacyapi.api.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.MappedDurationUnits;
import org.openmrs.module.pharmacyapi.api.util.MappedEncounters;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionUtils {
	
	private Concept findArvPlan(Encounter encounter) {
		
		Set<Obs> allObs = encounter.getAllObs();
		
		Concept arvPlan = Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_PLAN);
		
		for (Obs obs : allObs) {
			if (arvPlan.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		return null;
	}
	
	private Concept findArvTherapeuticPlan(Encounter encounter) {
		
		Set<Obs> allObs = encounter.getAllObs();
		
		Concept arvPlan = Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_THERAPEUTIC_LINE);
		
		for (Obs obs : allObs) {
			if (arvPlan.equals(obs.getConcept())) {
				return obs.getValueCoded();
			}
		}
		return null;
	}
	
	public List<PrescriptionItem> preparePrescriptionItems(Prescription prescription, Encounter prescriptionEncounter,
			List<DrugOrder> drugOrders) {

		List<PrescriptionItem> prescriptionItems = new ArrayList<>();

		PrescriptionDispensationService prescriptionDispensationService = Context
				.getService(PrescriptionDispensationService.class);

		for (DrugOrder drugOrder : drugOrders) {

			PrescriptionItem prescriptionItem = null;

			if (Action.NEW.equals(drugOrder.getAction())) {

				prescriptionItem = new PrescriptionItem(drugOrder);
				this.setPrescriptionInstructions(prescriptionItem, drugOrder);
				prescriptionItem.setDrugToPickUp(drugOrder.getQuantity());
				prescriptionItem.setStatus(PrescriptionItemStatus.NEW);
				prescriptionItem.setPrescription(prescription);

			} else {

				if (Action.REVISE.equals(drugOrder.getAction())) {

					prescriptionItem = new PrescriptionItem(drugOrder);
					this.setPrescriptionInstructions(prescriptionItem, drugOrder);

				} else if (Action.DISCONTINUE.equals(drugOrder.getAction())) {

					prescriptionItem = new PrescriptionItem(cloneDrugOrder((DrugOrder) drugOrder.getPreviousOrder()));
					this.setPrescriptionInstructions(prescriptionItem, prescriptionItem.getDrugOrder());

				} else {
					continue;
				}

				Double quantity = calculateDrugPikckedUp(drugOrder);
				prescriptionItem.setDrugPickedUp(quantity);
				prescriptionItem.setDrugToPickUp(
						prescriptionItem.getDrugOrder().getQuantity() - prescriptionItem.getDrugPickedUp());
				prescriptionItem.setPrescription(prescription);

				PrescriptionItemStatus status;

				if (Action.NEW.equals(drugOrder.getAction())) {
					status = PrescriptionItemStatus.NEW;
				} else if (Action.DISCONTINUE.equals(drugOrder.getAction())) {
					status = PrescriptionItemStatus.FINALIZED;
				} else {
					status = PrescriptionItemStatus.ACTIVE;
				}
				prescriptionItem.setStatus(status);
			}

			if (prescriptionItem != null) {
				try {
					if (prescriptionDispensationService.isArvDrug(prescriptionItem, drugOrder)) {
						prescriptionItem.setArvPlan(findArvPlan(prescriptionEncounter));
						prescriptionItem.setTherapeuticLine(findArvTherapeuticPlan(prescriptionEncounter));
						prescriptionItem.setTherapeuticLine(findArvTherapeuticPlan(prescriptionEncounter));
					}
				} catch (PharmacyBusinessException e) {

				}
				prescriptionItems.add(prescriptionItem);
			}
		}
		return prescriptionItems;

	}
	
	public void prepareObservations(Prescription prescription, Encounter encounter) throws PharmacyBusinessException {
		
		final Obs obsPrescriptionDate = new Obs();
		obsPrescriptionDate.setConcept(Context.getConceptService().getConceptByUuid(
		    MappedConcepts.POC_MAPPING_PRESCRIPTION_DATE));
		obsPrescriptionDate.setValueDatetime(prescription.getPrescriptionDate());
		encounter.addObs(obsPrescriptionDate);
		
		if (prescription.getRegime() != null) {
			
			final Obs obsRegime = new Obs();
			obsRegime.setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.PREVIOUS_ANTIRETROVIRAL_DRUGS));
			obsRegime.setValueCoded(getArvRegime(prescription));
			encounter.addObs(obsRegime);
			
			final Obs obsPlan = new Obs();
			obsPlan.setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_PLAN));
			obsPlan.setValueCoded(this.getArvPlan(prescription));
			encounter.addObs(obsPlan);
			
			final Obs obsTherapeuticLine = new Obs();
			obsTherapeuticLine.setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.ARV_THERAPEUTIC_LINE));
			obsTherapeuticLine.setValueCoded(this.getArvTherapeuticLine(prescription));
			encounter.addObs(obsTherapeuticLine);
			
			if (prescription.getInterruptionReason() != null) {
				
				final Obs obsInterruptionReason = new Obs();
				obsInterruptionReason.setConcept(Context.getConceptService().getConceptByUuid(
				    MappedConcepts.REASON_ANTIRETROVIRALS_STOPPED));
				obsInterruptionReason.setValueCoded(Context.getConceptService().getConceptByUuid(
				    prescription.getInterruptionReason().getUuid()));
				encounter.addObs(obsInterruptionReason);
				
			} else if (prescription.getChangeReason() != null) {
				
				final Obs obsChangeReason = new Obs();
				obsChangeReason.setConcept(Context.getConceptService().getConceptByUuid(
				    MappedConcepts.JUSTIFICATION_TO_CHANGE_ARV_TREATMENT));
				obsChangeReason.setValueCoded(Context.getConceptService().getConceptByUuid(
				    prescription.getChangeReason().getUuid()));
				encounter.addObs(obsChangeReason);
			}
			
		} else {
			
			for (PrescriptionItem item : prescription.getPrescriptionItems()) {
				
				Concept conceptDrug = Context.getConceptService().getDrugByUuid(item.getDrugOrder().getDrug().getUuid())
				        .getConcept();
				
				final Obs obsRegime = new Obs();
				obsRegime.setConcept(Context.getConceptService().getConceptByUuid(MappedConcepts.TREATMENT_PRESCRIBED));
				obsRegime.setValueCoded(conceptDrug);
				encounter.addObs(obsRegime);
			}
		}
	}
	
	public void prepareOrders(Prescription prescription, Encounter encounter) {
		
		for (PrescriptionItem item : prescription.getPrescriptionItems()) {
			
			DrugOrder itemDrugOrder = item.getDrugOrder();
			
			Drug drug = Context.getConceptService().getDrugByUuid(itemDrugOrder.getDrug().getUuid());
			Concept doseUnits = Context.getConceptService().getConceptByUuid(itemDrugOrder.getDoseUnits().getUuid());
			OrderFrequency frequency = Context.getOrderService().getOrderFrequencyByUuid(
			    itemDrugOrder.getFrequency().getUuid());
			Concept quantityUnits = Context.getConceptService().getConceptByUuid(itemDrugOrder.getQuantityUnits().getUuid());
			Concept durationUnits = Context.getConceptService().getConceptByUuid(itemDrugOrder.getDurationUnits().getUuid());
			Concept route = Context.getConceptService().getConceptByUuid(itemDrugOrder.getRoute().getUuid());
			Concept concept = drug.getConcept();
			CareSetting careSetting = Context.getOrderService().getCareSettingByUuid(
			    itemDrugOrder.getCareSetting().getUuid());
			
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
			itemDrugOrder.setQuantity(calculateDrugQuantity(itemDrugOrder));
			itemDrugOrder.setNumRefills(0);
			
			encounter.addOrder(itemDrugOrder);
		}
	}
	
	public Encounter prepareEncounter(Prescription prescription) {
		
		EncounterType encounterType = Context.getService(PrescriptionService.class).getEncounterTypeByPatientAge(
		    prescription.getPatient());
		final EncounterRole encounterRole = Context.getEncounterService().getEncounterRoleByUuid(
		    MappedEncounters.DEFAULT_ENCONTER_ROLE);
		final Location location = Context.getLocationService().getLocationByUuid(prescription.getLocation().getUuid());
		
		Encounter encounter = new Encounter();
		encounter.setEncounterType(encounterType);
		encounter.setPatient(prescription.getPatient());
		encounter.addProvider(encounterRole, prescription.getProvider());
		encounter.setLocation(location);
		encounter.setEncounterDatetime(prescription.getPrescriptionDate());
		
		return encounter;
	}
	
	public Concept getArvRegime(Prescription prescription) throws PharmacyBusinessException {
		
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
	
	private Concept getArvPlan(Prescription prescription) throws PharmacyBusinessException {
		
		Concept arvPlan = null;
		
		if (prescription.getArvPlan() != null) {
			
			arvPlan = Context.getConceptService().getConceptByUuid(prescription.getArvPlan().getUuid());
			
			if (arvPlan != null) {
				return arvPlan;
			}
		}
		
		throw new PharmacyBusinessException("Prescription wWith ARV Drug without ARV Plan  cannot be created "
		        + prescription);
	}
	
	private Concept getArvTherapeuticLine(Prescription prescription) throws PharmacyBusinessException {
		
		Concept therapeuticLine = null;
		
		if (prescription.getTherapeuticLine() != null) {
			
			therapeuticLine = Context.getConceptService().getConceptByUuid(prescription.getTherapeuticLine().getUuid());
			
			if (therapeuticLine != null) {
				return therapeuticLine;
			}
		}
		
		throw new PharmacyBusinessException("Prescription with ARV Drug without therapeutic line cannot be created "
		        + prescription);
	}
	
	public Double calculateDrugPikckedUp(final DrugOrder order) throws APIException {

		Double quantity = 0.0;
		final List<Obs> observations = new ArrayList<>();

		DrugOrder tempOrder = order;
		while (tempOrder.getPreviousOrder() != null) {

			if (tempOrder.getOrderReason() == null) {
				observations.addAll(tempOrder.getEncounter().getObs());
			}
			tempOrder = (DrugOrder) tempOrder.getPreviousOrder();
		}

		for (final Obs observation : observations) {

			if (this.isTheSameConceptAndSameDrug(order, observation)) {
				quantity += observation.getValueNumeric();
			}
		}

		return quantity;
	}
	
	private boolean isTheSameConceptAndSameDrug(final DrugOrder order, Obs observation) {
		
		Drug obsDrug = Context.getService(PrescriptionDispensationService.class).findDrugByOrderUuid(
		    observation.getOrder().getUuid());
		
		return MappedConcepts.MEDICATION_QUANTITY.equals(observation.getConcept().getUuid())
		        && order.getDrug().getUuid().equals(obsDrug.getUuid());
	}
	
	private void setPrescriptionInstructions(PrescriptionItem prescriptionItem, final DrugOrder drugOrder) {
		
		final Concept concept = Context.getConceptService().getConceptByUuid(drugOrder.getDosingInstructions());
		
		prescriptionItem.setDosingInstructions(concept.getNames().iterator().next().getName());
	}
	
	public List<PrescriptionItem> filterPrescriptionItemsByStatus(List<PrescriptionItem> prescriptionItems,
			List<PrescriptionItemStatus> itemStatus) {

		List<PrescriptionItem> result = new ArrayList<>();

		for (PrescriptionItem prescriptionItem : prescriptionItems) {

			if (itemStatus.contains(prescriptionItem.getStatus())) {
				result.add(prescriptionItem);
			}
		}
		return result;
	}
	
	public PrescriptionStatus calculatePrescriptioStatus(List<PrescriptionItem> prescriptionItems) {
		
		int countFinalized = 0;
		for (PrescriptionItem prescriptionItem : prescriptionItems) {
			if (PrescriptionItemStatus.FINALIZED.equals(prescriptionItem.getStatus())) {
				countFinalized++;
			}
		}
		
		return prescriptionItems.size() == countFinalized ? PrescriptionStatus.FINALIZED : PrescriptionStatus.ACTIVE;
	}
	
	private DrugOrder cloneDrugOrder(DrugOrder drugOrder) {
		DrugOrder clone = new DrugOrder();
		
		clone.setDose(drugOrder.getDose());
		clone.setQuantity(drugOrder.getQuantity());
		clone.setDosingInstructions(drugOrder.getDosingInstructions());
		clone.setDuration(drugOrder.getDuration());
		clone.setDurationUnits(drugOrder.getDurationUnits());
		clone.setQuantityUnits(drugOrder.getQuantityUnits());
		clone.setRoute(drugOrder.getRoute());
		clone.setDoseUnits(drugOrder.getDoseUnits());
		clone.setFrequency(drugOrder.getFrequency());
		clone.setAction(drugOrder.getAction());
		clone.setUuid(drugOrder.getUuid());
		clone.setEncounter(drugOrder.getEncounter());
		clone.setConcept(drugOrder.getConcept());
		clone.setOrderer(drugOrder.getOrderer());
		clone.setPatient(drugOrder.getPatient());
		clone.setCareSetting(drugOrder.getCareSetting());
		clone.setDrug(drugOrder.getDrug());
		return clone;
	}
	
}
