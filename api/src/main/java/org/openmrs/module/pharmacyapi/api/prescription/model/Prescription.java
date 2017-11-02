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
package org.openmrs.module.pharmacyapi.api.prescription.model;

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
	
	public enum PrescriptionStatus {
		NEW, ACTIVE, FINALIZED
	}
	
	private static final long serialVersionUID = 1L;
	
	private Date prescriptionDate;
	
	private Patient patient;
	
	private Provider provider;
	
	private Location location;
	
	private Encounter prescriptionEncounter;
	
	private PrescriptionStatus prescriptionStatus = PrescriptionStatus.NEW;
	
	private Concept regime;
	
	private Concept arvPlan;
	
	private Concept therapeuticLine;
	
	private Concept changeReason;
	
	private Concept interruptionReason;
	
	private List<PrescriptionItem> prescriptionItems = new ArrayList<>();
	
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
	
	public Encounter getPrescriptionEncounter() {
		return prescriptionEncounter;
	}
	
	public void setPrescriptionEncounter(Encounter prescriptionEncounter) {
		this.prescriptionEncounter = prescriptionEncounter;
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
	
	public PrescriptionStatus getPrescriptionStatus() {
		return prescriptionStatus;
	}
	
	public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
		this.prescriptionStatus = prescriptionStatus;
	}
	
	public Concept getRegime() {
		
		for (PrescriptionItem item : prescriptionItems) {
			
			if (item.getRegime() != null) {
				return this.regime = item.getRegime();
			}
		}
		return null;
	}
	
	public Concept getInterruptionReason() {
		
		for (PrescriptionItem item : prescriptionItems) {
			
			if (item.getInterruptionReason() != null) {
				
				return this.interruptionReason = item.getInterruptionReason();
			}
		}
		return null;
	}
	
	public Concept getChangeReason() {
		
		for (PrescriptionItem prescriptionItem : prescriptionItems) {
			
			if (prescriptionItem.getChangeReason() != null) {
				
				return this.changeReason = prescriptionItem.getChangeReason();
			}
		}
		return null;
	}
	
	public Concept getArvPlan() {
		
		for (PrescriptionItem prescriptionItem : prescriptionItems) {
			
			if (prescriptionItem.getArvPlan() != null) {
				
				return this.arvPlan = prescriptionItem.getArvPlan();
			}
		}
		return null;
	}
	
	public Concept getTherapeuticLine() {
		
		for (PrescriptionItem prescriptionItem : prescriptionItems) {
			if (prescriptionItem.getTherapeuticLine() != null) {
				return this.therapeuticLine = prescriptionItem.getTherapeuticLine();
			}
		}
		return null;
	}
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(final Integer prescriptionId) {
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((prescriptionEncounter == null) ? 0 : prescriptionEncounter.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prescription other = (Prescription) obj;
		if (prescriptionEncounter == null) {
			if (other.prescriptionEncounter != null)
				return false;
		} else if (!prescriptionEncounter.equals(other.prescriptionEncounter))
			return false;
		return true;
	}
	
}
