class: inverse, center, middle

# Java 15 újdonságok

---

## Java 15 megjelenése

* Kiadás: 2020. szeptember

---

## Tematika

* Text Blocks - JEP 378
* `String`, `DecimalFormat`, `Math`, `StrictMath` változások, `NoSuchElementException` új konstruktorok,
* Reimplement the Legacy DatagramSocket API - JEP 373
* Deprecate RMI Activation for Removal (Java 17-ben törölve) - JEP 385
* Hidden Classes - JEP 371
* ZGC - JEP 377
* Shenandoah: A Low-Pause-Time Garbage Collector - JEP 379
* Remove the Nashorn JavaScript Engine - JEP 372

---

## Preview, experimental, incubator

* Sealed Classes (Preview) - Java 17-ben végleges - JEP 360
* Pattern Matching for instanceof (Second Preview) - Java 16-ban végleges - JEP 375
* Records (Second Preview) - Java 16-ban végleges - JEP 384
* Foreign-Memory Access API (Second Incubator) - JEP 383
  * Project Panama keretein belül

---

class: inverse, center, middle

# Text Blocks, String és egyéb API változások

---

## Text Blocks

```java
String text = "{\n" +
                   "  \"name\": \"John Doe\",\n" +
                   "  \"age\": 45,\n" +
                   "  \"address\": \"Doe Street, 23, Java Town\"\n" +
                   "}\n";
```

```java
String text = """
            {
              "name": "John Doe",
              "age": 45,
              "address": "Doe Street, 23, Java Town"
            }
            """;
```

Megoldott problémák:

* Több soros konkatenáció, olvashatóság, idézőjelek escape-elése
* Copy-paste más eszközből

---

## Behúzás

* Megtartva a forráskód formázását
* A felesleges space-ek nem kerülnek a stringbe
* `\n` sortörést használ
* Sorvégi space-eket eltávolítja
* `String.stripIndent()` dokumentációjában leírva

---

## Text Blocks

* IDEA támogatás
  * _Replace with text block_
  * Zöld csíkkal jelöli, hogy mit vág le

---

## `stripIndent()`

```java
var htmlVisualized = """
                ....<html>
                ......<body>
                .........<h1 class="header">Hello World</h1>
                ......</body>
                ....</html>""";

System.out.println(htmlVisualized.replace(".", " ").stripIndent());
```

* Amennyiben az utolsó sorban még van egy sortörés, nem távolítja el a behúzást (hiszen az utolsó sorban nincs whitespace).

---

## `translateEscapes()`

```java
System.out.println("john\\tdoe"); // john\tdoe
System.out.println("john\\tdoe".translateEscapes()); // john	doe
```

---

## `String.formatted()`

```java
System.out.println(String.format("%s is %d years old", "John Doe", 20));
System.out.println("%s is %d years old".formatted("John Doe", 20));
```

---

## `CharSequence.isEmpty()`

* `interface CharSequence`
* Implementációi: `String`, `StringBuffer`, `StringBuilder`

---

## Más ezreselválasztó karakter <br /> pénznemek kiírásakor

```java
var locale = new Locale("de", "AT");

var number = DecimalFormat.getNumberInstance(locale);
System.out.println(number.format(10_000_000.10));
// 10 000 000,1

var currency = DecimalFormat.getCurrencyInstance(locale);
System.out.println(currency.format(10_000_000.10));
// € 10.000.000,10
```

* Ausztiában más az ezreselválasztó karakter számok és pénznemek kiírásakor
* `DecimalFormatSymbols` `get/setMonetaryGroupingSeparator()`

---

## `Math`, `StrictMath`

* Mindkettőben `absExact()` metódusok

```java
StrictMath.absExact(Integer.MAX_VALUE); // Lefut
StrictMath.absExact(Integer.MIN_VALUE); 
    // Exception in thread "main" java.lang.ArithmeticException: 
    //   Overflow to represent absolute value of Integer.MIN_VALUE
```

---

## `NoSuchElementException` <br /> új konstruktorok

* `NoSuchElementException(String s, Throwable cause)`, `NoSuchElementException(Throwable cause)`
* `cause` bevezetése

---

class: inverse, center, middle

# További API és JVM módosítások

---

## Reimplement the <br /> Legacy DatagramSocket API

* `java.net.DatagramSocket` és `java.net.MulticastSocket` API modernebb, karbantarthatóbb implementációja
* Régi, még JDK 1.0-ból, natív részeket tartalmaz
* Előkészítés a Project Loomra
* Konkurencia problémák, nehézkes IPv6 kezelés
* Régi `jdk.net.usePlainDatagramSocketImpl` visszakapcsolható

---

## Hidden Classes

* Javaban lehet olyan osztályokat létrehozni, amelyeknek nincs nevük, pl. anonymous inner classes
* JVM-nek limitációja volt, hogy minden osztálynak legyen neve
* Fordító generált hozzá nevet, dollárjellel
  * Javaban nem helyes osztálynév
  * JVM számára helyes osztálynév
* Ezeket is a class loader tölti be
  * Így mégis hozzá lehetett férni ezekhez
