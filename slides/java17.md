class: inverse, center, middle

# Java 17 újdonságok

---

## Java 17-től

* Kiadás: 2021. szeptember
* LTS
* Oracle No-Fee Terms and Conditions (NFTC) License 2021. szeptember 14-től legalább 2024. szeptemberig
* Nem kell elfogadni a licence-t, ezért automatikusan letölthető
* Production and commercial use
* Redistribution is permitted 

---

## API újdonságok Java 17-től

* Sealed Classes - JEP 409
* Enhanced Pseudo-Random Number Generators, `java.util.random` csomag - JEP 356
* Console Charset API (JDK-8264208) - `Console.charset()`
* `native.encoding` system property
* `HexFormat` osztály
* `Process.errorReader()`, `Process.inputReader()`, `Process.outputWriter()`
    * `getInputStream()`, `getOutputStream()`, `getErrorStream()` már volt
* `Map.Entry.copyOf()`
* `java.time.InstantSource` interfész
* Context-Specific Deserialization Filters - JEP 415

---

## Újdonságok Java 17-től

* Strongly Encapsulate JDK Internals - JEP 403 (JEP 396 folytatása)
	* Relaxed strong encapsulation -> strong encapsulation - szigorúbb szabály
* Restore Always-Strict Floating-Point Semantics - JEP 306
    * Lebegőpontos műveletek egységesítése, _default floating-point semantics_ kivezetésre került
* New macOS Rendering Pipeline - JEP 382
    * macOS-en Apple OpenGL API-ról átállás Apple Metal API-ra
* macOS/AArch64 Port - JEP 391
* JavaDoc összehasonlítja a verziókat, és továbbfejlesztett Deprecated oldal
    * Java 19 tovább változtat rajta

---

## Eltávolításra jelölve

* Deprecate the Applet API for Removal - JEP 398
    * Böngészőben futó Java alkalmazások
* Remove RMI Activation - JEP 407
    * Távoli metódushívásnál a híváskor történő aktiválás (pl. diskre serializált objektum memóriába töltése)
* Remove the Experimental AOT and JIT Compiler - JEP 410
    * Javasolt más technológia, pl. GraalVM
* Deprecate the Security Manager for Removal - JEP 411
    * Kliens oldali Java nem meghatározó

---

## Preview, experimental, incubator

* Pattern Matching for switch (Preview) - JEP 406
* Vector API (Second Incubator) - JEP 414
* Foreign Function & Memory API (Incubator) - JEP 412

---

class: inverse, center, middle

# Sealed types

---

## Sealed types

* Szigorúbb vezérlés, hogy ki származhat le egy osztálytól, vagy ki implementálhatja az interfészt
* API fejlesztőknek

```java
public sealed interface HasName permits Employee, Consumer {
        // ...
}

public non-sealed class Employee implements HasName {
        // ...
}

public final class Consumer implements HasName {
        // ...
}
```

---

## Sealed classes in JDK

```java
abstract sealed class AbstractStringBuilder implements Appendable, CharSequence
    permits StringBuilder, StringBuffer {
        // ...
}

public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, Comparable<StringBuilder>, CharSequence {
        // ...
}
```

---

class: inverse, center, middle

# Random generators

---

## Enhanced Pseudo-Random Number Generators

```java
RandomGeneratorFactory.all()
                .map(fac -> fac.group() + ":" + fac.name()
                        + " {"
                        + (fac.isSplittable() ? " splittable" : "")
                        + (fac.isStreamable() ? " streamable" : "")
                        + (fac.isJumpable() ? " jumpable" : "")
                        + (fac.isArbitrarilyJumpable() ? " arbitrarily-jumpable" : "")
                        + (fac.isLeapable() ? " leapable" : "")
                        + (fac.isHardware() ? " hardware" : "")
                        + (fac.isStatistical() ? " statistical" : "")
                        + (fac.isStochastic() ? " stochastic" : "")
                        + " stateBits: " + fac.stateBits()
                        + " }"
                )
                .sorted().forEach(System.out::println);
```

---

## Futtatás eredménye

```plaintext
LXM:L128X1024MixRandom { splittable streamable statistical stateBits: 1152 }
LXM:L128X128MixRandom { splittable streamable statistical stateBits: 256 }
LXM:L128X256MixRandom { splittable streamable statistical stateBits: 384 }
LXM:L32X64MixRandom { splittable streamable statistical stateBits: 96 }
LXM:L64X1024MixRandom { splittable streamable statistical stateBits: 1088 }
LXM:L64X128MixRandom { splittable streamable statistical stateBits: 192 }
LXM:L64X128StarStarRandom { splittable streamable statistical stateBits: 192 }
LXM:L64X256MixRandom { splittable streamable statistical stateBits: 320 }
Legacy:Random { statistical stateBits: 48 }
Legacy:SecureRandom { stochastic stateBits: 2147483647 }
Legacy:SplittableRandom { splittable streamable statistical stateBits: 64 }
Xoroshiro:Xoroshiro128PlusPlus { streamable jumpable leapable statistical stateBits: 128 }
Xoshiro:Xoshiro256PlusPlus { streamable jumpable leapable statistical stateBits: 256 }
```

---

## Használata

```java
var generator = RandomGeneratorFactory.of("Legacy:Random").create();
```

* Nem thread-safe
* `create()` metódusnak paraméterül átadható seed is
* `ints()`, `longs()`, `doubles()` metódusok Streammel térnek vissza
* `nextInt()`, `nextInt(bound)`, `nextInt(origin, bound)`
* `nextBoolean()`, `nextBytes()`, `nextLong()`, `nextFloat()`, `nextDouble()`

---

## Változott Random osztály

