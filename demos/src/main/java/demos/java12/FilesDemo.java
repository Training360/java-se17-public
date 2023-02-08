package demos.java12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesDemo {

    public static void main(String[] args) {
        var path1 = Path.of("src/main/resources/lorem1.txt");
        var path2 = Path.of("src/main/resources/lorem2.txt");

        try {
            var position = Files.mismatch(path1, path2);
            System.out.println(position);
        } catch (IOException e) {
            throw new IllegalStateException("error comparing files", e);
        }
    }
}
