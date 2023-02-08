package demos.java12;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InputStreamDemo {

    public static void main(String[] args) {
        var bytes = new byte[1000];

        try (var input = new ByteArrayInputStream(bytes)) {
            var skipped = input.skip(2000);
            System.out.println(skipped);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not skip", ioe);
        }

        try (var input = new ByteArrayInputStream(bytes)) {
            input.skipNBytes(2000);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not skip", ioe);
        }
    }
}
