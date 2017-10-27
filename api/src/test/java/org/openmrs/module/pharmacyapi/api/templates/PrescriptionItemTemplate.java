package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.DrugOrder;
import org.openmrs.module.pharmacyapi.api.prescription.model.PrescriptionItem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PrescriptionItemTemplate implements TemplateLoader {
	
	public static final String VALID_01 = "VALID_01";
	
	@Override
	public void load() {
		
		Fixture.of(PrescriptionItem.class).addTemplate(VALID_01, new Rule() {
			
			{
				this.add("drugOrder", this.one(DrugOrder.class, DrugOrderTemplate.VALID_PROFLAXIA_01));
				
			}
		});
	}
}
