package es.uvigo.esei.letta.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.letta.rest.PeopleResourceTest;
import es.uvigo.esei.letta.rest.UsersResourceTest;

@SuiteClasses({ 
	PeopleResourceTest.class,
	UsersResourceTest.class
})
@RunWith(Suite.class)
public class IntegrationTestSuite {
}
