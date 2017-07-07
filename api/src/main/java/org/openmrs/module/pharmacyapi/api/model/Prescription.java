/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;

/**
 * @author Stélio Moiane This class is basically an order wrapper.
 */
public class Prescription extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date prescriptionDate;
	
	private Patient patient;
	
	private Provider provider;
	
	private Location location;
	
	private Encounter encounter;
	
	private Concept arvPlan;
	
	private Concept regime;
	
	private String changeReason;
	
	private String interruptionReason;
	
	private List<PrescriptionItem> prescriptionItems;
	
	public Prescription() {
		this.prescriptionItems = new ArrayList<>();
	}
	
	public Provider getProvider() {
		return this.provider;
	}
	
	public void setProvider(final Provider provider) {
		this.provider = provider;
	}
	
	public Date getPrescriptionDate() {
		return this.prescriptionDate;
	}
	
	public void setPrescriptionDate(final Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}
	
	public List<PrescriptionItem> getPrescriptionItems() {
		return prescriptionItems;
	}
	
	public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
		this.prescriptionItems = prescriptionItems;
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Concept getRegime() {
		return regime;
	}
	
	public void setRegime(Concept regime) {
		this.regime = regime;
	}
	
	public Concept getArvPlan() {
		return arvPlan;
	}
	
	public void setArvPlan(Concept arvPlan) {
		this.arvPlan = arvPlan;
	}
	
	public String getChangeReason() {
		return changeReason;
	}
	
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	
	public String getInterruptionReason() {
		return interruptionReason;
	}
	
	public void setInterruptionReason(String interruptionReason) {
		this.interruptionReason = interruptionReason;
	}
	
	// public String getProviderName() {
	// return providerName;
	// }
	//
	// public void setProviderName(String providerName) {
	// this.providerName = providerName;
	// }
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(final Integer prescriptionId) {
		
	}
}
