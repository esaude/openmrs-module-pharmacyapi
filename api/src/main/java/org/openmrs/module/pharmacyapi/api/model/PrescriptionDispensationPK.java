/**
 *
 */
package org.openmrs.module.pharmacyapi.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
public class PrescriptionDispensationPK implements Serializable {

	private static final long serialVersionUID = 63294181773887125L;

	@Column(name = "prescription_id", nullable = false)
	private Integer prescriptionId;

	@Column(name = "dispensation_id", nullable = false)
	private Integer dispensationId;

	public PrescriptionDispensationPK() {

	}

	public PrescriptionDispensationPK(final Integer prescriptionId, final Integer dispensationId) {

		this.prescriptionId = prescriptionId;
		this.dispensationId = dispensationId;
	}

	public Integer getPrescriptionId() {
		return this.prescriptionId;
	}

	public void setPrescriptionId(final Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getDispensationId() {
		return this.dispensationId;
	}

	public void setDispensationId(final Integer dispensationId) {
		this.dispensationId = dispensationId;
	}
}
