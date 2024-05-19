package com.upt.cti.dentalhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import timber.log.Timber;

public class DatabaseInitializer {

    private DatabaseHelper dbHelper;

    public DatabaseInitializer(Context context) { dbHelper = new DatabaseHelper(context); }

    public void insertInitialData() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //dbHelper.clearDatabase();

        try {
            //check if data already exists
            if (isDatabaseEmpty(db)) {

                //symptoms
                long toothacheId = insertSymptom(db, "Toothache");
                long looseTeethId = insertSymptom(db, "Loose teeth");
                long crackedTeethId = insertSymptom(db, "Cracked teeth");
                long toothDiscolorationId = insertSymptom(db, "Teeth discoloration");
                long swollenGumsId = insertSymptom(db, "Swollen gums");
                long bleedingGumsId = insertSymptom(db, "Bleeding gums");
                long badBreathId = insertSymptom(db, "Bad breath");
                long badTasteId = insertSymptom(db, "Bad taste");
                long hotSensitivityId = insertSymptom(db, "Hot sensitivity");
                long coldSensitivityId = insertSymptom(db, "Cold sensitivity");
                long sweetSensitivityId = insertSymptom(db, "Sweet sensitivity");
                long jawPainId = insertSymptom(db, "Jaw pain");
                long recedingGumsId = insertSymptom(db, "Receding gums");
                long mouthSoresId = insertSymptom(db, "Mouth sores");
                long dryMouthId = insertSymptom(db, "Dry mouth");

                //diseases
                long cariesId = insertDisease(db, "Dental Caries");
                long gingivitisId = insertDisease(db, "Gingivitis");
                long periodontitisId = insertDisease(db, "Periodontitis");
                long periodontalDiseaseId = insertDisease(db, "Periodontal Disease");
                long abscessId = insertDisease(db, "Tooth Abscess");
                long bruxismId = insertDisease(db, "Bruxism");
                long oralThrushId = insertDisease(db, "Oral Thrush");
                long leukoplakiaId = insertDisease(db, "Leukoplakia");
                long halitosisId = insertDisease(db, "Halitosis");

                //disease-symptoms
                insertDiseaseSymptom(db, cariesId, toothacheId);
                insertDiseaseSymptom(db, cariesId, toothDiscolorationId);
                insertDiseaseSymptom(db, cariesId, hotSensitivityId);
                insertDiseaseSymptom(db, cariesId, coldSensitivityId);
                insertDiseaseSymptom(db, cariesId, sweetSensitivityId);
                insertDiseaseSymptom(db, gingivitisId, swollenGumsId);
                insertDiseaseSymptom(db, gingivitisId, bleedingGumsId);
                insertDiseaseSymptom(db, periodontitisId, jawPainId);
                insertDiseaseSymptom(db, periodontitisId, looseTeethId);
                insertDiseaseSymptom(db, periodontitisId, recedingGumsId);
                insertDiseaseSymptom(db, periodontalDiseaseId, badBreathId);
                insertDiseaseSymptom(db, periodontalDiseaseId, swollenGumsId);
                insertDiseaseSymptom(db, abscessId, toothacheId);
                insertDiseaseSymptom(db, abscessId, jawPainId);
                insertDiseaseSymptom(db, bruxismId, jawPainId);
                insertDiseaseSymptom(db, bruxismId, crackedTeethId);
                insertDiseaseSymptom(db, oralThrushId, mouthSoresId);
                insertDiseaseSymptom(db, oralThrushId, dryMouthId);
                insertDiseaseSymptom(db, leukoplakiaId, mouthSoresId);
                insertDiseaseSymptom(db, leukoplakiaId, dryMouthId);
                insertDiseaseSymptom(db, halitosisId, badBreathId);
                insertDiseaseSymptom(db, halitosisId, badTasteId);

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

    private long insertSymptom(SQLiteDatabase db, String name) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SYMPTOM_NAME, name);
        return db.insert(DatabaseHelper.TABLE_SYMPTOMS, null, values);

    }

    private long insertDisease(SQLiteDatabase db, String name) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DISEASE_NAME, name);
        return db.insert(DatabaseHelper.TABLE_DISEASES, null, values);

    }

    private void insertDiseaseSymptom(SQLiteDatabase db, long diseaseId, long symptomId) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DISEASE_ID, diseaseId);
        values.put(DatabaseHelper.COLUMN_SYMPTOM_ID, symptomId);
        db.insert(DatabaseHelper.TABLE_DISEASE_SYMPTOMS, null, values);

    }

}