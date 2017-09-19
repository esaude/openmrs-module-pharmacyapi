/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.EncounterType;
import org.openmrs.module.pharmacyapi.api.common.util.MappedEncounters;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class EncounterTypeTemplate implements TemplateLoader {
	
	public static final String ARV_FOLLOW_UP_ADULT = MappedEncounters.ARV_FOLLOW_UP_ADULT;
	
	public static final String ARV_FOLLOW_UP_CHILD = MappedEncounters.ARV_FOLLOW_UP_CHILD;
	
	public static final String DISPENSATION_ENCOUNTER_TYPE = MappedEncounters.DISPENSATION_ENCOUNTER_TYPE;
	
	public static final String FILA_ENCOUNTER_TYPE = MappedEncounters.FILA_ENCOUNTER_TYPE;
	
	@Override
	public void load() {
		
		Fixture.of(EncounterType.class).addTemplate(ARV_FOLLOW_UP_ADULT, new Rule() {
			
			{
				this.add("name", "S.TARV: ADULTO SEGUIMENTO");
				this.add("description", "seguimento visita do paciente adulto");
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", MappedEncounters.ARV_FOLLOW_UP_ADULT);
			}
		});
		
		Fixture.of(EncounterType.class).addTemplate(ARV_FOLLOW_UP_CHILD, new Rule() {
			
			{
				this.add("name", "S.TARV: PEDIATRIA SEGUIMENTO");
				this.add("description", "Seguimento visita do paciente pediatria");
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", MappedEncounters.ARV_FOLLOW_UP_CHILD);
			}
		});
		
		Fixture.of(EncounterType.class).addTemplate(DISPENSATION_ENCOUNTER_TYPE, new Rule() {
			
			{
				this.add("name", "POC:DISPENSATION");
				this.add("description", "For drugs dispensation on the pharmacy module - poc");
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", MappedEncounters.DISPENSATION_ENCOUNTER_TYPE);
			}
		});
		
		Fixture.of(EncounterType.class).addTemplate(FILA_ENCOUNTER_TYPE, new Rule() {
			
			{
				this.add("name", "S.TARV: FARMACIA");
				this.add("description", "Farmacia");
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", MappedEncounters.FILA_ENCOUNTER_TYPE);
			}
		});
	}
}
