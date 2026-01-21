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

#### A. Meniul Principal
Utilizatorul pornește aventura din ecranul de start, având acces la secțiunile: Start Joc, Colecție, Cufere și Ieșire.
<p style="align: center;">
<img src="./resources/home-screen.png" alt="Meniu Principal" width="300">
</p>

#### B. Tabla de Joc (Model Monopoly)
Traseul este reprezentat prin județele României. Utilizatorul alege între geografie și istorie, iar avansarea depinde de corectitudinea răspunsurilor.
<p style="align: center;">
<img src="./resources/in-game-screen-start.png" alt="Tabela de Joc" width="300">
</p>


#### C. Motorul de Quiz și Sistemul de Indicii
Fiecare județ oferă o întrebare. În caz de eroare, utilizatorul primește un indiciu vizual pentru a încuraja învățarea.
<p style="align: center;">
<img src="./resources/in-game-question-hint.png" alt="Quiz cu Indiciu" width="300">
</p>


#### D. Sistemul de Recompense (Cufere și Stickere)
Punctele acumulate permit achiziționarea de cufere. Stickerele colectabile sunt împărțite pe rarități: comun, rar, epic, legendar. Stickerele necolectate apar blurate pentru a stimula curiozitatea.
<p style="align: center;display:column;">
<img src="./resources/sticker-collection-owned.png" alt="Colecție Stickere" width="300">
<img src="./resources/sticker-collection-unknown.png" alt="Stickere Necunoscute" width="300">
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