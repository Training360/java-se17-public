package demos.java16;

import java.time.Duration;
import java.time.LocalDateTime;

public record Interval(LocalDateTime start, LocalDateTime end) implements HasLength {

    @Override
    public Duration length() {
        return Duration.between(start, end);
    }

//    public Interval(LocalDateTime start, LocalDateTime end) {
//        this.start = start;
//        this.end = end;
//    }

//    public Interval {
//        if (start == null) {
//            throw new IllegalArgumentException("Can not be null");
//        }
//    }

    public Interval(LocalDateTime start) {
        this(start, LocalDateTime.now());
    }
}
