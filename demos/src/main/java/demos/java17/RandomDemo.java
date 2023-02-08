package demos.java17;

import java.util.random.RandomGeneratorFactory;

public class RandomDemo {

    public static void main(String[] args) {
        RandomGeneratorFactory.all()
                .map(generator -> generator.name() + " " + generator.isHardware())
                .forEach(System.out::println);

        var generator = RandomGeneratorFactory.of("Random")
                .create();

        generator.ints()
                .limit(20)
                .forEach(System.out::println);
    }
}
