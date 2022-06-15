package es.uvigo.esei.letta.dataset;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.stream;

import java.util.Arrays;
import java.util.function.Predicate;

import es.uvigo.esei.letta.entities.Person;

public final class PeopleDataset {
	private PeopleDataset() {}
	
	public static Person[] people() {
		return new Person[] {
			new Person(1, "Antón", "Álvarez", "anton"),
			new Person(2, "Ana", "Amargo","ana"),
			new Person(3, "Manuel", "Martínez","manuel"),
			new Person(4, "María", "Márquez", "maria"),
			new Person(5, "Lorenzo", "López","lorenzo"),
			new Person(6, "Laura", "Laredo","laura"),
			new Person(7, "Perico", "Palotes","perico"),
			new Person(8, "Patricia", "Pérez","patricia"),
			new Person(9, "Julia", "Justa","julia"),
			new Person(10, "Juan", "Jiménez","juan")
		};
	}
	
	public static Person[] peopleWithout(int ... ids) {
		Arrays.sort(ids);
		
		final Predicate<Person> hasValidId = person ->
			binarySearch(ids, person.getId()) < 0;
		
		return stream(people())
			.filter(hasValidId)
		.toArray(Person[]::new);
	}
	
	public static Person person(int id) {
		return stream(people())
			.filter(person -> person.getId() == id)
			.findAny()
		.orElseThrow(IllegalArgumentException::new);
	}
	
	public static int existentId() {
		return 5;
	}
	
	public static int nonExistentId() {
		return 1234;
	}

	public static Person existentPerson() {
		return person(existentId());
	}
	
	public static Person nonExistentPerson() {
		return new Person(nonExistentId(), "Jane", "Smith","jane");
	}
	
	public static String newName() {
		return "John";
	}
	
	public static String newSurname() {
		return "Doe";
	}

	public static String newLogin(){ return "john";}
	
	public static Person newPerson() {
		return new Person(people().length + 1, newName(), newSurname(), newLogin());
	}
}
