/**
 *
 */
package org.openmrs.module.pharmacyapi.api.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.Patient;

/**
 */
public class DispensationDAOImpl implements DispensationDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DrugOrder> findLastDrugOrdersByLastPatientEncounter(final Patient patient) {
		
		final String hql = "select distinct drugOrder from DrugOrder drugOrder " + " join fetch drugOrder.drug drug "
		        + " join fetch drugOrder.encounter encounter " + " join fetch encounter.patient patient "
		        + " where patient = :patient " + " and drugOrder.dispenseAsWritten is false "
		        + " and drugOrder.action not in('DISCONTINUE') "
		        + " and drugOrder.dateStopped is null order by drugOrder.action desc, drugOrder.dateCreated desc";
		
		final Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("patient", patient);
		
		return query.list();
	}
	
	@Override
	public void updateDrugOrder(final DrugOrder drugOrder) {
		
		this.sessionFactory.getCurrentSession().update(drugOrder);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DrugOrder> findDrugOrdersByPatientAndNotDispensedAndPartialDispensed(Patient patient) {

		final String sql = "select distinct * from " + "           (select distinct orders.order_id from orders "
				+ "					join drug_order on drug_order.order_id = orders.order_id "
				+ "					join encounter on encounter.encounter_id = orders.encounter_id "
				+ "					join patient on patient.patient_id = encounter.patient_id "
				+ "					join person on person.person_id = patient.patient_id "
				+ "	   			where person.uuid = :patientUuid "
				+ "            		and orders.encounter_id not in ( "
				+ "   				    select phm_prescription_dispensation.dispensation_id from phm_prescription_dispensation "
				+ "   			  	 ) "
				+ "		       	 	and orders.date_stopped is null order by orders.order_id desc "
				+ " 		  ) ORDER_WITHOUT_PRESCRIPTION " + "      union " + "         select distinct * from "
				+ "			 (select distinct dispensed_order.order_id from orders dispensed_order "
				+ "  				join drug_order dispensed_drug on dispensed_drug.order_id = dispensed_order.order_id "
				+ "   				join encounter on encounter.encounter_id = dispensed_order.encounter_id "
				+ "   				join patient on patient.patient_id = encounter.patient_id "
				+ "   				join person on person.person_id = patient.patient_id "
				+ "    				join phm_prescription_dispensation on phm_prescription_dispensation.dispensation_id = encounter.encounter_id "
				+ "   			where person.uuid = :patientUuid "
				+ "   				and dispensed_order.order_action in ('REVISE') "
				+ "   	     		and dispensed_drug.order_id  in ( "
				+ "   					select max(b.order_id) from drug_order b "
				+ "   					where b.drug_inventory_id = dispensed_drug.drug_inventory_id and dispensed_order.previous_order_id is not null and b.dispense_as_written is false "
				+ "    	     	     ) " + "   	     	    order by dispensed_order.order_id desc "
				+ "          ) ORDER_WITH_PARTIAL_DISPENSED_PRESCRIPTION ";

		final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("patientUuid", patient.getUuid()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List list = query.list();

		List<Integer> orderIds = new ArrayList<>();
		for (Object object : list) {
			Map row = (Map) object;

			orderIds.add((Integer) row.get("order_id"));
		}

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Order.class, "order");
		criteria.add(Restrictions.in("order.orderId", orderIds));

		return criteria.list();
	}
	
	@Override
	public Drug findDrugByOrderUuid(String uuid) {
		
		String Sql = "select drug.* from drug join drug_order on drug_order.drug_inventory_id = drug.drug_id join orders on orders.order_id = drug_order.order_id where orders.uuid = :orderuuid";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(Sql).addEntity(Drug.class)
		        .setParameter("orderuuid", uuid);
		
		return (Drug) query.uniqueResult();
	}
}
