package lambda.part3.exercise;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.junit.Test;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Exercise4 {

    private static class LazyCollectionHelper<T, R> {

        private List<T> list;
        private Function<List<T>, List<R>> function;

        private LazyCollectionHelper(List<T> list,
                                     Function<List<T>, List<R>> function) {
            this.list = list;
            this.function = function;
        }

        public static <T> LazyCollectionHelper<T, T> from(List<T> list) {
            return new LazyCollectionHelper<>(list, (o) -> list);
        }

        public <U> LazyCollectionHelper<T, U> flatMap(Function<R, List<U>> flatMapping) {
            return new LazyCollectionHelper<>(list, function.andThen((o) -> {
                ArrayList<U> newList = new ArrayList<>();
                for (R item : o) {
                    newList.addAll(flatMapping.apply(item));
                }
                return newList;
            }));
        }

        public <U> LazyCollectionHelper<T, U> map(Function<R, U> mapping) {
            return new LazyCollectionHelper<>(list, function.andThen((o) -> {
                ArrayList<U> newList = new ArrayList<>();
                for (R item : o) {
                    newList.add(mapping.apply(item));
                }
                return newList;
            }));
        }

        public List<R> force() {
            return new ArrayList<>(function.apply(list));
//            for (T item : list) {
//                newList.addAll(function.apply(list));
//            }
//            return newList;
        }
    }

    @Test
    public void mapEmployeesToCodesOfLetterTheirPositionsUsingLazyFlatMapHelper() {
        List<Employee> employees = getEmployees();

        List<Integer> codes = LazyCollectionHelper.from(employees)
            .flatMap(Employee::getJobHistory)
            .map(JobHistoryEntry::getPosition)
            .flatMap(s -> {
                List<Character> result = new ArrayList<>(s.length());
                for (int i = 0; i < s.length(); i++) {
                    result.add(s.charAt(i));
                }
                return result;
            })
            .map(Integer::valueOf)
            .force();
        assertEquals(
            calcCodes("dev", "dev", "tester", "dev", "dev", "QA", "QA", "dev", "tester", "tester",
                "QA", "QA", "QA", "dev"), codes);
    }

    private static List<Integer> calcCodes(String... strings) {
        List<Integer> codes = new ArrayList<>();
        for (String string : strings) {
            for (char letter : string.toCharArray()) {
                codes.add((int) letter);
            }
        }
        return codes;
    }

    private static List<Employee> getEmployees() {
        return Arrays.asList(
            new Employee(
                new Person("Иван", "Мельников", 30),
                Arrays.asList(
                    new JobHistoryEntry(2, "dev", "EPAM"),
                    new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(
                new Person("Александр", "Дементьев", 28),
                Arrays.asList(
                    new JobHistoryEntry(1, "tester", "EPAM"),
                    new JobHistoryEntry(1, "dev", "EPAM"),
                    new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(
                new Person("Дмитрий", "Осинов", 40),
                Arrays.asList(
                    new JobHistoryEntry(3, "QA", "yandex"),
                    new JobHistoryEntry(1, "QA", "mail.ru"),
                    new JobHistoryEntry(1, "dev", "mail.ru")
                )),
            new Employee(
                new Person("Анна", "Светличная", 21),
                Collections.singletonList(
                    new JobHistoryEntry(1, "tester", "T-Systems")
                )),
            new Employee(
                new Person("Игорь", "Толмачёв", 50),
                Arrays.asList(
                    new JobHistoryEntry(5, "tester", "EPAM"),
                    new JobHistoryEntry(6, "QA", "EPAM")
                )),
            new Employee(
                new Person("Иван", "Александров", 33),
                Arrays.asList(
                    new JobHistoryEntry(2, "QA", "T-Systems"),
                    new JobHistoryEntry(3, "QA", "EPAM"),
                    new JobHistoryEntry(1, "dev", "EPAM")
                ))
        );
    }

}
