package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;

/**
 * 
 *
 */
public class PrescriptionItem extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private DrugOrder drugOrder;
	
	private Double drugToPickUp;
	
	private Double drugPickedUp;
	
	private String dosingInstructions;
	
	private Prescription prescription;
	
	private Concept arvPlan;
	
	private Concept regime;
	
	private Concept changeReason;
	
	private Concept interruptionReason;
	
	public PrescriptionItem() {
		this.drugPickedUp = 0.0;
		this.drugToPickUp = 0.0;
	}
	
	public PrescriptionItem(DrugOrder drugOrder) {
		this.drugPickedUp = 0.0;
		this.drugToPickUp = 0.0;
		this.drugOrder = drugOrder;
	}
	
	public DrugOrder getDrugOrder() {
		return drugOrder;
	}
	
	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}
	
	public Double getDrugToPickUp() {
		return drugToPickUp;
	}
	
	public void setDrugToPickUp(Double drugToPickUp) {
		this.drugToPickUp = drugToPickUp;
	}
	
	public Double getDrugPickedUp() {
		return drugPickedUp;
	}
	
	public void setDrugPickedUp(Double drugPickedUp) {
		this.drugPickedUp = drugPickedUp;
	}
	
	public Concept getRegime() {
		return regime;
	}
	
	public void setRegime(Concept regime) {
		this.regime = regime;
	}
	
	public String getDosingInstructions() {
		return dosingInstructions;
	}
	
	public void setDosingInstructions(String dosingInstructions) {
		this.dosingInstructions = dosingInstructions;
	}
	
	public Prescription getPrescription() {
		return prescription;
	}
	
	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	
	public Concept getArvPlan() {
		return arvPlan;
	}
	
	public void setArvPlan(Concept arvPlan) {
		this.arvPlan = arvPlan;
	}
	
	public Concept getChangeReason() {
		return changeReason;
	}
	
	public void setChangeReason(Concept changeReason) {
		this.changeReason = changeReason;
	}
	
	public Concept getInterruptionReason() {
		return interruptionReason;
	}
	
	public void setInterruptionReason(Concept interruptionReason) {
		this.interruptionReason = interruptionReason;
	}
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(Integer arg0) {
		
	}
}
