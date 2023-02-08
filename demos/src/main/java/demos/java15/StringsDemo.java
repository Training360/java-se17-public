package demos.java15;

import java.text.DecimalFormat;
import java.util.Locale;

public class StringsDemo {

    public static void main(String[] args) {
        //var result = String.format("%s is %d years old", "John Doe", 20);
        //System.out.println(result);
        var result = "%s is %d years old".formatted("John Doe", 20);
        System.out.println(result);

        System.out.println("".isEmpty());
        System.out.println(new StringBuilder().isEmpty());

        var locale = new Locale("de", "AT");
        var number = DecimalFormat.getNumberInstance(locale);
        System.out.println(number.format(10_000_000.10));

        var currency = DecimalFormat.getCurrencyInstance(locale);
        System.out.println(currency.format(10_000_000.10));
    }
}