* `implements RandomGenerator`
* `@RandomGeneratorProperties`
* Sok default metódus a `RandomGenerator` interfészben, `RandomSupport`-nak átadja önmagát 

```java
default int nextInt(int origin, int bound) {
    RandomSupport.checkRange(origin, bound);

    return RandomSupport.boundedNextInt(this, origin, bound);
}
```

---

class: inverse, center, middle

# API módosítások

---

## Console

```java
System.out.println(System.console().charset());
System.out.println(System.getProperty("native.encoding"));
```

* Első sor IDEA esetén `NullPointerException`, mert a `System.console()` értéke `null`

Parancssorból:

```plaintext
IBM852
Cp1250 
```

---

## Konzol kódolásának állítása

```plaintext
REM IBM852
chcp 852
REM Windows-1250
chcp 1250
REM UTF-8
chcp 65001
```

---

## `HexFormat` osztály

* Skalár átalakítása

```java
var hexFormat = HexFormat.of().withUpperCase();
System.out.println(hexFormat.toHexDigits((byte) 14)); // 0E
System.out.println(hexFormat.toHexDigits(14)); // 0000000E
```

* byte-ok átalakítása

```java
var hexFormat = HexFormat.of().withUpperCase();
System.out.println(hexFormat.formatHex(new byte[]{-54, -2, -70, -66})); // CAFEBABE
```

* formázva

```java
var hexFormat = HexFormat.of().withDelimiter(" ").withUpperCase().withPrefix("0x");
System.out.println(hexFormat.formatHex(new byte[]{-54, -2, -70, -66})); // 0xCA 0xFE 0xBA 0xBE
```

---

## Parse

```java
var hexFormat = HexFormat.of().withUpperCase();
System.out.println(Arrays.toString(hexFormat.parseHex("CAFEBABE"))); // [-54, -2, -70, -66]
```

---

## `Map.Entry.copyOf()`

```java
var employees = new HashMap<>(Map.of(1, "John Doe", 2, "Jack Doe", 3, "Jane Doe"));
var entry = employees
        .entrySet().stream()
        .map(Map.Entry::copyOf)
        .sorted(Map.Entry.comparingByValue())
        .findFirst()
        .orElseThrow();
System.out.println(entry);
entry.setValue("Jack Smith"); // java.lang.UnsupportedOperationException
System.out.println(employees);
```

* Immutable, konkrét maphez nem köthető példányt hoz létre

---

class: inverse, center, middle

# InstantSource
---

## `InstantSource`

* `Instant` osztály
    * Egy pillanat az idővonalon UTC időzóna szerint
    * EPOCH-tól (1970. január 1-től) eltelt idő, nanoszekundum bontásban
    * `LocalDateTime` több pillanatot is jelölhet, attól függően, hogy melyik időzónában értelmezzük
    * `ZonedDateTime` figyelembe veszi az időzóna eltolásait és a különböző anomáliákat (pl. téli/nyári időszámítás)
* Statikus metódusok helyett kéne egy objektum, melytől le lehet kérni az aktuális időt
* Injektálható, teszteknél használható test double
* Erre született a `Clock`
    * Ez azonban erősen kötődik az időzónához
* Kellett egy olyan, ami nem
    * Ez lett az `InstantSource`

---

## Nem tesztelhető metódus

```java
public Instant tenSecondsLater() {
    var now = Instant.now();
    return now.plus(10, ChronoUnit.SECONDS);
}
```

---

## Tesztelhető osztály

```java
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
```

* `instant()` metódus kér le egy instant objektumot

---

## Teszteset

```java
@Test
void testTenSecondsLater() {
    var dateTime = LocalDateTime.parse("2022-12-01T10:00:00");
    var instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
    var futureService = new FutureService(InstantSource.fixed(instant));
    var result = futureService.tenSecondsLater();
    assertEquals(10, Duration.between(instant, result).get(ChronoUnit.SECONDS));
}
```

* Különböző gyártó metódusok
    * `fixed()` - az idő nem múlik
    * `system()` - rendszeridőnek megfelelő
    * `offset()` - rendszeridőtől megfelelő eltolással
    * `tick()` - bizonyos lépésközzel programozottan továbbléptethető

---

class: inverse, center, middle

# Context-Specific Deserialization Filters

---

## Serialization

* Módszer objektumok bájtfolyammá alakításához
    * Pl. disken tároláshoz, hálózaton átvitelhez, stb.
* Több biztonsági rés nyitható vele
* Egyik megoldás: Deserialization Filters - megszabjuk, mit lehet beolvasni, és mit nem

---

## Serialization

```java
var output = new ObjectOutputStream(Files.newOutputStream(Path.of("employee.ser")));
try (output) {
    output.writeObject(new Employee("John Doe", 1970));
}

var input = new ObjectInputStream(Files.newInputStream(Path.of("employee.ser")));
try (input) {
    var employee = (Employee) input.readObject();
    System.out.println(employee.getName());
}
```

---

## Filter globális beállítása

```java
var filter =
        ObjectInputFilter.rejectFilter(cl -> cl == Employee.class, ObjectInputFilter.Status.UNDECIDED);
ObjectInputFilter.Config.setSerialFilter(filter);
ObjectInputFilter.Config.setSerialFilterFactory((f1, f2) -> ObjectInputFilter.merge(f2,f1));
```

```plaintext
Exception in thread "main" java.io.InvalidClassException: filter status: REJECTED
```

---

## Filter lokális beállítása

```java
var filter =
        ObjectInputFilter.rejectFilter(cl -> cl == Employee.class, ObjectInputFilter.Status.UNDECIDED);
var input = new ObjectInputStream(Files.newInputStream(Path.of("employee.ser")));
input.setObjectInputFilter(filter);
try (input) {
    var employee = (Employee) input.readObject();
    System.out.println(employee.getName());
}      
```

