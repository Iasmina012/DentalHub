package com.upt.cti.dentalhub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dentalhub.db";
    private static final int DATABASE_VERSION = 1;

    //table names
    public static final String TABLE_SYMPTOMS = "symptoms";
    public static final String TABLE_DISEASES = "diseases";
    public static final String TABLE_DISEASE_SYMPTOMS = "disease_symptoms";

    //common column names
    public static final String COLUMN_ID = "_id";

    //symptoms table: column names
    public static final String COLUMN_SYMPTOM_NAME = "name";

    //diseases table: column names
    public static final String COLUMN_DISEASE_NAME = "name";

    //disease-symptoms table: column names
    public static final String COLUMN_DISEASE_ID = "disease_id";
    public static final String COLUMN_SYMPTOM_ID = "symptom_id";

    //create tables
    private static final String CREATE_TABLE_SYMPTOMS = "CREATE TABLE "
            + TABLE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SYMPTOM_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASES = "CREATE TABLE "
            + TABLE_DISEASES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DISEASE_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASE_SYMPTOMS = "CREATE TABLE "
            + TABLE_DISEASE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DISEASE_ID + " INTEGER," + COLUMN_SYMPTOM_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_DISEASE_ID + ") REFERENCES " + TABLE_DISEASES + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_SYMPTOM_ID + ") REFERENCES " + TABLE_SYMPTOMS + "(" + COLUMN_ID + "))";

    public DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_SYMPTOMS);
        db.execSQL(CREATE_TABLE_DISEASES);
        db.execSQL(CREATE_TABLE_DISEASE_SYMPTOMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYMPTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASE_SYMPTOMS);
        onCreate(db);

    }

    public void clearDatabase() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DISEASE_SYMPTOMS);
        db.execSQL("DELETE FROM " + TABLE_DISEASES);
        db.execSQL("DELETE FROM " + TABLE_SYMPTOMS);
        db.close();

    }

}