package demos.java12;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamsDemo {

    public static void main(String[] args) {
        var employees = List.of(
                new Employee("John Doe", 1970),
                new Employee("Jack Doe", 1980),
                new Employee("Jane Doe", 1990)
        );

        var boundaries = employees.stream().collect(
                Collectors.teeing(
                        Collectors.minBy(Comparator.comparing(Employee::getYearOfBirth)),
                        Collectors.maxBy(Comparator.comparing(Employee::getYearOfBirth)),
                        (result1, result2) -> new Boundaries(result1.orElseThrow(), result2.orElseThrow())
                )
        );

        System.out.println(boundaries.getMin().getName());
        System.out.println(boundaries.getMax().getName());
    }
}
