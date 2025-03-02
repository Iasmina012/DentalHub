# DentalHub

#RO

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

# EN

Source Code: https://github.com/Iasmina012/DentalHub

## Description
This project aims to develop an intuitive and user-friendly mobile application for its users, regardless of their role (patient, doctor, or admin). Users can schedule dentist appointments, modify or cancel them, access detailed information about the services offered by the dental office team (doctors, services, technologies used in patient treatment, contact methods), and receive useful advice on dental hygiene.

Additionally, to better inform the patient, the application will include a self-diagnosis feature where users can select symptoms from a list and receive a preliminary diagnosis based on them until they meet with a specialist. Furthermore, the admin has the role of marking the patient's presence or absence at the scheduled appointment. All these actions trigger automatic notifications in case of cancellations, modifications, and a reminder 24 hours before an appointment, as well as notifications regarding the patient's presence or absence.

## Installation

### Cloning the Project
To clone the project from GitHub, open a terminal and run the following command:

```bash
git clone https://github.com/Iasmina012/DentalHub
```

### Opening the Project in Android Studio

- Open Android Studio;
- Select "File > Open an existing Android Studio project";
- Navigate to the directory where the project was cloned;
- After opening the project, Android Studio will prompt you to sync it with Gradle. Click on "Sync Now" or go to "File > Sync Project with Gradle Files".

### Configuring Firebase
- Create your own Firebase project here: https://console.firebase.google.com by following the steps provided;
- Ensure that the project is correctly connected to Firebase (check the steps in Android Studio under "Tools > Firebase");
- Download the "google-services.json" file from your Firebase project's settings;
- Add the "google-services.json" file to the root directory of the application;
- Add the necessary Firebase dependencies in the "build.gradle" file if required;
- Replace every occurrence of the link "https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app" in the code with the one from your Firebase database.

## Compilation
Use "Build > Make Project" to compile the application.

## Running the Application
The application can be run on a personal Android device or by creating an emulator in Android Studio:

1. _Android Device_
- On your device, go to "Settings > About phone > Software information > More" and tap "Build number" seven times to enable Developer mode;
- On your device, go to "Settings > Developer options" and enable "USB Debugging";
- Connect your device to your computer/laptop using a USB cable;
- Select your connected device from the list of available devices;
- In Android Studio, click on the "Run" button.

2. _Android Emulator_
- In Android Studio, navigate to "Tools > AVD Manager";
- Click on "Create Virtual Device";
- Choose a device from the list of available devices (e.g., Pixel 4) and click "Next";
- Select an Android version for the emulator, preferably one of the latest versions (> Android 11);
- If the system image is not already downloaded, click "Download" to install it;
- Customize the emulator settings if necessary (RAM, storage space, etc.);
- Click "Finish" to create the emulator;
- Select the emulator as the primary device;
- Click "Run," and the application will be automatically installed and launched on the emulator.

## Launch
Once the application is installed on your device, follow these steps:

- On your Android device, locate the application icon;
- Tap on the application icon to launch it;
- You can now start using the app and all its features.

## Minimum System Requirements

### Windows:
- Windows 8/10/11 (64-bit)
- 4 GB RAM minimum (8 GB RAM recommended)
- 2 GB free disk space minimum (4 GB recommended)
- Minimum screen resolution of 1280 x 800
- Processor with virtualization support
- GPU: OpenGL ES 2.0 compatible GPU

### macOS:
- macOS 10.14 Mojave or later
- 4 GB RAM minimum (8 GB RAM recommended)
- 2 GB free disk space minimum (4 GB recommended)
- Minimum screen resolution of 1280 x 800
- Processor with virtualization support
- GPU: OpenGL ES 2.0 compatible GPU

### Linux:
- A recent 64-bit Linux distribution, such as Ubuntu 18.04+
- GNU C Library (glibc) 2.31 or later
- 4 GB RAM minimum (8 GB RAM recommended)
- 2 GB free disk space minimum (4 GB recommended)
- Minimum screen resolution of 1280 x 800
- Processor with virtualization support
- GPU: OpenGL ES 2.0 compatible GPU

### Java Development Kit (JDK)
- JDK 8 or later (JDK 11 recommended)

### Android Studio
- Android Studio version 8.0 or later
- Android SDK 26 or later

---

_This application was developed on macOS Ventura 13.5.1 in Android Studio Iguana 2023.2.1 Patch 2 with JDK 17.0.2._


