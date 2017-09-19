package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.CareSetting;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class CareSettingTemplate implements TemplateLoader {
	
	public static final String INPATIENT = "c365e560-c3ec-11e3-9c1a-0800200c9a66";
	
	@Override
	public void load() {
		
		Fixture.of(CareSetting.class).addTemplate(INPATIENT, new Rule() {
			
			{
				this.add("uuid", INPATIENT);
			}
		});
	}
}
