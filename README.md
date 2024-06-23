# DentalHub

Cod sursă: https://github.com/Iasmina012/DentalHub

## Descriere
Acest proiect dorește dezvoltarea unei aplicații mobile intuitive și ușor de utilizat de către utilizatorii ei, indiferent de rolul pe care aceștia îl au (pacient, doctor sau admin). Utilizatorii pot să efectueze programări la dentist, să le modifice, să le anuleze, să acceseze informații detaliate despre serviciile oferite de către echipa cabinetului (despre doctori, servicii, tehnologii utilizare în tratamentul pacienților, metode de contactare) și să primească sfaturi utile despre igiena dentară. De asemenea, pentru o mai bună informare a pacientului, aplicația va include o funcționalitate de auto-diagnosticare, unde utilizatorii pot selecta dintr-o lista simptomele care îi supără și pot primi un diagnostic provizoriu bazat pe acestea până la întâlnirea cu medicul specialist. În plus, adminul are rolul de a marca prezența sau absența pacientului la programarea respectivă. Toate acestea aduc trimiterea unor notificări automate în cazul anulării, modificării și un memento cu 24 de ore înaintea unei programări, dar și pentru prezența sau absența pacientului.

## Instalare

### Clonarea proiectului

Pentru a putea clona proiectul de pe GitHub trebuie să deschideți un terminal și să rulați comanda următoare:

```bash
git clone https://github.com/Iasmina012/DentalHub
```
### Deschiderea proiectului în Android Studio

- Deschideți Android Studio;
- Selectați "File > Open an existing Android Studio project";
- Navigați către directorul în care a fost clonat proiectul;
- După ce ați deschis proiectul, Android Studio va arăta mesajul pentru a-l putea sincroniza cu Gradle, trebuie să apăsați pe "Sync Now" sau pe "File > Sync Project with Gradle Files".

### Configurare Firebase
- Creați propriul proiect Firebase aici https://console.firebase.google.com urmând pașii de acolo;
- Asigurați-va că proiectul este conectat la Firebase corect (vezi pașii în Android Studio la "Tools > Firebase");
- Descărcați fișierul "google-services.json" din setările proiectului în cadrul Firebase;
- Adăugați fișierul "google-services.json" în directorul principal al aplicației;
- Adăugați dependențele corespunzătoare Firebase în fișierul "build.gradle" dacă este cazul;
- Trebuie să înlocuiți peste tot în cod unde găsiți link-ul "https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app" cu cel din baza dvs. de date Firebase.

## Compilare
Folosiți "Build > Make Project" ca să compilați aplicația.

## Rulare
Aplicația poate fi rulată pe dispozitivul Android personal sau creând un emulator în Android Studio:

1. _Dispozitiv Android_
- Pe dispozitvul dvs. mergeți la "Settings > About phone > Software information > More" și apăsați "Build number" de 7 ori până când este activat modul "Dezvoltator";
- Pe dispozitvul dvs. mergeți la "Settings > Developer options" și activați opțiunea "USB Debugging";
- Conectați dispozitivul dvs. la calculator/laptop folosind un cablu USB;
- Selectați dispozitivul dvs. conectat din lista dispozitivelor disponibile;
- În Android Studio apăsați pe butonul "Run".

2. _Emulator Android_
- În Android Studio navigați la "Tools > AVD Manager";
- Apăsați pe "Create Virtual Device";
- Alegeți un dispozitiv din lista de dispozitive disponibile (exemplu: Pixel 4) și apăsați "Next";
- Alegeți o versiune de Android pentru emulator, însă este recomandat să alegeți una dintre cele mai recente versiuni ( > Android 11);
- Dacă nu aveți deja descărcată imaginea sistemului, apăsați "Download" pentru a o putea descărca;
- Personalizați setările emulatorului dacă este necesar (memorie RAM, spațiu de stocare, etc.);
- Apăsați "Finish" pentru a crea emulatorul;
- Selectați emulatorul ca dispozitiv principal;
- Apăsați "Run", iar aplicația va fi instalată și lansată automat pe emulator.

## Lansare
Odată ce aplicația e instalată pe dispozitivul dvs. urmați pașii de mai jos:

- Pe dispozitivul dvs. Android căutați iconița aplicației;
- Apăsați pe iconița aplicației pentru a o putea lansa;
- Acum puteți începe să folosiți aplicația și toate funcționalitățile ei.

## Cerințe minime ale sistemului

### Windows:

- Windows 8/10/11 (64-bit)
- 4 GB RAM minim (8 GB RAM recomandat)
- 2 GB spațiu liber pe disc minim (4 GB recomandat)
- Rezoluție ecran de minim 1280 x 800
- Procesor cu suport pentru virtualizare
- GPU: GPU compatibil cu OpenGL ES 2.0

### macOS:
- macOS 10.14 Mojave sau mai recent
- 4 GB RAM minim (8 GB RAM recomandat)
- 2 GB spațiu liber pe disc minim (4 GB recomandat)
- Rezoluție ecran de minim 1280 x 800
- Procesor cu suport pentru virtualizare
- GPU: GPU compatibil cu OpenGL ES 2.0

### Linux:
- Distribuție recentă de 64-bit, cum ar fi Ubuntu 18.04+
- GNU C Library (glibc) 2.31 sau mai recent
- 4 GB RAM minim (8 GB RAM recomandat) 
- 2 GB spațiu liber pe disc minim (4 GB recomandat)
- Rezoluție ecran de minim 1280 x 800
- Procesor cu suport pentru virtualizare
- GPU: GPU compatibil cu OpenGL ES 2.0

### Java Development Kit (JDK)
- JDK 8 sau mai recent (recomandat JDK 11)

### Android Studio
- Versiunea Android Studio 8.0 sau mai recentă
- Android SDK 26 sau mai recentă

---

_Această aplicație a fost dezvoltată pe macOS Ventura 13.5.1 în Android Studio Iguana 2023.2.1 Patch 2 cu JDK 17.0.2_
