package demos.java17;

import java.util.Scanner;

public class ConsoleDemo {

    public static void main(String[] args) {
        System.out.println(System.console().charset());
        System.out.println(System.getProperty("native.encoding"));

        var scanner = new Scanner(System.in);
        var name = scanner.nextLine();
        System.out.println(name);
    }
}
