# Explore România - Proiect HCI
**Echipa Ubibisoft**

![People](https://metrics.lecoq.io/alexvladu/ubibisoft?template=classic&plugin_people=true)

---

## 🇷🇴 Română

### 1. Introducere și Cerință
Aplicația „Explore România” este un instrument didactic sub formă de joc, creat pentru a ajuta elevii să aplice cunoștințele de istorie și geografie dobândite la școală. Aceasta îmbină elementele curriculare cu gândirea logică pentru a dezvolta o experiență de învățare profundă.

### 2. Analiza Utilizatorilor (Cunoașterea utilizatorilor țintă)
- **Eșantion:** S-a lucrat cu 30 de elevi de clasa a IV-a și un cadru didactic pentru extragerea cerințelor.
- **Vârstă:** Utilizatori între 10-12 ani.
- **Nivel Tehnologic:** Ridicat (mediu urban), cu acces constant la smartphone-uri și tablete.
- **Mediul de utilizare:** Mixt (școală sau acasă).
- **Preferințe:** Interactivitate predominantă și explorarea hărților.

### 3. Arhitectură Tehnică & Tehnologii
Proiectul este o aplicație Android modernă, construită pe următoarea stivă:
- **Limbaj:** Kotlin.
- **UI Framework:** Jetpack Compose (declarativ).
- **Dependency Injection:** Hilt (Dagger).
- **Persistență:** DataStore (scoruri și colecția de stickere).
- **Resurse Media:** Coil (imagini) și Media Player (audio).
- **Gestiune Date:** Fișiere `JSON` (`intrebari_geografie.json`, `info_judete.json`) și `CSV`.

### 4. Implementare Prototip Executabil (Screenshots)

#### A. Meniul Principal și Personalizarea Profilului
Utilizatorul pornește aventura din ecranul de start, având acces la secțiunile principale: Start Joc, Colecție, Cufere și Ieșire. Înainte de a începe explorarea, aplicația permite configurarea identității jucătorului:

<p align="center">
    <img src="./resources/home-screen.png" alt="Meniu Principal" width="500">
</p>

- **Alegerea Numelui**: Utilizatorul își poate introduce numele pentru a personaliza experiența de învățare și tabelul de scor.
- **Selecția Caracterului**: Răspunzând preferințelor elevilor pentru „jocuri cu personificarea unui caracter”, utilizatorul poate alege un avatar care să îl reprezinte pe harta interactivă.
- **Salvarea Progresului**: Datele profilului (numele și caracterul ales) sunt stocate local pentru a menține continuitatea între sesiunile de joc.

<p align="center">
    <img src="./resources/home-screen-character-1.png" alt="Caracter 1" width="150">
    <img src="./resources/home-screen-character-2.png" alt="Caracter 2" width="150">
    <img src="./resources/home-screen-character-3.png" alt="Caracter 3" width="150">
    <img src="./resources/home-screen-character-4.png" alt="Caracter 4" width="150">
</p>
Selecția personajului este realizată printr-un mecanism interactiv de tip „carousel”, care permite utilizatorului să parcurgă pe rând toate avatarurile disponibile. Navigarea între personaje se face prin acțiuni intuitive (de exemplu, butoane de tip stânga/dreapta sau glisare), fiecare caracter fiind afișat pe ecran în mod individual pentru o vizualizare clară.

Utilizatorul poate explora toate opțiunile disponibile înainte de a face o alegere, ceea ce îi oferă control asupra identității sale virtuale. La selectarea unui personaj, acesta este evidențiat vizual pentru a confirma opțiunea curentă.

După confirmarea selecției, avatarul ales este asociat profilului jucătorului și salvat local, urmând să fie utilizat pe parcursul întregii experiențe de joc, atât pe harta interactivă, cât și în cadrul celorlalte interfețe ale aplicației.



Prin intermediul acestui meniu principal și al opțiunilor de personalizare, aplicația oferă utilizatorului un prim contact intuitiv și atractiv, facilitând integrarea într-un mediu educațional interactiv și motivant.


**Resurse grafice personaje:**
Sistemul utilizează sprite-sheet-uri și ilustrații personalizate pentru a asigura animații fluide și un stil vizual unitar.
Ilustrațiile sunt concepute astfel încât să fie atractive pentru utilizatori și să se integreze armonios în mediul educațional al jocului. 
<p align="center">
    <img src="./resources/sprite_sheet_character-1.png">
    <img src="./resources/sprite_sheet_character-2.png">
    <img src="./resources/sprite_sheet_character-3.png">
</p>



#### B. Tabla de Joc (Model Monopoly)
Traseul este reprezentat prin județele României. Utilizatorul alege între geografie și istorie, iar avansarea depinde de corectitudinea răspunsurilor.
<p align="center">
<img src="./resources/in-game-screen-start.png" alt="Tabela de Joc" width="500">
</p>


#### C. Motorul de Quiz și Sistemul de Indicii
Fiecare județ oferă o întrebare. În caz de eroare, utilizatorul primește un indiciu vizual pentru a încuraja învățarea.
<p align="center">
<img src="./resources/in-game-question-hint.png" alt="Quiz cu Indiciu" width="500">
</p>


#### D. Sistemul de Recompense (Cufere și Stickere)
Punctele acumulate permit achiziționarea de cufere. Stickerele colectabile sunt împărțite pe rarități: comun, rar, epic, legendar. Stickerele necolectate apar blurate pentru a stimula curiozitatea.
<p align="center" style="display: column;">
<img src="./resources/sticker-collection-owned.png" alt="Colecție Stickere" width="500">
<img src="./resources/sticker-collection-unknown.png" alt="Stickere Necunoscute" width="500">
</p>

### 5. Accesibilitate și Evaluare
- **Interfață:** Butoane mari, colorate și contrast ridicat pentru diferențiere ușoară.
- **Audio:** Efecte sonore pentru o experiență distractivă.
- **Asistență:** Mesaje ajutătoare la întâlnirea unor funcționalități noi.
- **Evaluare Prototip:** În urma feedback-ului, s-a stabilit necesitatea unui design uniform și a persificării stickerelor.

### 6. Dezvoltări Ulterioare
* Introducerea unui cont de administrator (doamna învățătoare) pentru adăugarea de noi întrebări.
* Migrarea stocării de la local la remote pentru actualizarea dinamică a conținutului.
* Extinderea către noi domenii: chimie, fizică, biologie.

---

## 🇺🇸 English

### 1. Introduction and Goal
"Explore Romania" is a didactic game application designed for 4th-grade students. It combines school-taught history and geography with logical thinking to provide a deep learning experience.

### 2. User Analysis (HCI)
- **Target Audience:** Students aged 10-12.
- **Tech Literacy:** High, based in urban environments with constant tablet and smartphone access.
- **Environment:** Mixed use in classrooms (collaborative) or at home (inpidual).
- **Key Requirements:** Predominant interactive elements and character-based exploration.

### 3. Technical Implementation
- **Language:** Kotlin.
- **UI:** Jetpack Compose (Declarative UI).
- **Architecture:** Modular structure under `com.ububi.explore_romania`, featuring Hilt for DI and DataStore for persistence.
- **Data Handling:** Content-driven approach using `JSON` files for educational materials and sticker rarities.

### 4. Game Mechanics
- **Game Loop:** Users navigate a "Monopoly-style" board representing Romanian counties.
- **Quiz Engine:** Players choose between History and Geography questions. Correct answers reward points and progress, while incorrect ones provide helpful hints.
- **Sticker Rewards:** Points are spent on "Chests" to collect stickers categorized by rarity (Common, Rare, Epic, Legendary). Unlocked stickers are viewed in a collection album.

### 5. Accessibility and Feedback
- **Design:** Simple interface with large, high-contrast buttons.
- **Feedback:** Sound effects for engagement and pop-up help messages for new features.
- **Evaluation:** Prototype testing showed students desired more sticker diversity and more interactive county information.

### 6. Future Scope
- **Admin Access:** A specialized account for teachers to add curriculum-aligned questions.
- **Cloud Integration:** Shifting from local storage to remote methods for continuous content delivery.

---

### Referinţe bibliografice / References
1. Istorie clasa a IV-a, Cleopatra Mihailescu, Tudor Pitila
2. Geografie clasa a IV-a, Carmen Camelia Radulescu, Ionut Popa
