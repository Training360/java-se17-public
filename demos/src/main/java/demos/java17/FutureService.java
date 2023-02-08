package demos.java17;

import java.time.Instant;
import java.time.InstantSource;
import java.time.temporal.ChronoUnit;

public class FutureService {

    private InstantSource instantSource;

    public FutureService(InstantSource instantSource) {
        this.instantSource = instantSource;
    }

    public Instant tenSecondsLater() {
        var now = instantSource.instant();
        return now.plus(10, ChronoUnit.SECONDS);
    }
}
