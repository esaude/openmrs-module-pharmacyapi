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

	private Encounter prescriptionEncounter;

	private boolean prescriptionStatus;

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

	public boolean isPrescriptionStatus() {
		return prescriptionStatus;
	}

	public void setPrescriptionStatus(boolean prescriptionStatus) {
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
}
