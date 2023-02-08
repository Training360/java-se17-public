package demos.java12;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatDemo {

    public static void main(String[] args) {
        var format = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
        System.out.println(format.format(1000));
        System.out.println(format.format(10_000));
        System.out.println(format.format(1_000_000));

        format = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.LONG);
        System.out.println(format.format(1000));
        System.out.println(format.format(10_000));
        System.out.println(format.format(1_000_000));

        format = NumberFormat.getCompactNumberInstance(new Locale("hu", "HU"), NumberFormat.Style.SHORT);
        System.out.println(format.format(1000));
        System.out.println(format.format(10_000));
        System.out.println(format.format(1_000_000));

        format = NumberFormat.getCompactNumberInstance(new Locale("hu", "HU"), NumberFormat.Style.LONG);
        System.out.println(format.format(1000));
        System.out.println(format.format(10_000));
        System.out.println(format.format(1_000_000));


    }
}
