class: inverse, center, middle

# Java 16 újdonságok

---

## Java 16 megjelenése

* Kiadás: 2021. március

---

## Java 16 API módosítások

* Pattern Matching for instanceof - JEP 394
* Records - JEP 395
* `Stream.toList()` metódus
* `Stream.mapMulti()` és primitív típusos társai
* Day period support
* `Objects.checkFromIndexSize()`, `Objects.checkFromToIndex()`, `Objects.checkIndex()`
* `IndexOutOfBoundsException(long index)` konstruktor
* Unix-Domain Socket Channels - JEP 380
* Új `java.net.http.HttpRequest` metódusok
* Új NIO `Buffer` `put` metódusok
* `java.util.logging.LogRecord` thread id-jának beállítása/lekérése

---

## További Java 16 módosítások

* Enable C++14 Language Features - JEP 347
* Warnings for Value-Based Classes - JEP 390
* Elastic Metaspace - JEP 387
* Packaging Tool - JEP 392
* Strongly Encapsulate JDK Internals by Default - JEP 396
* ZGC: Concurrent Thread-Stack Processing - JEP 376
    * További művelet átmozgatása a stop-the-world fázisból a concurrent fázisba
* Alpine Linux Port - JEP 386
* Windows/AArch64 támogatás - JEP 388
* Migrate from Mercurial to Git - JEP 357
* Migrate to GitHub - JEP 369

---

## Preview, experimental, incubator

* Vector API (Incubator) - JEP 338
  * Project Panama keretein belül
  * CPU: képes ugyanazt az operátort párhuzamosan több adaton végrehajtani egy órajel alatt (Single instruction, multiple data  - SIMD)
* Sealed Classes (Second Preview) - Java 17-ben végleges - JEP 397
* Foreign-Memory Access API (Third Incubator) - JEP 393
* Foreign Linker API (Incubator) - JEP 389
  * Project Panama keretein belül

---

class: inverse, center, middle

# Pattern Matching for instanceof 

---

## Pattern Matching for instanceof 

```java
Object o = new Employee("John Doe", 1970);
if (o instanceof Employee) {
    Employee employee = (Employee) o;
    System.out.println(employee.getName());
}
```

```java
Object o = new Employee("John Doe", 1970);
if (o instanceof Employee employee) {
    System.out.println(employee.getName());
}
```

---

## Összetett feltétel

* Változó scope-ja:

```java
Object o = new Employee("John Doe", 1970);
if (o instanceof Employee employee && employee.getYearOfBirth() < 100) {
    System.out.println(employee.getName());
}
```

* A `||` operátor esetén nem fog lefordulni

---

## Változó else ágban

```java
Object o = new Employee("John Doe", 1970);
if (!(o instanceof Employee employee)) {
    System.out.println("Not employee");
} else {
    System.out.println(employee.getName());
}
```

---

class: inverse, center, middle

# Records

---

## Records

* Immutable osztályokhoz
* `final` attribútumok
* konstruktor, getterek, `equals`, `hashCode` és `toString` metódusok
  * Fejlesztőeszközzel generálva
  * Lombok (`@Value` annotáció)


```java
public record Interval(LocalDateTime start, LocalDateTime end) {
}
```

```java
var start = LocalDateTime.parse("2022-01-01T10:30");
var end = LocalDateTime.parse("2022-01-01T11:00");
var interval = new Interval(start, end);
System.out.println(interval.start());
```

---

## Tulajdonságai

* Őse: `java.lang.Record`
* IDEA támogatás: Class can be a record
* Bővíthető új metódussal

```java
public record Interval(LocalDateTime start, LocalDateTime end) {

  public Duration length() {
      return Duration.between(start, end);
  }
}
```

* Implementálhat interfészt

---

## Konstruktorok


```java
public Interval(LocalDateTime start, LocalDateTime end) {
    if (start == null) {
        throw new IllegalArgumentException("No start");
    }
    this.start = start;
    this.end = end;
}
```

* Compact form

```java
public Interval {
    if (start == null) {
        throw new IllegalArgumentException("No start");
    }
}
```

---

## Konstruktor overload

```java
public Interval(LocalDateTime start) {
    this(start, null);
}
```

---

class: inverse, center, middle

# API módosítások

---

## `Stream.toList()`


```java
var numbers = Stream.iterate(0, i -> i + 1)
    .limit(20)
    .collect(Collectors.toList()); // Mutable
```

```java
var numbers = Stream.iterate(0, i -> i + 1)
                .limit(20)
                .collect(Collectors.toUnmodifiableList()); // Immutable
```

```java
var numbers = Stream.iterate(0, i -> i + 1)
    .limit(20)
    .toList();
```

* Immutable, módosító metódusok `UnsupportedOperationException` kivételt dobnak

---

## `Stream.mapMulti()`

* Hasonló a `flatMap()` metódushoz

```java
<R> Stream<R> mapMulti(BiConsumer<T, Consumer<R>> mapper)
```

```java
employees.stream()
    .mapMulti((employee, consumer) -> {
        if (employee.getYearOfBirth() >= 1980) {
            consumer.accept(employee.getName());
        }
    })
.forEach(System.out::println);
```

* Egy-nulla, egy-egy transzformációnál

