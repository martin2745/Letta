package es.uvigo.esei.letta.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.letta.entities.PersonUnitTest;

@SuiteClasses({
	PersonUnitTest.class
})
@RunWith(Suite.class)
public class UnitTestSuite {
}
