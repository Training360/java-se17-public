package demos.java17;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SerializationDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var employee = new Employee("John Doe", 1970);

        var filter = ObjectInputFilter.rejectFilter(cl -> cl == Employee.class, ObjectInputFilter.Status.UNDECIDED);
//        ObjectInputFilter.Config.setSerialFilter(filter);
//        ObjectInputFilter.Config.setSerialFilterFactory((f1, f2) -> ObjectInputFilter.merge(f2, f1));

        var output = new ObjectOutputStream(Files.newOutputStream(Path.of("employee.ser")));
        try (output) {
            output.writeObject(employee);
        }

        var input = new ObjectInputStream(Files.newInputStream(Path.of("employee.ser")));
        input.setObjectInputFilter(filter);
        try (input) {
            var result = (Employee) input.readObject();
            System.out.println(result.getName());
        }
    }
}
