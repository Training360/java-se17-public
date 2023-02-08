package demos.java14;

public class SwitchDemo {

    public static void main(String[] args) {
        var fruit = Fruit.APPLE;
//        switch (fruit) {
//            case APPLE:
//            case PEAR:
//                System.out.println("Common fruit");
//                break;
//            case ORANGE:
//            case AVOCADO:
//                System.out.println("Exotic fruit");
//        }

//        switch (fruit) {
//            case APPLE, PEAR -> System.out.println("Common fruit");
//            case ORANGE, AVOCADO -> System.out.println("Exotic fruit");
//        }

        var text = switch (fruit) {
            case APPLE, PEAR -> {
                System.out.println("apple or pear");
                yield "Common fruit";
            }
            case ORANGE -> "Exotic fruit";
            default -> "Unknown";
        };
        System.out.println(text);
    }
}
