Salomia Maria Stefania 321CD

# Descrierea Implementării

Am scris codul cât mai accesibil pentru viitoare modificări. Am implementat 4 design pattern-uri: **Command**, **Strategy**, **Factory** și **Visitor**.

## **Command**
- Am folosit acest pattern pentru comenzile de intrare (ex. `printUsers`, `addAccount`, `createCard`, etc.).
- Am un **Invoker** care primește comanda de la Client (Main) și îi apelează metoda `execute`.
- Fiecare comandă este reprezentată de o clasă concretă care implementează interfața `Command` și ține loc de **Receiver**.

---

## **Strategy**
- Am utilizat Strategy pentru implementarea metodelor de plată: 
  - Plata online cu cardul.
  - Plata prin transfer bancar.
  - Retrageri de la bancomat.
- Cele trei clase concrete implementează interfața `PayStrategy` cu metoda generică `pay()`.
- **Clasa `PaymentContext`** are atribuția de a executa plata prin metoda cerută.
  - Comanda `payOnline` vizează plata cu cardul.  
    - Moneda în care se face această plată poate să nu coincidă cu moneda contului din care se face plata.
    - Pentru aceasta, în `InfoBank`, am implementat o metodă recursivă `exchange` care gestionează schimburile valutare:
      - Metoda `exchange` apelează `recursiveExchange`, care:
        - Se oprește și returnează suma dacă moneda `from` coincide cu moneda `to`.
        - Utilizează un vector `visited` pentru a evita ciclurile infinite.
        - Continuă să caute conversiile directe care nu au fost vizitate.
    - După o plată cu cardul:
      - Se scade suma din cont (convertită în moneda contului).
      - Dacă cardul utilizat este de unică folosință, acesta se șterge după utilizare și se generează altul.

- **Plata prin transfer bancar** poate fi efectuată și către comercianți:
  - Când caut contul receptor, verific atât conturile utilizatorilor, cât și conturile comercianților.
  - Nu se adaugă bani în conturile comercianților.

---

## **Visitor**
- **Clasa `Account`** este extinsă în:
  - `ClassicAccount` pentru conturi clasice.
  - `SavingsAccount` pentru conturi de economii.
  - `BusinessAccount` pentru conturi de afaceri.
    - Conturile de economii au un câmp suplimentar pentru dobânda încasată pe suma păstrată în cont.
    - Conturile de afaceri au:
      - Un **owner**.
      - O listă cu **angajați**.
      - O listă cu **manageri** care gestionează contul.
      - Limite de retragere și depunere pentru angajați.
    - Având în vedere că angajații au limite pentru plăți și depuneri, am implementat metode specifice folosind pattern-ul **Visitor**.

- Clasele `Owner`, `Employee` și `Manager` extind `User`:
  - Chiar dacă pentru `User` și `Owner` nu am implementat metoda `visit`, am lăsat-o accesibilă pentru viitoare modificări.

---

## **Factory**
- Am folosit Factory pentru **cashBack**:
  - Există două tipuri de cashback:
    - Pe suma cheltuită.
    - Pe numărul de tranzacții efectuate.
  - La fiecare plată online sau transfer bancar către un comerciant:
    - Verific tipul de cashback asociat comerciantului.
    - Creez instanța specifică folosind `CashBackFactory`.

---

## Alte Clase Importante

### **Clasa `Constants`**
- Reține constantele utilizate în proiect.
- Are o metodă `getValue()` care returnează valoarea necesară.

### **Clasa abstractă `JsonOutput`**
- Afisează output-ul în format JSON.
- Este abstractă pentru a nu putea fi instanțiată direct.
- Metodele sunt apelate cu ajutorul numelui clasei.

### **Clasa `SpliPayment`**
 - am retinut campurile pentru o plata distribuita si in plus un contor pentru accepturi si refuzuri ale platii. Cand primesc comanda de spliPayment adaug aceasta plata in coada de asteptare specifica fiecarui utilizator. La fiecare accept creste contorul pentru respectivul split din lista fiecarui utilizator implicat. Dupa ce numarul de accepturi ajunge egal cu numarul conturilor implicate se va incerca efectuarea platii. Exista doua tipuri de astfel de plati: fiecare plateste in mod egal sau fiecare plateste cat a consumat.
--- 

## Raport De Afaceri

### **Raport pentru tranzactii**
 - am folosit un HashMap pentru manageri si un HashMap pentru angajati specifica fiecarui cont in care retin momentul platii drept cheie, si in acelasi timp datorita clasei Pair suma platita si tipul tranzactiei pentru a stii daca contorizam la deposit sau la spent.
 
### **Raport pentru comercianti**
  - am folosit un HashMap pentru comercianti specific fiecarui cont in care retin momentul platii drept cheie si folosind clasa Pair numele comerciantului, suma platita si email-ul celui care face plata. Folosesc o lista de clienti specifica fiecarui comerciant care retine si momentul in care clientul face plati. AStfel pot afisa pentru fiecare comerciant suma incasata si numele celor care au facut plata intr-un anumit interval de timp.

