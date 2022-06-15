package es.uvigo.esei.letta;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import es.uvigo.esei.letta.filters.AuthorizationFilter;

@ApplicationPath("/rest/*")
public class LETTATestApplication extends LETTAApplication {
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<>(super.getClasses());
		
		classes.add(AuthorizationFilter.class);
		
		return unmodifiableSet(classes);
	}
}
