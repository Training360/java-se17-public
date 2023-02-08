package demos.preview;

public class SwitchDemo {

    public static void main(String[] args) {
        Object o = "Hello world!";
        var text = switch (o) {
            case String s -> s.toUpperCase();
            case Long l -> "Long: " + l;
            default -> o.toString();
        };
        System.out.println(text);
    }
}
