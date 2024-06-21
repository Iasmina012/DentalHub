package com.upt.cti.dentalhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import timber.log.Timber;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseInitializer {

    private DatabaseHelper dbHelper;

    public DatabaseInitializer(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertInitialData() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //dbHelper.clearDatabase();

        try {
            if (isDatabaseEmpty(db)) {
                insertSymptoms(db);
                insertDiseases(db);
                insertDiseaseSymptoms(db);
                insertDevices(db);
                insertTips(db);
                insertLocations(db);
                insertDoctors(db);
                insertServices(db);
                insertServiceSpecializations(db);
                insertInsurances(db);
            }
        } catch (Exception e) {
            Timber.e(e, "Error initializing database");
        } finally {
            db.close();
        }

    }

    private boolean isDatabaseEmpty(SQLiteDatabase db) {

        boolean isEmpty = true;
        Cursor cursor = null;

        try {
            cursor = db.query(DatabaseHelper.TABLE_SYMPTOMS, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                isEmpty = false;
            }
        } catch (Exception e) {
            Timber.e(e, "Error checking if database is empty");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isEmpty;

    }

    private void insertSymptoms(SQLiteDatabase db) {

        String[] symptoms = {
                "Toothache", "Loose teeth", "Cracked teeth", "Teeth discoloration", "Swollen gums",
                "Bleeding gums", "Bad breath", "Bad taste", "Hot sensitivity", "Cold sensitivity",
                "Sweet sensitivity", "Jaw pain", "Receding gums", "Mouth sores", "Dry mouth"
        };

        for (String symptom : symptoms) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_SYMPTOM_NAME, symptom);
            db.insert(DatabaseHelper.TABLE_SYMPTOMS, null, values);
        }

    }

    private void insertDiseases(SQLiteDatabase db) {

        String[] diseases = {
                "Dental Caries", "Gingivitis", "Periodontitis", "Periodontal Disease",
                "Tooth Abscess", "Bruxism", "Oral Thrush", "Leukoplakia", "Halitosis"
        };

        for (String disease : diseases) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DISEASE_NAME, disease);
            db.insert(DatabaseHelper.TABLE_DISEASES, null, values);
        }

    }

    private void insertDiseaseSymptoms(SQLiteDatabase db) {

        long[][] diseaseSymptoms = {
                {1, 1}, {1, 4}, {1, 9}, {1, 10}, {1, 11},
                {2, 5}, {2, 6},
                {3, 12}, {3, 2}, {3, 13},
                {4, 7}, {4, 5},
                {5, 1}, {5, 12},
                {6, 12}, {6, 3},
                {7, 14}, {7, 15},
                {8, 14}, {8, 15},
                {9, 7}, {9, 8}
        };

        for (long[] ds : diseaseSymptoms) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DISEASE_ID, ds[0]);
            values.put(DatabaseHelper.COLUMN_SYMPTOM_ID, ds[1]);
            db.insert(DatabaseHelper.TABLE_DISEASE_SYMPTOMS, null, values);
        }

    }

    private void insertDevices(SQLiteDatabase db) {

        String[][] devices = {
                {"Centrifuge PRF Duo", "Prepares membranes using the patient's blood to enhance healing and reduce graft rejection.", String.valueOf(R.drawable.device01)},
                {"Pentamix Lite", "Automatic mixing unit for even distribution of dental impression materials.", String.valueOf(R.drawable.device02)},
                {"Valo Ortho Cordless", "LED lamp for orthodontic photopolymerization, ensuring consistent polymerization on brackets.", String.valueOf(R.drawable.device03)},
                {"Surgic XT Plus", "LED physiodispenser for use in implantology and oral surgery.", String.valueOf(R.drawable.device04)},
                {"Ivoclar Bluephase Photo Curing Light", "Dual LED curing light that completes polymerization in just 5 seconds.", String.valueOf(R.drawable.device05)},
                {"Silver Mix", "Uniformizes dental material capsules via controlled agitation.", String.valueOf(R.drawable.device06)},
                {"Laser BTL10", "Features 10 preset programs for various medical and dental recovery conditions.", String.valueOf(R.drawable.device07)},
                {"Apex Locator", "Ensures maximum precision for root canal treatment.", String.valueOf(R.drawable.device08)},
                {"Canon 70D", "High-quality dental photography with advanced Canon technologies.", String.valueOf(R.drawable.device09)},
                {"Irix 70", "Radiology device for assessing dental health and root morphology.", String.valueOf(R.drawable.device10)},
                {"Zoom Lamp", "Dental aesthetic tool for effective teeth whitening.", String.valueOf(R.drawable.device11)},
                {"Vita Easy Shade", "Determines tooth color by measuring brightness, transparency, and saturation.", String.valueOf(R.drawable.device12)},
                {"Hurimix", "Ensures perfect homogenization of alginate for dental models.", String.valueOf(R.drawable.device13)},
                {"Satelec Piezotome 2 Surgery", "Combines Piezotome for microsurgery and Newtron for endodontic treatments.", String.valueOf(R.drawable.device14)},
                {"Autoclave Euronda E9 MED", "Sterilizes medical equipment using high pressure and controlled temperature.", String.valueOf(R.drawable.device15)},
                {"Euronda Sealing Device", "Seals dental equipment to maintain sterilization and prevent contamination.", String.valueOf(R.drawable.device16)}
        };

        for (String[] device : devices) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DEVICE_NAME, device[0]);
            values.put(DatabaseHelper.COLUMN_DEVICE_DESCRIPTION, device[1]);
            values.put(DatabaseHelper.COLUMN_DEVICE_IMAGE, Integer.parseInt(device[2]));
            db.insert(DatabaseHelper.TABLE_DEVICES, null, values);
        }

    }

    private void insertTips(SQLiteDatabase db) {

        String[][] tips = {
                {"Checkup", "Regular dental checkups are vital for maintaining healthy teeth and gums. Experts recommend visiting the dentist at least once every six months for a routine examination and cleaning. During the visit, your dentist will check for cavities, gum disease, and other oral health issues. They will also provide professional cleaning to remove plaque and tartar buildup. If you experience pain, sensitivity, or other changes in your oral health, schedule an appointment immediately. Early detection and treatment of dental problems can save your teeth and ensure your mouth stays healthy.", String.valueOf(R.drawable.check_up)},
                {"Brushing", "To maintain good oral hygiene, brushing your teeth correctly is essential. Start by choosing a soft-bristled toothbrush and fluoride toothpaste. Hold your brush at a 45-degree angle to your gums and use gentle circular motions to clean each tooth thoroughly. Brush for at least two minutes, covering all surfaces: the outside, inside, and chewing areas of each tooth. Don’t forget to brush your tongue to remove bacteria and freshen your breath. For optimal dental health, brush your teeth twice a day, in the morning and before bedtime.", String.valueOf(R.drawable.brushing)},
                {"Flossing", "Using dental floss is crucial for removing plaque and food particles from between your teeth, where a toothbrush can't reach. Cut about 18 inches of floss and wind most of it around one of your middle fingers, with the rest around the opposite middle finger. Hold the floss tightly between your thumbs and index fingers and gently slide it up and down between your teeth. Curve the floss around the base of each tooth, making sure you go beneath the gumline. Never snap or force the floss, as this may cut or bruise delicate gum tissue. Use clean sections of floss as you move from tooth to tooth.", String.valueOf(R.drawable.flossing)},
                {"Rinsing", "Rinsing your mouth properly is a key step in your dental care routine. Use an antimicrobial or fluoride mouthwash to enhance oral health. Pour the recommended amount of mouthwash into a cup, empty it into your mouth, and swish vigorously for 30 seconds. Make sure the liquid moves throughout your mouth and between your teeth. Spit out the mouthwash; do not swallow it. For best results, avoid eating or drinking for 30 minutes after using mouthwash so the active ingredients can work effectively.", String.valueOf(R.drawable.rinsing)}
        };

        for (String[] tip : tips) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TIP_NAME, tip[0]);
            values.put(DatabaseHelper.COLUMN_TIP_DESCRIPTION, tip[1]);
            values.put(DatabaseHelper.COLUMN_TIP_IMAGE, Integer.parseInt(tip[2]));
            db.insert(DatabaseHelper.TABLE_TIPS, null, values);
        }

    }

    private void insertLocations(SQLiteDatabase db) {

        String[][] locations = {
                { "1425 Broadway Suite 22\nNew York, NY 10018\nClinic 1", "+40 (256) 867 346", "head@office.com"},
                { "58 Wall Street Suite 100\nNew York, NY 10005\nClinic 2", "+40 (758) 364 567", "office@office.com"}
        };

        for (String[] location : locations) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_LOCATION_ADDRESS, location[0]);
            values.put(DatabaseHelper.COLUMN_LOCATION_PHONE, location[1]);
            values.put(DatabaseHelper.COLUMN_LOCATION_EMAIL, location[2]);
            db.insert(DatabaseHelper.TABLE_LOCATIONS, null, values);
        }

    }

    private String hashPassword(String plainTextPassword) { return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt()); }

    private void insertDoctors(SQLiteDatabase db) {

        long[] locationIds = {1, 2};
        String[] names = {"Dr. Daniela Pop", "Dr. Ana Maria Popescu", "Dr. Andrei Radu", "Dr. Maria Ionescu", "Dr. Elena Popa", "Any Doctor"};
        int[] images = {R.drawable.doctor01, R.drawable.doctor02, R.drawable.doctor03, R.drawable.doctor04, R.drawable.doctor05, R.drawable.doctors};
        String[] specializations = {"General Dentist", "Orthodontist", "Implantologist", "Pedodontist", "Prosthodontist", "Any Specialization"};
        String[] phone_numbers = {"0721122334", "0723456789", "0734567890", "0745678901", "0756789012", "0256986274"};
        String[] emails = {"daniela@gmail.com", "ana@yahoo.com", "andrei@gmail.com", "maria@yahoo.com", "elena@gmail.com", "doctors@gmail.com"};
        String password = "Doctor123#";

        for (int i = 0; i < names.length; i++) {
            long doctorId = insertDoctor(db, names[i], images[i], specializations[i], phone_numbers[i], emails[i], password);
            insertDoctorSchedules(db, doctorId, i);
            if (i == 5) { //"Any Doctor"
                for (long locationId : locationIds) {
                    insertDoctorLocation(db, doctorId, locationId);
                }
            } else {
                insertDoctorLocation(db, doctorId, locationIds[i % 2]);
            }
        }
    }

    private long insertDoctor(SQLiteDatabase db, String name, int image, String specialization, String phone_number, String email, String password) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DOCTOR_NAME, name);
        values.put(DatabaseHelper.COLUMN_DOCTOR_IMAGE, image);
        values.put(DatabaseHelper.COLUMN_DOCTOR_SPECIALIZATION, specialization);
        values.put(DatabaseHelper.COLUMN_DOCTOR_PHONE_NUMBER, phone_number);
        values.put(DatabaseHelper.COLUMN_DOCTOR_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_DOCTOR_PASSWORD, hashPassword("Doctor123#"));
        return db.insert(DatabaseHelper.TABLE_DOCTORS, null, values);

    }

    private void insertDoctorSchedules(SQLiteDatabase db, long doctorId, int doctorIndex) {

        String[][] schedules = {
                {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"},
                {"Monday", "Wednesday", "Friday"},
                {"Tuesday", "Thursday"},
                {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"},
                {"Monday", "Wednesday", "Friday"},
                {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}
        };

        String[][] startTimes = {
                {"08:00", "08:00", "08:00", "08:00", "08:00"},
                {"14:00", "14:00", "14:00"},
                {"10:00", "10:00"},
                {"12:00", "12:00", "12:00", "12:00", "12:00"},
                {"08:00", "08:00", "08:00"},
                {"08:00", "08:00", "08:00", "08:00", "08:00", "08:00", "08:00"}
        };

        String[][] endTimes = {
                {"16:00", "16:00", "16:00", "16:00", "16:00"},
                {"18:00", "18:00", "18:00"},
                {"14:00", "14:00"},
                {"15:00", "15:00", "15:00", "15:00", "15:00"},
                {"12:00", "12:00", "12:00"},
                {"22:00", "22:00", "22:00", "22:00", "22:00", "22:00", "22:00"}
        };

        for (int i = 0; i < schedules[doctorIndex].length; i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DOCTOR_ID, doctorId);
            values.put(DatabaseHelper.COLUMN_DAY_OF_WEEK, schedules[doctorIndex][i]);
            values.put(DatabaseHelper.COLUMN_START_TIME, startTimes[doctorIndex][i]);
            values.put(DatabaseHelper.COLUMN_END_TIME, endTimes[doctorIndex][i]);
            db.insert(DatabaseHelper.TABLE_DOCTOR_SCHEDULE, null, values);
        }

    }

    private void insertDoctorLocation(SQLiteDatabase db, long doctorId, long locationId) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DOCTOR_ID, doctorId);
        values.put(DatabaseHelper.COLUMN_LOCATION_ID, locationId);
        db.insert(DatabaseHelper.TABLE_DOCTOR_LOCATION, null, values);

    }

    private void insertServices(SQLiteDatabase db) {

        String[][] services = {
                {"SPECIALIZED CONSULTATION", "Surgery consultation\nPeriodontology consultation\nOrthodontics and orthopedics consultation\nImplantology consultation", String.valueOf(R.drawable.services01)},
                {"DENTAL PROPHYLAXIS", "Scaling, air-flow, professional brushing\nSealing of temporary and permanent teeth", String.valueOf(R.drawable.services02)},
                {"DENTAL AESTHETICS", "Teeth whitening\nDirect and indirect dental veneers\nAesthetic light-curing fillings\nCeramic crowns", String.valueOf(R.drawable.services03)},
                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Myofunctional appliances\nRemovable and mobilizable appliances\nFixed dental appliances: metallic, aesthetic", String.valueOf(R.drawable.services04)},
                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Child prophylaxis\nSealing of grooves and pits in primary teeth\nFillings for primary teeth\nEndodontic treatment of primary teeth\nExtraction of primary teeth", String.valueOf(R.drawable.services05)},
                {"ODONTOTHERAPY", "Pulp capping\nAesthetic fillings\nDirect veneers", String.valueOf(R.drawable.services06)},
                {"ENDODONTICS", "Specific treatments application\nRoot canal treatments\nRoot fillings\nTransdental drainage", String.valueOf(R.drawable.services07)},
                {"PERIODONTOLOGY", "Removal of subgingival plaque and tartar\nPeri-implant curettage\nSRP - surface root planing\nSampling for testing pathogens (Periodontitis - Peri-implantitis - Pathogens)", String.valueOf(R.drawable.services08)},
                {"DENTAL PROSTHETICS", "Removal of old prosthetic works\nRemovable dentures (telescopic and skeletonized)\nDental crowns or bridges: Metal-ceramic, All-ceramic, Zirconia, Emax\nIndirect fillings - Inlay and Onlay\nCeramic veneers (Veneers)", String.valueOf(R.drawable.services09)},
                {"DENTAL SURGERY", "Tooth extractions\nSinus lift (elevation of the sinus membrane)\nBone crest remodeling (bone addition + membrane)\nApical resections\nWisdom tooth odontectomy/Impacted tooth exposure\nFrenectomy and frenoplasty\nDrainage with a drainage blade", String.valueOf(R.drawable.services10)},
                {"IMPLANTOLOGY", "Alpha Bio dental implants\nImplantium dental implants\nNobel Biocare dental implants", String.valueOf(R.drawable.services11)},
                {"DENTAL RADIOLOGY", "Retroalveolar dental X-ray", String.valueOf(R.drawable.services12)}
        };

        for (String[] service : services) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_SERVICE_NAME, service[0]);
            values.put(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION, service[1]);
            values.put(DatabaseHelper.COLUMN_SERVICE_IMAGE, Integer.parseInt(service[2]));
            db.insert(DatabaseHelper.TABLE_SERVICES, null, values);
        }

    }

    private void insertServiceSpecializations(SQLiteDatabase db) {

        String[][] serviceSpecializations = {
                {"SPECIALIZED CONSULTATION", "Any Specialization"},
                {"SPECIALIZED CONSULTATION", "General Dentist"},
                {"SPECIALIZED CONSULTATION", "Aesthetic Dentist"},
                {"SPECIALIZED CONSULTATION", "Orthodontist"},
                {"SPECIALIZED CONSULTATION", "Pedodontist"},
                {"SPECIALIZED CONSULTATION", "Endodontist"},
                {"SPECIALIZED CONSULTATION", "Periodontologist"},
                {"SPECIALIZED CONSULTATION", "Prosthodontist"},
                {"SPECIALIZED CONSULTATION", "Surgeon"},
                {"SPECIALIZED CONSULTATION", "Implantologist"},
                {"SPECIALIZED CONSULTATION", "Odontotherapist"},
                {"SPECIALIZED CONSULTATION", "Radiologist"},

                {"DENTAL PROPHYLAXIS", "Any Specialization"},
                {"DENTAL PROPHYLAXIS", "General Dentist"},
                {"DENTAL PROPHYLAXIS", "Aesthetic Dentist"},
                {"DENTAL PROPHYLAXIS", "Orthodontist"},
                {"DENTAL PROPHYLAXIS", "Pedodontist"},
                {"DENTAL PROPHYLAXIS", "Endodontist"},
                {"DENTAL PROPHYLAXIS", "Periodontologist"},
                {"DENTAL PROPHYLAXIS", "Prosthodontist"},
                {"DENTAL PROPHYLAXIS", "Surgeon"},
                {"DENTAL PROPHYLAXIS", "Implantologist"},
                {"DENTAL PROPHYLAXIS", "Odontotherapist"},
                {"DENTAL PROPHYLAXIS", "Radiologist"},

                {"DENTAL AESTHETICS", "Any Specialization"},
                {"DENTAL AESTHETICS", "General Dentist"},
                {"DENTAL AESTHETICS", "Aesthetic Dentist"},
                {"DENTAL AESTHETICS", "Orthodontist"},
                {"DENTAL AESTHETICS", "Prosthodontist"},
                {"DENTAL AESTHETICS", "Implantologist"},

                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Any Specialization"},
                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Orthodontist"},
                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Prosthodontist"},
                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Implantologist"},
                {"ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Surgeon"},

                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Any Specialization"},
                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Pedodontist"},
                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Orthodontist"},
                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Endodontist"},
                {"PEDODONTICS (PEDIATRIC DENTISTRY)", "Odontotherapist"},

                {"ODONTOTHERAPY", "Any Specialization"},
                {"ODONTOTHERAPY", "Odontotherapist"},
                {"ODONTOTHERAPY", "Endodontist"},

                {"ENDODONTICS", "Any Specialization"},
                {"ENDODONTICS", "Endodontist"},
                {"ENDODONTICS", "Odontotherapist"},

                {"PERIODONTOLOGY", "Any Specialization"},
                {"PERIODONTOLOGY", "Periodontologist"},
                {"PERIODONTOLOGY", "Surgeon"},
                {"PERIODONTOLOGY", "Implantologist"},
                {"PERIODONTOLOGY", "Prosthodontist"},

                {"DENTAL PROSTHETICS", "Any Specialization"},
                {"DENTAL PROSTHETICS", "Prosthodontist"},
                {"DENTAL PROSTHETICS", "Periodontologist"},
                {"DENTAL PROSTHETICS", "Implantologist"},
                {"DENTAL PROSTHETICS", "Orthodontist"},

                {"DENTAL SURGERY", "Any Specialization"},
                {"DENTAL SURGERY", "Surgeon"},

                {"IMPLANTOLOGY", "Any Specialization"},
                {"IMPLANTOLOGY", "Implantologist"},
                {"IMPLANTOLOGY", "Prosthodontist"},

                {"DENTAL RADIOLOGY", "Any Specialization"},
                {"DENTAL RADIOLOGY", "Radiologist"}
        };

        for (String[] specialization : serviceSpecializations) {
            long serviceId = getServiceIdByName(db, specialization[0]);
            if (serviceId != -1) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_SERVICE_ID, serviceId);
                values.put(DatabaseHelper.COLUMN_SPECIALIZATION, specialization[1]);
                db.insert(DatabaseHelper.TABLE_SERVICE_SPECIALIZATIONS, null, values);
            }
        }

    }

    private long getServiceIdByName(SQLiteDatabase db, String serviceName) {

        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES,
                new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_SERVICE_NAME + "=?",
                new String[]{serviceName},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            long serviceId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            cursor.close();
            return serviceId;
        }
        return -1;

    }

    private void insertInsurances(SQLiteDatabase db) {

        String[] insurances = {
                "National Health Insurance Fund", "Groupama", "NN", "Medicover", "UltraMED", "Euraxess",
                "Raiffeisen", "Generali", "No Insurance"
        };

        for (String insurance : insurances) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_INSURANCE_NAME, insurance);
            db.insert(DatabaseHelper.TABLE_INSURANCES, null, values);
        }

    }

}
