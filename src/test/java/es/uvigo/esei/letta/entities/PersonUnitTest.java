package es.uvigo.esei.letta.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PersonUnitTest {
	@Test
	public void testPersonIntStringString() {
		final int id = 1;
		final String name = "John";
		final String surname = "Doe";
		final String login = "john";
		
		final Person person = new Person(id, name, surname,login);
		
		assertThat(person.getId(), is(equalTo(id)));
		assertThat(person.getName(), is(equalTo(name)));
		assertThat(person.getSurname(), is(equalTo(surname)));
		assertThat(person.getLogin(), is(equalTo(login)));
	}

	@Test(expected = NullPointerException.class)
	public void testPersonIntStringStringNullName() {
		new Person(1, null, "Doe","john");
	}
	
	@Test(expected = NullPointerException.class)
	public void testPersonIntStringStringNullSurname() {
		new Person(1, "John", null, "john");
	}

	@Test(expected = NullPointerException.class)
	public void testPersonIntStringStringNullLogin() {
		new Person(1, "John", "Doe", null);
	}

	@Test
	public void testSetName() {
		final int id = 1;
		final String surname = "Doe";
		final String login = "john";

		final Person person = new Person(id, "John", surname,login);
		person.setName("Juan");
		
		assertThat(person.getId(), is(equalTo(id)));
		assertThat(person.getName(), is(equalTo("Juan")));
		assertThat(person.getSurname(), is(equalTo(surname)));
		assertThat(person.getLogin(), is(equalTo(login)));
	}

	@Test(expected = NullPointerException.class)
	public void testSetNullName() {
		final Person person = new Person(1, "John", "Doe","john");
		
		person.setName(null);
	}

	@Test
	public void testSetSurname() {
		final int id = 1;
		final String name = "John";
		final String login = "john";

		final Person person = new Person(id, name, "Doe",login);
		person.setSurname("Dolores");
		
		assertThat(person.getId(), is(equalTo(id)));
		assertThat(person.getName(), is(equalTo(name)));
		assertThat(person.getSurname(), is(equalTo("Dolores")));
		assertThat(person.getLogin(), is(equalTo(login)));
	}

	@Test(expected = NullPointerException.class)
	public void testSetNullSurname() {
		final Person person = new Person(1, "John", "Doe","john");
		
		person.setSurname(null);
	}

	@Test
	public void testSetLogin() {
		final int id = 1;
		final String name = "John";
		final String surname = "Doe";

		final Person person = new Person(id, name, surname,"john");
		person.setLogin("juan");

		assertThat(person.getId(), is(equalTo(id)));
		assertThat(person.getName(), is(equalTo(name)));
		assertThat(person.getSurname(), is(equalTo(surname)));
		assertThat(person.getLogin(), is(equalTo("juan")));
	}

	@Test(expected = NullPointerException.class)
	public void testSetNullLogin() {
		final Person person = new Person(1, "John", "Doe","john");

		person.setLogin(null);
	}

	@Test
	public void testEqualsObject() {
		final Person personA = new Person(1, "Name A", "Surname A","loginA");
		final Person personB = new Person(1, "Name B", "Surname B", "loginB");
		
		assertTrue(personA.equals(personB));
	}

	@Test
	public void testEqualsHashcode() {
		EqualsVerifier.forClass(Person.class)
			.withIgnoredFields("name", "surname","login")
			.suppress(Warning.STRICT_INHERITANCE)
			.suppress(Warning.NONFINAL_FIELDS)
		.verify();
	}
}
