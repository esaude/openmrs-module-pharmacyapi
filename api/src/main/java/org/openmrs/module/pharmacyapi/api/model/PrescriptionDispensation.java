/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.openmrs.Encounter;
import org.openmrs.module.pharmacyapi.api.dao.PrescriptionDispensationDAO;

/**
 *
 */
@NamedQueries(value = { @NamedQuery(name = PrescriptionDispensationDAO.QUERY_NAME.findByUuid, query = PrescriptionDispensationDAO.QUERY.findByUuid) })
@Entity
@Table(name = "phm_prescription_dispensation")
public class PrescriptionDispensation extends BaseOpenmrsObjectWrapper {
	
	private static final long serialVersionUID = 5970480895598422427L;
	
	@Id
	@GeneratedValue
	@Column(name = "prescription_dispensation_id")
	private Integer prescriptionDispensationId;
	
	@ManyToOne
	@JoinColumn(name = "prescription_id")
	private Encounter prescription;
	
	@ManyToOne
	@JoinColumn(name = "dispensation_id")
	private Encounter dispensation;
	
	@Override
	public Integer getId() {
		
		return this.prescriptionDispensationId;
	}
	
	@Override
	public void setId(Integer id) {
		this.prescriptionDispensationId = id;
	}
	
	public Encounter getPrescription() {
		return prescription;
	}
	
	public void setPrescription(Encounter prescription) {
		this.prescription = prescription;
	}
	
	public Encounter getDispensation() {
		return dispensation;
	}
	
	public void setDispensation(Encounter dispensation) {
		this.dispensation = dispensation;
	}
}
