package demos.java16;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.Locale;

public class FormatterDemo {

    public static void main(String[] args) {
        var formatter = DateTimeFormatter.ofPattern("B").withLocale(Locale.ENGLISH);
        System.out.println(formatter.format(LocalTime.of(8, 0)));

        formatter = DateTimeFormatter.ofPattern("B").withLocale(new Locale("hu", "HU"));
        System.out.println(formatter.format(LocalTime.of(8, 0)));

        formatter = new DateTimeFormatterBuilder()
                .appendDayPeriodText(TextStyle.FULL)
                .toFormatter()
                .withLocale(Locale.ENGLISH);

        System.out.println(formatter.format(LocalTime.of(16, 0)));

    }
}