* Hidden classes: JVM tulajdonság, mely nem engedi ezekhez az osztályokhoz való hozzáférést
* Alkalmazásfejlesztő számára nem használt tulajdonság
  * Fordítóprogramok, bájtkódot módosító 3rd party library-k számára hasznos

---

class: inverse, center, middle

# Z szemétgyűjtő algoritmus

---

## ZGC

* Terabyte nagyságú heap-ek kezelésére
* 10 ms alatt a megállások
* Skálázható, memóriaterület nagyságának növekedésével ne nőjön a GC idő
* Max 15%-ot használjon a futási időből
* Felkészülés a multi-tiered heapre <br /> (flash és non-volatile memory elterjedésével, <br /> Non-volatile random-access memory (NV-RAM))
* Nagy felhasználószámmal rendelkező, nagy memóriát igénylő, gyorsan válaszoló backend alkalmazásoknál

---

## Jellemzői

* Régió alapú: memóriát sok különböző méretű darabra, ún. _page_-re osztja
  * Kisméretű page-ek 2MB, közepes page-ek 32MB, nagy méretű page-ek: 2MB többszöröse
  * Kisméretű page-en max. 256KB méretű objektumot tárol, közepesen 256KB - 4MB, nagyon >= 4MB
* Nem használ generációkat, ezek a page-ek egyenrangúak
* Szemétgyűjtő algoritmus részei
  * Stop-the-world fázisok - felfüggesztik az alkalmazás szálak működését
  * Concurrent fázisok - az alkalmazás szálakkal együtt futnak
  * Parallel fázisok - a GC-n belül is a munkát több szálon végzik
* Az objektumok felszabadításával a memória széttöredezik, fragmentálódik
  * ZGC objektumok átmozgatásával kezeli (compacting)
  * Teljes page-eket szabadít fel
* NUMA-ra felkészített: Non-uniform memory access (CPU-hoz közelebb eső memóriából gyorsabb az olvasás)
---

## Fő részei

* Marking
  * A használt és nem használt objektumok megjelölése
* Relocation
  * Defragmentálás, objektumok page-ek közötti átmozgatásával

---

## Fő részei - Marking

* Root references összegyűjtése (lokális változókból kiindulva) (stop-the-world), nagyon gyors
* Root references-ből elérhető objektumok megjelölése (concurrent, parallel)
  * Reference coloring, ún. színes pointerrel történik (64 bitből csak 48 bitet használ címzésre, a maradékból felhasznál egy bitet)
* Mark befejezése (stop-the-world)

---

## Fő részei - Relocation

* Ürítendő page-ek kiválasztása (concurrent, parallel) - mely page-en van a legkevesebb objektum (kijelölt page-ek: _relocation set_)
* Mozgatandó root objektumok átmozgatása, referenciák átírása (stop-the-world)
* Maradék objektumok átmozgatása (concurrent, parallel)
  * Nem írja át a referenciákat, hanem egy _forwarding table_ táblát használ a régi cím/új cím párokkal
  * A 48 biten felül felhasznál további egy bitet annak tárolására, hogy átmozgatás történt

A következő marking rész cseréli direkt referenciákra az indirekt referenciákat

---

## Load Barriers

* JVM által beillesztett kódrészletek, melyek az objektum referencia alapján történő betöltésekor futnak le
* Kezelik az indirekt hivatkozásokat
* Ha a jelzőbitek alapján direkt hivatkozás van, akkor azt használja
* Ha indirekt hivatkozás van, akkor a forwarding tábla alapján frissíti
* Két különböző helyen lévő (színben) eltérő referenciák hivatkozhatnak ugyanarra az objektumra (a szín jelöli, hogy a referencia volt-e már vizsgálva)
* Több utasítás, nagyobb CPU használat

---

class: inverse, center, middle

# Shenandoah szemétgyűjtő algoritmus

---

## Shenandoah

* Concurrent, parallel
* Cél: magas reszponzivitás, rövid stop-the-world
* Cserébe: nagyobb processzorterhelés és memória
* Nagyon hasonló a G1 szemétgyűjtőhöz
  * Régiókkal dolgozik
  * Különbség: objektumok átmozgatása másik régiókba is párhuzamosan tud történni
  * Indirection (brooks) pointer: objektum headerben lévő plusz adat, mely az átmozgatott
    objektumra mutat
  * Az eredeti objektumot nem törli, hanem beállítja a pointert, így az alkalmazással
    párhuzamosan képes futni

---

## Shenandoah lépések

* Init mark (stop-the-world): Root references összegyűjtése
* Concurrent mark (concurrent, parallel): megjelöli az elérhető objektumokat
* Final mark (stop-the-world): jelölgetés befejezése
* Concurrent Cleanup (concurrent): visszaadja azokat a régiókat, melyek csak eldobható objektumokkal vannak teli
* Concurrent Evacuation (concurrent): objektumok átmozgatása új régiókba: pointer kezelése
* Init Update Refs (stop-the-world): pillanatnyi megállás a következő fázis előkészítéséhez, egy szinkronizációs pont
* Concurrent Update References: feloldja az indirekt hivatkozásokat
* Final Update Refs (stop-the-world): root references feloldása
* Concurrent Cleanup (concurrent): visszaadja azokat a régiókat, melyek csak eldobható objektumokkal vannak teli 