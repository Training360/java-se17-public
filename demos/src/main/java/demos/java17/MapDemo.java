package demos.java17;

import java.util.HashMap;
import java.util.Map;

public class MapDemo {

    public static void main(String[] args) {
        var employees =
                new HashMap<>(
                Map.of(1, "John Doe", 2, "Jack Doe", 3, "Jane Doe"));

        var result = employees.entrySet()
                .stream()
                .map(Map.Entry::copyOf)
                .sorted(Map.Entry.comparingByValue())
                .findFirst()
                .orElseThrow();

        System.out.println(result);
        result.setValue("Jack Doe 2");
        System.out.println(result);
        System.out.println(employees);
    }
}
