/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.openmrs.Encounter;

/**
 *
 */
@Entity
@Table(name = "phm_prescription_dispensation")
public class PrescriptionDispensation implements Serializable {

	private static final long serialVersionUID = 101185341009855900L;

	@EmbeddedId
	private PrescriptionDispensationPK id;

	private Encounter encounterPrescription;

	private Encounter encounterDispensation;

	public PrescriptionDispensation(final Encounter encounterPrescription, final Encounter encounterDispensation) {

		this.encounterDispensation = encounterDispensation;
		this.encounterPrescription = encounterPrescription;

		this.id = new PrescriptionDispensationPK(encounterPrescription.getEncounterId(),
				encounterDispensation.getEncounterId());
	}

	public PrescriptionDispensationPK getId() {
		return this.id;
	}

	public void setId(final PrescriptionDispensationPK id) {
		this.id = id;
	}

	public Encounter getEncounterPrescription() {
		return this.encounterPrescription;
	}

	public void setEncounterPrescription(final Encounter encounterPrescription) {
		this.encounterPrescription = encounterPrescription;
	}

	public Encounter getEncounterDispensation() {
		return this.encounterDispensation;
	}

	public void setEncounterDispensation(final Encounter encounterDispensation) {
		this.encounterDispensation = encounterDispensation;
	}
}
