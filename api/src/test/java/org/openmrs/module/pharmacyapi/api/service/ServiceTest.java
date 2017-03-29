/**
 *
 */
package org.openmrs.module.pharmacyapi.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacyapi.api.model.SupplierService;
import org.openmrs.module.pharmacyapi.api.service.stock.DrugMeasureUnitService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * @author Guimino Neves
 */
public class ServiceTest extends BaseModuleContextSensitiveTest {
	
	private PatientService patientService;
	
	private PrescriptionService prescriptionService;
	
	private DispensationService dispensationService;
	
	private DrugItemService drugItemService;
	
	private DrugMeasureUnitService drugMeasureService;
	
	private SupplierService supplierService;
	
	@Before
	public void before() throws Exception {
		
		this.initializeInMemoryDatabase();
		this.authenticate();
	}
	
	@Test
	public void shoulFindServices() throws Exception {
		
		this.prescriptionService = Context.getService(PrescriptionService.class);
		this.dispensationService = Context.getService(DispensationService.class);
		this.patientService = Context.getService(PatientService.class);
		this.drugItemService = Context.getService(DrugItemService.class);
		this.drugMeasureService = Context.getService(DrugMeasureUnitService.class);
		this.supplierService = Context.getService(SupplierService.class);
		
		Assert.assertNotNull(this.prescriptionService);
		Assert.assertNotNull(this.dispensationService);
		Assert.assertNotNull(this.patientService);
		Assert.assertNotNull(this.drugItemService);
		Assert.assertNotNull(this.drugMeasureService);
		Assert.assertNotNull(this.supplierService);
		
	}
}
