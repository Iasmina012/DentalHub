package com.upt.cti.dentalhub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dentalhub.db";
    private static final int DATABASE_VERSION = 6;


    //Table names
    public static final String TABLE_SYMPTOMS = "symptoms";
    public static final String TABLE_DISEASES = "diseases";
    public static final String TABLE_DISEASE_SYMPTOMS = "disease_symptoms";
    public static final String TABLE_DEVICES = "devices";
    public static final String TABLE_TIPS = "tips";
    public static final String TABLE_LOCATIONS = "locations";
    public static final String TABLE_DOCTORS = "doctors";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_INSURANCES = "insurances";


    //Common column names
    public static final String COLUMN_ID = "_id";


    //Symptoms: column names
    public static final String COLUMN_SYMPTOM_NAME = "name";


    //Diseases: column names
    public static final String COLUMN_DISEASE_NAME = "name";


    //Disease-Symptoms: column names
    public static final String COLUMN_DISEASE_ID = "disease_id";
    public static final String COLUMN_SYMPTOM_ID = "symptom_id";


    //Devices: column names
    public static final String COLUMN_DEVICE_NAME = "name";
    public static final String COLUMN_DEVICE_DESCRIPTION = "description";


    //Tips: column names
    public static final String COLUMN_TIP_NAME = "name";
    public static final String COLUMN_TIP_DESCRIPTION = "description";
    public static final String COLUMN_TIP_IMAGE = "image";


    //Doctors: column names
    public static final String COLUMN_DOCTOR_NAME = "name";
    public static final String COLUMN_DOCTOR_IMAGE = "image";
    public static final String COLUMN_DOCTOR_SPECIALIZATION = "specialization";
    public static final String COLUMN_DOCTOR_SCHEDULE = "schedule";
    public static final String COLUMN_DOCTOR_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DOCTOR_EMAIL = "email";


    //Locations: column names
    public static final String COLUMN_LOCATION_ADDRESS = "address";


    //Services: column names
    public static final String COLUMN_SERVICE_NAME = "name";
    public static final String COLUMN_SERVICE_DESCRIPTION = "description";
    public static final String COLUMN_SERVICE_IMAGE = "image";


    //Insurances: column names
    public static final String COLUMN_INSURANCE_NAME = "name";


    //Create tables
    private static final String CREATE_TABLE_SYMPTOMS = "CREATE TABLE "
            + TABLE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SYMPTOM_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASES = "CREATE TABLE "
            + TABLE_DISEASES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DISEASE_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASE_SYMPTOMS = "CREATE TABLE "
            + TABLE_DISEASE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DISEASE_ID + " INTEGER," + COLUMN_SYMPTOM_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_DISEASE_ID + ") REFERENCES " + TABLE_DISEASES + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_SYMPTOM_ID + ") REFERENCES " + TABLE_SYMPTOMS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_DEVICES = "CREATE TABLE "
            + TABLE_DEVICES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DEVICE_NAME + " TEXT," + COLUMN_DEVICE_DESCRIPTION + " TEXT," + COLUMN_DOCTOR_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_TIPS = "CREATE TABLE "
            + TABLE_TIPS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIP_NAME + " TEXT," + COLUMN_TIP_DESCRIPTION + " TEXT,"
            + COLUMN_TIP_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE "
            + TABLE_LOCATIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LOCATION_ADDRESS + " TEXT" + ")";

    private static final String CREATE_TABLE_DOCTORS = "CREATE TABLE "
            + TABLE_DOCTORS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_NAME + " TEXT," + COLUMN_DOCTOR_IMAGE + " INTEGER,"
            + COLUMN_DOCTOR_SPECIALIZATION + " TEXT," + COLUMN_DOCTOR_SCHEDULE + " TEXT,"
            + COLUMN_DOCTOR_PHONE_NUMBER + " TEXT," + COLUMN_DOCTOR_EMAIL + " TEXT" + ")";

    private static final String CREATE_TABLE_SERVICES = "CREATE TABLE "
            + TABLE_SERVICES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SERVICE_NAME + " TEXT," + COLUMN_SERVICE_DESCRIPTION + " TEXT,"
            + COLUMN_SERVICE_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_INSURANCES = "CREATE TABLE "
            + TABLE_INSURANCES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_INSURANCE_NAME + " TEXT" + ")";

    public DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_SYMPTOMS);
        db.execSQL(CREATE_TABLE_DISEASES);
        db.execSQL(CREATE_TABLE_DISEASE_SYMPTOMS);
        db.execSQL(CREATE_TABLE_DEVICES);
        db.execSQL(CREATE_TABLE_TIPS);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_DOCTORS);
        db.execSQL(CREATE_TABLE_SERVICES);
        db.execSQL(CREATE_TABLE_INSURANCES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYMPTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASE_SYMPTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSURANCES);
        onCreate(db);

    }

    public void clearDatabase() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SYMPTOMS);
        db.execSQL("DELETE FROM " + TABLE_DISEASES);
        db.execSQL("DELETE FROM " + TABLE_DISEASE_SYMPTOMS);
        db.execSQL("DELETE FROM " + TABLE_DEVICES);
        db.execSQL("DELETE FROM " + TABLE_TIPS);
        db.execSQL("DELETE FROM " + TABLE_LOCATIONS);
        db.execSQL("DELETE FROM " + TABLE_DOCTORS);
        db.execSQL("DELETE FROM " + TABLE_SERVICES);
        db.execSQL("DELETE FROM " + TABLE_INSURANCES);
        db.close();

    }

}