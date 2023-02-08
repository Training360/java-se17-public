package demos.java14;

import java.util.Map;

public class NullPointerDemo {

    public static void main(String[] args) {
//        Map<Long, Employee> employees = null;
//        System.out.println(employees.get(1L).getName().toUpperCase());

//        Map<Long, Employee> employees = Map.of();
//        System.out.println(employees.get(1L).getName().toUpperCase());

        Map<Long, Employee> employees = Map.of(1L, new Employee(null, 1970));
        System.out.println(employees.get(1L).getName().toUpperCase());
    }
}
