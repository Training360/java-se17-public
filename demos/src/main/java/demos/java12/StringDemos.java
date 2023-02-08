package demos.java12;

import java.util.function.Function;

public class StringDemos {

    public static void main(String[] args) {
        var html = "<html>\n" +
                "\t<body>\n" +
                "\t\t<h1>Hello World</h1>\n" +
                "\t<body>\n" +
                "</html>";
        System.out.println(html);

        var indented = html.indent(4);
        System.out.println(indented);

        var unindented = indented.indent(-2);
        System.out.println(unindented);

        var withLineBreak = "hello\r\nworld";
        indented = withLineBreak.indent(2);
        System.out.println(indented);

        System.out.println(indented.contains("\r\n"));
        System.out.println(indented.contains("\n"));

        var text = "hello";
        var concat = (Function<String, String>) input -> input.concat(" world");
        var transformed = text
                .transform(concat)
                .transform(String::toUpperCase)
                .transform(String::length);
        System.out.println(transformed);
    }
}
