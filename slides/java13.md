class: inverse, center, middle

# Java 13 újdonságok

---

## Java 13 megjelenése

* Kiadás: 2019. szeptember

---

## Tematika

* `DocumentBuilderFactory` Namespace aware metódusok, pl. `newDefaultNSInstance()`
* NIO módosítások
* Socket API újraírása - JEP 353
* Dynamic CDS Archives - JEP 350

---

## Preview, experimental, incubator

* ZGC adja vissza az operációs rendszernek a nem használt memóriát - Java 15-ben végleges - JEP 351
* Text Blocks (Preview) - Java 15-ben végleges - JEP 355
* Switch Expressions (Preview) - Java 14-ben végleges - JEP 354

---

class: inverse, center, middle

# `DocumentBuilderFactory`

---

## `DocumentBuilderFactory` <br /> namespace aware metódusok

* `newDefaultNSInstance()`, `newNSInstance()`, `newNSInstance(String factoryClassName, ClassLoader classLoader)`
* A `setNamespaceAware(true)` hívást váltja ki
* A parser figyelembe veszi a névtereket is

---

class: inverse, center, middle

# NIO módosítások

---

## NIO módosítások

* NIO: Java 1.4-ben megjelent alternatív IO kezelés
* Inkább New IO, mint Non-blocking IO, hiszen vannak benne még blocking részek
* Azonban tényleg lehetővé teszi a non-blocking működést
* Operációs rendszer lehetőségeit használja ki (Linux, Windows)
* Nem várja meg az olvasás eredményét, hanem egy callback, mely visszahívásra kerül, ha előállt az adat
* `java.nio.Buffer`, pl. `ByteBuffer` osztályok - olyan pufferterület, mely konkrétan az operációs rendszerhez kötött.
  Ezért pl. fájl olvasáskor nem kell az operációs rendszer memóriájából a JVM memóriájába átmásolni, így CPU-t takarít meg
* `java.nio.channels.Channel`, pl. `AsynchronousFileChannel` interfészek - `Buffer` írás és olvasás, tipikusan fájl és socket felé vagy felől
* Java 13 módosítások
  * `FileSystem` létrehozására egyszerűbben használható metódusok
  * Különböző `Buffer` osztályokban megjelentek `get` és `put` metódusok, melynek nem kell offsetet átadni

---

class: inverse, center, middle

# Socket API és Project Loom

---

## Socket API újraírása

* `java.net.Socket` és `java.net.ServerSocket` API modernebb, karbantarthatóbb implementációja
* Régi, még JDK 1.0-ból, natív részeket tartalmaz
* Előkészítés a Project Loomra

---

## Threadek

* Régi eszköz párhuzamos alkalmazások írására: `Thread`
* Operációs rendszer kerneljének szálaira épül
* Létrehozása költséges, megoldás: thread pool
* Számossága erősen korlátozott
* Megnőtt az igény jelentős számú felhasználó párhuzamos kiszolgálására
* Konkurencia kezelés problémás

---

## Project Loom <br /> - virtual threads

* Új eszköz párhuzamos alkalmazások írására
* JVM által (és nem az OS által) vezérelt szálak
* Akár több millió is létrehozható
* `Thread` modellhez illeszkedik
* Létrehozása nem költséges, ezért poolozni sem szükséges
* Leképezhető egy vagy több OS szálra
* IO-ra várás (fájl, hálózat) az OS szálakkal ellentétben nem erőforrásigényes
* Performancia szempontjából kiválthatják a reaktív programozást (funkcionalitásban, könnyű olvashatóságban nem)
* Java 19-esben preview

---

## Project Loom <br /> - structured concurrency

* Egyszerűbb, hibamentesebb, erőforráskímélőbb párhuzamosság
* Ha az `X` szál indít egy `Y` szálat, akkor az `Y` nem futhat tovább, mint az `X`
* Java 19-esben experimental

---

class: inverse, center, middle

# Dynamic CDS Archives

---

## Dynamic CDS Archives

* Java 10-től Application Class-Data Sharing (AppCDS)
  * Alkalmazás osztályai is bekerülhetnek
  * Hiszen a class loaderek innen is tudnak betölteni
* Java 12-ben Default CDS Archives (`bin\server\classes.jsa`)
* Java 13-tól nem kell újra létrehozni, hanem futás közben képes a még nem benne lévő osztályokat beletenni

---

## Class loaderekről röviden

* Java 9-től class loader változás a Java Platform Module System miatt
  *  bootstrap class loader (null), standard osztálykönyvtár bizonyos moduljainak, osztályainak betöltéséért
  *  platform class loader (extension class loader lett átnevezve, az a 9-es verzióban ugyanis kivezetésre került), standard osztálykönyvtár további moduljainak, osztályainak betöltéséért (amelyek nem kapnak teljes jogosultságot a SecurityManagertől)
  *  application class loader (más néven system class loader), standard 3rd party libek, alkalmazás osztályainak betöltéséért
* Delegálás

---

## Class loaderek tettenérése

```java
// Bootstrap
System.out.println("class loader for String: "
                + java.lang.String.class.getClassLoader());

// Platform
System.out.println("class loader for DataSource: "
        + javax.sql.DataSource.class
        .getClassLoader());

// Application
System.out.println("class loader for this class: "
        + ClassLoaderDemo.class.getClassLoader());
```

---

## Mely modult <br /> mely class loader tölti be

```java
ModuleLayer layer = ModuleLayer.boot();
layer.modules().forEach(module -> {
    var classLoader = module.getClassLoader();
    var classLoaderName = Objects.isNull(classLoader) ? "bootstrap" : classLoader.getName();
    System.out.println(classLoaderName + ": " + module.getName());
});
```