package demos.java16;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsDemo {

    public static void main(String[] args) {
        var numbers = Stream.iterate(1, i -> i + 1)
                .limit(20).toList();
        System.out.println(numbers);

        var employees = List.of(
                new Employee("John Doe", 1970, List.of("Java")),
                new Employee("Jack Doe", 1980, List.of("Java", "Python")),
                new Employee("Jane Doe", 1990, List.of("Java", "JavaScript"))
        );

        var result = employees.stream()
                .<String>mapMulti((employee, consumer) -> {
                    if (employee.getYearOfBirth() >= 1980) {
                        consumer.accept(employee.getName());
                    }
                }).toList();
        System.out.println(result);

        result = employees.stream()
                .<String>mapMulti((employee, consumer) -> {
                    if (employee.getYearOfBirth() >= 1980) {
                        for (var skill: employee.getSkills()) {
                            if (skill.startsWith("J")) {
                                consumer.accept(skill);
                            }
                        }
                    }
                }).toList();
        System.out.println(result);

        result = employees.stream()
                .filter(employee -> employee.getYearOfBirth() >= 1980)
                .flatMap(employee -> employee.getSkills().stream())
                .filter(skill -> skill.startsWith("J"))
                .toList();
        System.out.println(result);
    }
}
