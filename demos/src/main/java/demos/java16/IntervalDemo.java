package demos.java16;

import java.time.LocalDateTime;

public class IntervalDemo {

    public static void main(String[] args) {
        var start = LocalDateTime.parse("2022-01-01T10:30");
        var end = LocalDateTime.parse("2022-01-01T11:00");
        var interval = new Interval(start, end);

        System.out.println(interval.start());

        System.out.println(interval.length());

        var interval2 = new Interval(start, end);
//        // Ne tegyünk
//        System.out.println(interval == interval2);

        System.out.println(interval.equals(interval2));

        System.out.println(interval instanceof Record);
    }
}