---

## Komplexebb példa

```java
employees
    .stream().<String>mapMulti((employee, consumer) -> {
        if (employee.getYearOfBirth() >= 1980) {
            for (var skill: employee.getSkills()) {
                if (skill.startsWith("J")) {
                    consumer.accept(skill);
                }
            }
        }
    })
    .forEach(System.out::println);
```

* Ha nulla v. kevés elemmé akarjuk konvertálni, a `flatMap()`-nél hatékonyabb, mert ott mindig streammé kell alakítani
* Ha egyszerűbb előállítani az elemeket magukban, mint elemek streamjét előállítani
* (Valós alkalmazásban method reference-re cserélendő.)

---

## Day Period Support 

```java
var formatter = DateTimeFormatter.ofPattern("B");
System.out.println(formatter.format(LocalTime.of(8, 0)));
System.out.println(formatter.format(LocalTime.of(13, 0)));
System.out.println(formatter.format(LocalTime.of(20, 0)));
System.out.println(formatter.format(LocalTime.of(23, 0)));
System.out.println(formatter.format(LocalTime.of(0, 0)));
```

```plaintext
in the morning
in the afternoon
in the evening
at night
midnight
```

---

## Locale támogatás

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("B").withLocale(new Locale("hu", "HU"));
```

```plaintext
reggel
délután
este
éjjel
éjfél
```

---

## DateTimeFormatterBuilder

* Java 8 óta
* Pattern helyett kódból (builder tervezési minta) állíthatjuk elő a formattert

```java
DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .optionalStart()
        .appendPattern(".SSS")
        .optionalEnd()
        .optionalStart()
        .appendZoneOrOffsetId()
        .optionalEnd()
        .optionalStart()
        .appendOffset("+HHMM", "0000")
        .optionalEnd()
        .toFormatter();
```

Új metódus: `DateTimeFormatBuilder.appendDayPeriodText(TextStyle style)`

---

## Unix-Domain Socket Channels

* `SocketChannel` és `ServerSocketChannel` API támogatja a Unix-Domain Socketek használatát
* Inter-process communication (IPC) on same physical host
* Kezdetben Unix, de már Windowson is elérhető
* Mikor használjuk loopback IP-cím és port helyett?
  * Sebesség, hiszen nem megy át a TCP/IP stacken, azaz több hálózati rétegen
  * Biztonság
* Pl. Docker serverrel való kommunikációra

---

class: inverse, center, middle

# További Java 16 újdonságok

---

## Value-Based Classes

* `final` és immutable
* `equals`, `hashCode`, és `toString` metódusok az állapota alapján
* Nem használható `==` összehasonlításra, két különböző objektumnak lehet egyező hashCode-ja, nem használható szinkronizálásra
* Az összehasonlításuk `equals()` metódussal történik, nem `==` operátorral
* Nincs konstruktoruk, csak gyártó metódusok
* Ahol az egyik objektumot használhatom, ugyanott használhatom az ugyanazzal az állapottal rendelkező
  másik objektumot is.

JDK-ban:

* Csomagoló osztályok - konstruktorok ezért lettek Java 9-ben deprecatedek
* `java.time` osztályai
* `Optional`

---

## Warnings for Value-Based Classes

* `@jdk.internal.ValueBased` annotáció
* Fordító hibát jelez, ha a tiltott műveleteket akarjuk vele végezni (JDK-n belül)
* Továbblépés a Valhalla Project felé
    * Value objects és user-defined primitives
    * Mivel nem klasszikus objektumok (érték számít, nem az identity), hatékonyabban kezelhetőek
    * Hatékonyabban ábrázolhatóak a memóriában, büntetlenül másolhatóak

---

## Elastic Metaspace

* Metaspace
  * Osztálydefiníciók, String intern, és static változók értékeinek tárolására
  * Automatikusan méretezhető
  * PermGent váltotta le a Java 8-ban, mely sokszor betelt (tipikusan class loader problémából adódóan alkalmazásszervereken való redeploy-kor)
  * GC a nem használt osztályokat törli
* Változtatás célja:
  * Gyorsabban adja vissza a nem felhasznált területet az operációs rendszernek
  * Metaspace mérete legyen kisebb
  * Kódegyszerűsítés a könnyebb karbantarthatóságért

---

## Packaging Tool

* Alkalmazás (és mellé a JDK) becsomagolása egy natívan futtatható exe fájlba

```shell
jpackage --input target --name LocationsCli --main-jar locations.jar --type exe --win-console 
    --app-version 1.0.0
```

```shell
Can not find WiX tools (light.exe, candle.exe)                                                  
Download WiX 3.0 or later from https://wixtoolset.org and add it to the PATH.
Error: Invalid or unsupported type: [exe] 
```

---

## Strongly Encapsulate <br /> JDK Internals by Default 

* Java Platform Module System levédi a Java saját osztályait a reflectionnel szemben (Java 16-tól nem open)

```java
var value = String.class.getDeclaredField("value");
value.setAccessible(true);
```

```plaintext
Exception in thread "main" java.lang.reflect.InaccessibleObjectException: 
    Unable to make field private final byte[] java.lang.String.value accessible: 
    module java.base does not "opens java.lang" to module javase
```