package org.openmrs.module.pharmacyapi.api.templates;

import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EncounterRoleTemplate implements TemplateLoader {
	
	public static final String DEFAULT_ENCONTER_ROLE = "a0b03050-c99b-11e0-9572-0800200c9a66";
	
	@Override
	public void load() {
		//
		// Fixture.of(EncounterType.class).addTemplate(ARV_FOLLOW_UP_ADULT, new
		// Rule() {
		//
		// {
		// this.add("name", "S.TARV: ADULTO SEGUIMENTO");
		// this.add("description", "seguimento visita do paciente adulto");
		// this.add("dateCreated", this.instant("now"));
		// this.add("retired", false);
		// this.add("uuid", MappedEncounters.ARV_FOLLOW_UP_ADULT);
		// }
		// });
		
	}
}
