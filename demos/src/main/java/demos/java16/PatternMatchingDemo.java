package demos.java16;

public class PatternMatchingDemo {

    public static void main(String[] args) {
        Object o = new Employee("John Doe", 1970);
//        if (o instanceof Employee) {
//            System.out.println(((Employee) o).getName());
//        }

//        if (o instanceof Employee employee) {
//            System.out.println(employee.getName());
//        }

//        if (o instanceof Employee employee && employee.getYearOfBirth() == 1970) {
//            System.out.println(employee.getName());
//        }

        if (!(o instanceof Employee employee)) {
            System.out.println("Not employee");
        }
        else {
            System.out.println(employee.getName());
        }
    }
}
