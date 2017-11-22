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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;

/**
 * @author Stélio Moiane This class is basically an order wrapper.
 */
public class Prescription extends BaseOpenmrsData implements Serializable, Comparable<Prescription> {
	
	public enum PrescriptionStatus {
		ACTIVE, FINALIZED, INTERRUPTED, EXPIRED
	}
	
	private static final long serialVersionUID = 1L;
	
	private Date prescriptionDate;
	
	private Patient patient;
	
	private Provider provider;
	
	private Location location;
	
	private Encounter prescriptionEncounter;
	
	private PrescriptionStatus prescriptionStatus = PrescriptionStatus.ACTIVE;
	
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
		return this.prescriptionItems;
	}
	
	public void setPrescriptionItems(final List<PrescriptionItem> prescriptionItems) {
		this.prescriptionItems = prescriptionItems;
	}
	
	public Encounter getPrescriptionEncounter() {
		return this.prescriptionEncounter;
	}
	
	public void setPrescriptionEncounter(final Encounter prescriptionEncounter) {
		this.prescriptionEncounter = prescriptionEncounter;
	}
	
	public Patient getPatient() {
		return this.patient;
	}
	
	public void setPatient(final Patient patient) {
		this.patient = patient;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}
	
	public PrescriptionStatus getPrescriptionStatus() {
		return this.prescriptionStatus;
	}
	
	public void setPrescriptionStatus(final PrescriptionStatus prescriptionStatus) {
		this.prescriptionStatus = prescriptionStatus;
	}
	
	public Concept getRegime() {
		
		for (final PrescriptionItem item : this.prescriptionItems) {
			
			if (item.getRegime() != null) {
				return this.regime = item.getRegime();
			}
		}
		return null;
	}
	
	public Concept getInterruptionReason() {
		
		for (final PrescriptionItem item : this.prescriptionItems) {
			
			if (item.getInterruptionReason() != null) {
				
				return this.interruptionReason = item.getInterruptionReason();
			}
		}
		return null;
	}
	
	public Concept getChangeReason() {
		
		for (final PrescriptionItem prescriptionItem : this.prescriptionItems) {
			
			if (prescriptionItem.getChangeReason() != null) {
				
				return this.changeReason = prescriptionItem.getChangeReason();
			}
		}
		return null;
	}
	
	public Concept getArvPlan() {
		
		for (final PrescriptionItem prescriptionItem : this.prescriptionItems) {
			
			if (prescriptionItem.getArvPlan() != null) {
				
				return this.arvPlan = prescriptionItem.getArvPlan();
			}
		}
		return null;
	}
	
	public Concept getTherapeuticLine() {
		
		for (final PrescriptionItem prescriptionItem : this.prescriptionItems) {
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
		result = (prime * result) + ((this.prescriptionEncounter == null) ? 0 : this.prescriptionEncounter.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Prescription other = (Prescription) obj;
		if (this.prescriptionEncounter == null) {
			if (other.prescriptionEncounter != null) {
				return false;
			}
		} else if (!this.prescriptionEncounter.equals(other.prescriptionEncounter)) {
			return false;
		}
		return true;
	}
	
	public boolean isArv() {
		
		for (final PrescriptionItem prescriptionItem : this.prescriptionItems) {
			
			if (prescriptionItem.getRegime() != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isActive() {
		return PrescriptionStatus.ACTIVE.equals(this.getPrescriptionStatus());
	}
	
	public boolean isFinalized() {
		
		return PrescriptionStatus.FINALIZED.equals(this.getPrescriptionStatus());
	}
	
	public boolean isInterrupted() {
		
		return PrescriptionStatus.INTERRUPTED.equals(this.getPrescriptionStatus());
	}
	
	@Override
	public int compareTo(final Prescription o) {
		return new CompareToBuilder().append(this.patient, o.getPatient())
		        .append(this.prescriptionEncounter, o.getPrescriptionEncounter())
		        .append(this.prescriptionStatus, o.getPrescriptionStatus()).toComparison();
	}
}
