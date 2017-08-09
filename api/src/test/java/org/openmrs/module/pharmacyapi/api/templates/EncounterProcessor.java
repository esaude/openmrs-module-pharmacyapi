/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.templates;

import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.pharmacyapi.api.common.util.MappedConcepts;
import org.openmrs.module.pharmacyapi.api.util.EntityFactory;

import br.com.six2six.fixturefactory.processor.Processor;

/**
 * @author Stélio Moiane
 */
public class EncounterProcessor implements Processor {
	
	@Override
	public void execute(final Object result) {
		
		if (!(result instanceof Encounter)) {
			return;
		}
		
		final Encounter encounter = (Encounter) result;
		final Set<Obs> allObs = encounter.getAllObs();
		
		final Obs convSetObservation = this.getConvSetObs(allObs);
		
		if (convSetObservation == null) {
			return;
		}
		
		for (final Obs obs : allObs) {
			
			if (MappedConcepts.DOSING_INSTRUCTIONS.equals(obs.getConcept().getUuid())) {
				obs.setValueCoded(EntityFactory.gimme(Concept.class, ConceptTemplate.BEFORE_MEALS));
			}
			
			if (MappedConcepts.DURATION_UNITS.equals(obs.getConcept().getUuid())) {
				obs.setValueCoded(EntityFactory.gimme(Concept.class, ConceptTemplate.WEEKS));
			}
			
			if (MappedConcepts.DOSAGE_AMOUNT.equals(obs.getConcept().getUuid())) {
				obs.setValueNumeric(2.0);
			}
			
			obs.setObsGroup(convSetObservation);
		}
	}
	
	private Obs getConvSetObs(final Set<Obs> allObs) {
		
		for (final Obs obs : allObs) {
			if (MappedConcepts.TREATMENT_PRESCRIBED_SET.equals(obs.getConcept().getUuid())) {
				return obs;
			}
		}
		
		return null;
	}
}
