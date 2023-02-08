package demos.java15;

public class TextBlocksDemo {

    public static void main(String[] args) {
        var json = """
                {
                  "name": "John Doe",
                  "age": 45,
                  "address": "Doe Street, 23, Java Town"
                }
                """;

        System.out.println(json);
        var indented = json.indent(10);
        System.out.println(indented);
        indented = indented.substring(0, indented.length() - 1);

        var unindented = indented.stripIndent();
        System.out.println(unindented);

        var text = "hello\\tworld";
        System.out.println(text);

        System.out.println(text.translateEscapes());
    }
}
