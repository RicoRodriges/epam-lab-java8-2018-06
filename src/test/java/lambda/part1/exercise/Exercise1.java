package lambda.part1.exercise;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import lambda.data.Person;
import org.junit.Test;

public class Exercise1 {

    @Test
    public void sortPersonsByAgeUsingArraysSortComparator() {
        Person[] persons = getPersons();

        class PersonComparator implements Comparator<Person> {

            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.getAge(), p2.getAge());
            }
        }

        Arrays.sort(persons, new PersonComparator());

        assertArrayEquals(new Person[]{
            new Person("Иван", "Мельников", 20),
            new Person("Николай", "Зимов", 30),
            new Person("Алексей", "Доренко", 40),
            new Person("Артем", "Зимов", 45)
        }, persons);
    }

    @Test
    public void sortPersonsByAgeUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        Arrays.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.getAge(), p2.getAge());
            }
        });

        assertArrayEquals(new Person[]{
            new Person("Иван", "Мельников", 20),
            new Person("Николай", "Зимов", 30),
            new Person("Алексей", "Доренко", 40),
            new Person("Артем", "Зимов", 45)
        }, persons);
    }

    @Test
    public void sortPersonsByLastNameThenFirstNameUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        Arrays.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                int result = p1.getLastName().compareTo(p2.getLastName());
                if (result == 0) {
                    result = p1.getFirstName().compareTo(p2.getFirstName());
                }
                return result;
            }
        });

        assertArrayEquals(new Person[]{
            new Person("Алексей", "Доренко", 40),
            new Person("Артем", "Зимов", 45),
            new Person("Николай", "Зимов", 30),
            new Person("Иван", "Мельников", 20)
        }, persons);
    }

    @Test
    public void findFirstWithAge30UsingGuavaPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        class MyPredicate implements Predicate<Person> {

            @Override
            public boolean apply(Person person) {
                return person.getAge() == 30;
            }
        }

        Person person = FluentIterable
            .from(persons)
            .firstMatch(new MyPredicate())
            .orNull();

        assertEquals(new Person("Николай", "Зимов", 30), person);
    }

    @Test
    public void findFirstWithAge30UsingGuavaAnonymousPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        Person person = FluentIterable
            .from(persons)
            .firstMatch(new Predicate<Person>() {
                @Override
                public boolean apply(Person person) {
                    return person.getAge() == 30;
                }
            })
            .orNull();

        assertEquals(new Person("Николай", "Зимов", 30), person);
    }

    private Person[] getPersons() {
        return new Person[]{
            new Person("Иван", "Мельников", 20),
            new Person("Алексей", "Доренко", 40),
            new Person("Николай", "Зимов", 30),
            new Person("Артем", "Зимов", 45)
        };
    }
}
