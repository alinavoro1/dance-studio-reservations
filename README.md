# 🕺 Dance Studio Manager

**Dance Studio Manager** este o aplicație Java complexă, bazată pe consolă, creată pentru a gestiona activitatea completă a unui studio de dans. Aplicația respectă principiile programării orientate pe obiecte (OOP) și include persistență a datelor (bază de date relațională MySQL) și un sistem de audit.

---

## 🚀 Funcționalități Principale

### 👥 Gestiunea Persoanelor
* **Clienți:** Adăugare, căutare și vizualizare detalii clienți. Clienții beneficiază de sortare alfabetică automată folosind `TreeSet` și interfața `Comparable`.
* **Profesori:** Gestiunea profesorilor, asocierea stilurilor de dans (folosind colecții de tip `Set`) și vizualizarea claselor pe care aceștia le predau.

### 💳 Abonamente și Rezervări
* **Achiziție Abonamente:** Clienții pot achiziționa abonamente de diferite tipuri (*Single*, *Silver*, *Gold*), folosind principii avansate de moștenire (Inheritance) și interfețe (`Calculable`).
* **Rezervare Clase:** Sistem inteligent de rezervări cu validări (Ex: `AbonamentInvalidException`, `CapacitateMaximaException`). Datele de expirare ale abonamentelor sunt calculate automat la prima activare.

### 📅 Program și Clase de Dans
* **Orar Dinamic:** Adăugarea de clase noi cu profesori și săli specifice. Sistemul blochează asignările greșite (ex: profesorul trebuie să cunoască stilul clasei).
* **Filtrare Avansată:** Clasele pot fi filtrate rapid după `StilDans` sau `Dificultate` (utilizând *Enum*-uri).
* **Curățare Inteligentă:** Ștergerea unui interval orar va elimina clasa complet din sistem dacă acesta a fost ultimul ei interval.

### ⭐ Sistem de Review-uri
* **Feedback:** Clienții pot lăsa recenzii (1-5 stele) și comentarii text atât pentru profesori, cât și pentru clasele de dans.

### 🛡️ Audit și Persistență
* **Bază de Date (JDBC):** Toate datele sunt salvate persistent într-o bază de date MySQL (operațiuni CRUD depline prin stratul de Repositories/DAO).
* **Audit automat (CSV):** Orice acțiune efectuată în sistem (ex: *adauga_client*, *rezervare_clasa*) este înregistrată instantaneu cu un Timestamp (format ISO-8601) în fișierul `audit.csv`, utilizând un serviciu `Singleton`.

---

## 🛠️ Arhitectură și Tehnologii
Aplicația bifează cu succes toate cerințele pentru un proiect universitar complet:

* **Limbaj:** Java 17+
* **Build Tool:** Maven
* **Bază de date:** MySQL (via JDBC)
* **Colecții folosite:** `List`, `Set`, `Map`, inclusiv colecții sortate (`TreeSet`).
* **Structură:** Arhitectură pe straturi: `model` -> `repository` (DAO) -> `service` -> `Meniu` (UI).
* **Validări:** Excepții tratate riguros (Blocuri `try-catch` pentru `NumberFormatException`, `DateTimeParseException`).

---

## ⚙️ Rulare Proiect

1. **Baza de Date:** Asigură-te că instanța de MySQL (sau containerul Docker asociat) rulează local pe portul corect (`3308` conform configurărilor din `DatabaseConnection`).
2. **Inițializare Schema:** Nu este nevoie să creezi manual tabelele. Aplicația citește și execută fișierul `schema.sql` la fiecare pornire pentru a se asigura că baza de date este validă.
3. **Pornire:** Rulează clasa `main.Main` direct din IDE-ul tău (ex: IntelliJ IDEA, VS Code).
4. **Navigare:** Folosește consola pentru a introduce numerele corespunzătoare opțiunilor dorite din meniul interactiv.

---
*Proiect dezvoltat cu ❤️ pentru pasionații de dans și cod curat.*
