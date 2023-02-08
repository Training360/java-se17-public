class: inverse, center, middle

# Java 12-17 újdonságok

---

class: inverse, center, middle

# Szükséges alapfogalmak

---

## Újdonságok megjelenésének <br /> folyamata

* JEP: JDK Enhancement Proposal, OpenJDK és Oracle JDK specifikus javaslatok <br /> kezelésének folyamata
* JCP: Java Community Process, szervezet és folyamat a specifikációk kezelésére
* JSR: Java Specification Requests, a JCP-n belül fejlesztett specifikációk
* LTS - Long-term support
  * Többi is production-ready, azonban a következő verzióval megszűnik a támogatása
* [Java version history - Wikipedia](https://en.wikipedia.org/wiki/Java_version_history)
  * Verziók részletes leírása
* [Quality Outreach - OpenJDK](https://wiki.openjdk.java.net/display/quality/Quality+Outreach)
  * Free Open Source Software (FOSS) keretrendszerek és library-k teszteltsége különböző JDK-kon

---

## Preview, experimental, incubator

* A preview majdnem végleges
* Az experimental erősen fejlesztés alatt lévő
* Az incubator fejlesztések külön, `jdk.incubator` prefix-szel rendelkező modulokban jelennek meg
* Lehet, hogy egyiket sem viszik tovább későbbi Java verziókba

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.10.1</version>
    <configuration>
        <compilerArgs>
            --enable-preview
        </compilerArgs>
    </configuration>
</plugin>
```

---

class: inverse, center, middle

# Java 12 újdonságok

---

## Java 12 megjelenése

* Kiadás: 2019. március

---

## Java 12 újdonságai

* API újdonságok (`String`, `Files`, `Collectors`, `InputStream`, `CompletableFuture`)
* JVM constants API - JEP 334
* Abortable Mixed Collections for G1 - JEP 344
* G1 gyakrabban tudja visszaadni az operációs rendszernek a nem használt memóriát - JEP 346
* Default CDS Archives - JEP 341
* Microbenchmark Suite - JEP 230
* Egy 64 bites ARM implementáció - JEP 340

---

## Preview, experimental, incubator

* Switch expressions (Preview) - Java 16-ban végleges - JEP 325
* Shenandoah: A Low-Pause-Time Garbage Collector (Experimental) - Java 15-től végleges - JEP 189

---

class: inverse, center, middle

# API újdonságok

---

## API újdonságok

* `String` `indent`
  * Negatív számmal is meghívható
  * A Tab karaktert is egy karakternek, whitespace karakternek számolja
  * Normalizing line ending: utolsó sor végére, ha szükséges, betesz egy sortörést, kicseréli a `\r\n` sorvégét `\n`-re
* `String` `transform`

```java
var text = "Lorem ipsum";
var transformed = text
  .transform(input -> input.concat(" dolor sit amet"))
  .transform(String::toUpperCase);
```

---

## Mismatch

* `Files.mismatch(Path, Path)`
  * Két fájlt összehasonlít, és az első eltérő byte pozícióját adja vissza

---

## Teeing

* `Collectors` `teeing()` két collector eredményét kombinálja

```java
var employees = List.of(
        new Employee("John Doe", 1970),
        new Employee("Jack Doe", 1980),
        new Employee("Jane Doe", 1990));

var boundaries = employees.stream().collect(
        Collectors.teeing(
                Collectors.minBy(Comparator.comparing(Employee::getYearOfBirth)),
                Collectors.maxBy(Comparator.comparing(Employee::getYearOfBirth)),
                (e1, e2) -> new Boundaries(e1.orElseThrow(), e2.orElseThrow())
        )
);
```

---

## Compact number

* `NumberFormat` `getCompactNumberInstance()`

```java
var format = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
System.out.println(format.format(1000));
System.out.println(format.format(100_000));
System.out.println(format.format(1_000_000));
```

```plaintext
1K
100K
1M
```

---

## Compact number - <br /> hosszú formátum

`NumberFormat.Style.LONG`

```plaintext
1 thousand
100 thousand
1 million
```

---

## Compact number - <br /> magyar nyelv

`new Locale("hu", "HU")`

```plaintext
1 E
100 E
1 M

1 ezer
100 ezer
1 millió
```

---

## `InputStream.skipNBytes`

* Már létező `long skip(long n)` metódus
  * Megpróbál `n` byte-ot átugorni, majd visszaadja, mennyit sikerült
  * Pl. fájl vége esetén nem tud ugorni
* `void skipNBytes(long n)`
  * Mindenképp pontosan `n` byte-ot ugrik, ha nem sikerül, kivételt dob (`EOFException`)

---

## CompletableFuture API

* CompletableFuture egy keretrendszer aszinkron feladatok létrehozására, <br /> kombinálására és futtatására
* Több, mint 50 metódus

```java
class CompletableFuture<T> implements Future<T>, CompletionStage<T>
```

Új metódusok:

`CompletitionStage` `exceptionallyAsync`, `exceptionallyCompose`, `exceptionallyComposeAsync`

* Kivétel esetén `Function` futtatása párhuzamosan, összefűzése más `CompletitionStage` példánnyal, összefűzése párhuzamosan más `CompletitionStage` példánnyal

---

class: inverse, center, middle

# JVM constants API

---

## JVM constants API 1.

* Minden Java osztály rendelkezik egy constant pool táblával
* A constant pool ún. loadable constant értékeket tartalmaz
  * Minden loadable constant áll egy tagből, mely a típust adja meg (pl. Class, Method reference, String, stb.) és különböző leíró információkból
* Bizonyos JVM bájtkód utasítások, mint az `ldc` és `invokedynamic` ezeket használja
  * `ldc` betölti az operand stackre (ezek lesznek a műveletek paraméterei)
  * `invokedynamic` paramétereként használható (pl. a lambda kifejezések mögött)
* Az előbbi utasítások futtatásakor ezek az értékek betöltésre kerülnek, "élővé válnak" (live value)
* Amikor bájtkódot manipulálunk Java programmal, ezeket olvashatjuk/írhatjuk
* Sok esetben azonban a reflection nem használható
* String, Integer, stb. esetén még egyszerű, de osztályokra, metódusokra való referenciáknál nehéz

---

## JVM constants API 2.

* Ezért hoztak létre egy hierarchiát a `java.lang.constant` csomagban, ami ebben segít, melynek gyökere a `ConstantDesc` interfész
* Ez az ún. _nominal type_, mely nem maga a constant poolban lévő érték, hanem leírja, hogy kell azt a constant poolban ábrázolni, onnan betölteni ("élővé tenni")
* Innentől `class String implements Constable, ConstantDesc`
* `ConstantDesc` leszármazottai pl. a `ClassDesc` vagy `MethodHandleDesc`, melyek egy osztályt vagy egy hívást reprezentálnak
* `LambdaMetafactory` implementációja egyszerűbb lett
  * Ez az, ami a lambda kifejezések és metódus referenciák mögött elkészít egy példányt, mely implementálja a megfelelő funkcionális interfészt. Majd tipikusan az `invokedynamic` használatával továbbítja a hívást egy `MethodHandle` példánynak, mely egy referencia a hívandó metódusra.
  * `MethodHandle` API: egy Reflectionnél alacsonyabb szintű, gyorsabb, nagyobb funkcionalitással rendelkező, de nehezebben használható API

---

## JVM constants API <br /> a gyakorlatban

* Hasznos
  * class fájlt módosító library-khez
  * fordítási vagy linkelési időben futó statikus analízis eszközökhöz
  * üzleti alkalmazásoknál nincs jelentősége
* Reflection használata nélkül, egyszerű típusok használatával, szabványos módon tudunk bájtkódot manipulálni

---

class: inverse, center, middle

# JVM és tool módosítások

---

## Szemétgyűjtő


* Abortable Mixed Collections for G1
  * Ha túl sokáig tart a szemétgyűjtés, átlép egy határt, abba tudja hagyni a teljes lefutás nélkül
* G1 gyakrabban tudja visszaadni az operációs rendszernek a nem használt memóriát
  * A G1 alapvetően Full GC-kor és concurrent gyűjtésnél adja vissza, melyek ritkák (ugyanis a G1 ügyel, hogy ne teljen meg a memória)
  * Beiktat concurrent gyűjtéseket (így hamarabb visszaadható az operációs rendszernek)

---

## Default CDS Archives

* Több párhuzamosan futó JVM esetén csökkenti a memóriafoglalást és elindulás idejét
* JDK buildeléskor kerül legenerálásra, és a disztribúció része
  * Windows esetén `bin\server\classes.jsa`
* Második JVM indításakor az felhasználja az első által memóriába töltött archive-ot, mely gyorsabb, mint az
  osztálybetöltés

`java -verbose:class`

```
[0.020s][info][class,load] java.lang.Object source: shared objects file
[0.020s][info][class,load] java.io.Serializable source: shared objects file
[0.020s][info][class,load] java.lang.Comparable source: shared objects file
[0.020s][info][class,load] java.lang.CharSequence source: shared objects file
...
[0.134s][info][class,load] org.w3c.dom.Node source: jrt:/java.xml
```

---

## Microbenchmark Suite

* Microbenchmarking nagyon nehéz
  * JVM belső optimalizációi miatt, pl. a JIT
* Van erre eszköz: [Java Microbenchmark Harness - JMH](https://openjdk.java.net/projects/code-tools/jmh/)
* A JDK forráskód mellett helyezték el a performancia teszteket (azaz benchmarkokat), így könnyebben lehet ezeket futtatni és újakat készíteni

---

## Egy 64 bites ARM implementáció

* Két implementáció volt, az egyik eltávolításra került