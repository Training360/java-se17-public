package demos.java17;

import org.junit.jupiter.api.Test;

import java.time.InstantSource;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class FutureServiceTest {

    @Test
    void tenSecondsLater() {
        var dateTime = LocalDateTime.parse("2022-12-01T10:00:00");
        var start = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        var instantSource = InstantSource.fixed(start);

        //System.out.println(instantSource.instant());

        var service = new FutureService(instantSource);
        var instant = service.tenSecondsLater();
        var expected = LocalDateTime.parse("2022-12-01T10:00:10").atZone(ZoneId.systemDefault()).toInstant();

        assertEquals(expected, instant);
    }
}