/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.Patient;

/**
 */
public class DispensationDAOImpl implements DispensationDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DrugOrder> findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(Patient patient) {

		List<DrugOrder> result = new ArrayList<>();

		Query query0 = this.sessionFactory.getCurrentSession().createQuery(
				"select distinct o from DrugOrder o where o.patient = :patient and o.voided is false and o.encounter not in ( select pd.dispensation from PrescriptionDispensation pd ) and o.dateStopped is null");

		List drugsNotDispensed = query0.setParameter("patient", patient).list();

		if (drugsNotDispensed != null && !drugsNotDispensed.isEmpty()) {
			result.addAll(drugsNotDispensed);
		}

		String sql = " select distinct dispensed_order.order_id from orders dispensed_order "
				+ "     join drug_order dispensed_drug on dispensed_drug.order_id = dispensed_order.order_id "
				+ "     join encounter on encounter.encounter_id = dispensed_order.encounter_id "
				+ "     join patient on patient.patient_id = encounter.patient_id "
				+ "   	join person on person.person_id = patient.patient_id "
				+ "     join phm_prescription_dispensation on phm_prescription_dispensation.dispensation_id = encounter.encounter_id "
				+ "   where person.uuid = :patientUuid "
				+ "   	and dispensed_order.order_action in ('REVISE', 'DISCONTINUE') "
				+ "     and dispensed_order.voided is false " + "   	and dispensed_drug.order_id  in ( "
				+ "   		 select max(b.order_id) from drug_order b "
				+ "   		 where b.drug_inventory_id = dispensed_drug.drug_inventory_id "
				+ "             and dispensed_order.previous_order_id is not null ) ";

		if (!result.isEmpty()) {
			sql += " and phm_prescription_dispensation.prescription_id in (:prescriptionItems) ";
		}
		sql += " order by dispensed_order.order_id desc";

		final Query query1 = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("patientUuid", patient.getUuid()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		if (!result.isEmpty()) {
			List<Integer> prescriptionIds = new ArrayList<>();

			for (DrugOrder drugOrder : result) {
				prescriptionIds.add(drugOrder.getEncounter().getEncounterId());
			}

			query1.setParameter("prescriptionItems", StringUtils.join(prescriptionIds, ","));
		}

		List list = query1.list();
		List<Integer> orderIds = new ArrayList<>();
		for (Object object : list) {
			Map row = (Map) object;

			orderIds.add((Integer) row.get("order_id"));
		}
		if (!orderIds.isEmpty()) {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Order.class, "order");
			criteria.add(Restrictions.in("order.orderId", orderIds));

			result.addAll(criteria.list());
		}

		for (DrugOrder order : result) {

			if (!Action.DISCONTINUE.equals(order.getAction())) {
				return result;
			}
		}

		return Collections.emptyList();
	}
	
	@Override
	public Drug findDrugByOrderUuid(String uuid) {
		
		String Sql = "select drug.* from drug join drug_order on drug_order.drug_inventory_id = drug.drug_id join orders on orders.order_id = drug_order.order_id where orders.uuid = :orderuuid";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(Sql).addEntity(Drug.class)
		        .setParameter("orderuuid", uuid);
		
		return (Drug) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Encounter> findEncountersByPatientAndEnconterType(Patient patient, EncounterType encounterType) {
		
		Query query = this.sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            " select distinct enc from Encounter enc inner join fetch enc.orders o where enc.patient = :patient and enc.encounterType = :encounterType and o.voided is false order by enc.encounterId, enc.dateCreated desc");
		
		query.setParameter("patient", patient);
		query.setParameter("encounterType", encounterType);
		
		return query.list();
	}
}
