/**
 * 
 */
package org.openmrs.module.pharmacyapi.api.templates;

import org.openmrs.Location;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class LocationTemplate implements TemplateLoader {
	
	public static final String XANADU = "9356400c-a5a2-4532-8f2b-2361b3446eb8";
	
	@Override
	public void load() {
		
		Fixture.of(Location.class).addTemplate(XANADU, new Rule() {
			
			{
				this.add("dateCreated", this.instant("now"));
				this.add("retired", false);
				this.add("uuid", XANADU);
			}
		});
	}
}
