package com.upt.cti.dentalhub;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //DB (Name + Version)
    private static final String DATABASE_NAME = "dentalhub.db";
    private static final int DATABASE_VERSION = 21;


    //Table names
    public static final String TABLE_SYMPTOMS = "symptoms";
    public static final String TABLE_DISEASES = "diseases";
    public static final String TABLE_DISEASE_SYMPTOMS = "disease_symptoms";
    public static final String TABLE_DEVICES = "devices";
    public static final String TABLE_TIPS = "tips";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_LOCATIONS = "locations";
    public static final String TABLE_DOCTORS = "doctors";
    public static final String TABLE_DOCTOR_SCHEDULE = "doctor_schedule";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_SERVICE_SPECIALIZATIONS = "service_specializations";
    public static final String TABLE_INSURANCES = "insurances";
    public static final String TABLE_DOCTOR_LOCATION = "doctor_location";


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
    public static final String COLUMN_DEVICE_IMAGE = "image";


    //Tips: column names
    public static final String COLUMN_TIP_NAME = "name";
    public static final String COLUMN_TIP_DESCRIPTION = "description";
    public static final String COLUMN_TIP_IMAGE = "image";


    //Appointments: column names
    public static final String COLUMN_APPOINTMENT_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";


    //Doctors: column names
    public static final String COLUMN_DOCTOR_NAME = "name";
    public static final String COLUMN_DOCTOR_IMAGE = "image";
    public static final String COLUMN_DOCTOR_SPECIALIZATION = "specialization";
    public static final String COLUMN_DOCTOR_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DOCTOR_EMAIL = "email";
    public static final String COLUMN_DOCTOR_PASSWORD = "password";


    //Doctor Schedule: column names
    public static final String COLUMN_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";


    //Doctor Location: column names
    public static final String COLUMN_LOCATION_ID = "location_id";


    //Locations: column names
    public static final String COLUMN_LOCATION_ADDRESS = "address";
    public static final String COLUMN_LOCATION_PHONE = "phone";
    public static final String COLUMN_LOCATION_EMAIL = "email";


    //Services: column names
    public static final String COLUMN_SERVICE_NAME = "name";
    public static final String COLUMN_SERVICE_DESCRIPTION = "description";
    public static final String COLUMN_SERVICE_IMAGE = "image";


    //Service-Specializations: column names
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SPECIALIZATION = "specialization";


    //Insurances: column names
    public static final String COLUMN_INSURANCE_NAME = "name";


    //Create tables
    private static final String CREATE_TABLE_SYMPTOMS = "CREATE TABLE "
            + TABLE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SYMPTOM_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASES = "CREATE TABLE "
            + TABLE_DISEASES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DISEASE_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_DISEASE_SYMPTOMS = "CREATE TABLE "
            + TABLE_DISEASE_SYMPTOMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DISEASE_ID + " INTEGER," + COLUMN_SYMPTOM_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_DISEASE_ID + ") REFERENCES " + TABLE_DISEASES + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_SYMPTOM_ID + ") REFERENCES " + TABLE_SYMPTOMS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_DEVICES = "CREATE TABLE "
            + TABLE_DEVICES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DEVICE_NAME + " TEXT," + COLUMN_DEVICE_DESCRIPTION + " TEXT,"
            + COLUMN_DEVICE_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_TIPS = "CREATE TABLE "
            + TABLE_TIPS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIP_NAME + " TEXT," + COLUMN_TIP_DESCRIPTION + " TEXT,"
            + COLUMN_TIP_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE "
            + TABLE_APPOINTMENTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_APPOINTMENT_DATE + " TEXT," + COLUMN_APPOINTMENT_TIME + " TEXT,"
            + COLUMN_APPOINTMENT_DOCTOR_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_APPOINTMENT_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE "
            + TABLE_LOCATIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LOCATION_ADDRESS + " TEXT,"
            + COLUMN_LOCATION_PHONE + " TEXT,"
            + COLUMN_LOCATION_EMAIL + " TEXT" + ")";

    private static final String CREATE_TABLE_DOCTORS = "CREATE TABLE "
            + TABLE_DOCTORS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_NAME + " TEXT," + COLUMN_DOCTOR_IMAGE + " INTEGER,"
            + COLUMN_DOCTOR_SPECIALIZATION + " TEXT,"
            + COLUMN_DOCTOR_PHONE_NUMBER + " TEXT," + COLUMN_DOCTOR_EMAIL + " TEXT,"
            + COLUMN_DOCTOR_PASSWORD + " TEXT DEFAULT 'Doctor123#'" + ")";

    private static final String CREATE_TABLE_DOCTOR_SCHEDULE = "CREATE TABLE "
            + TABLE_DOCTOR_SCHEDULE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_ID + " INTEGER," + COLUMN_DAY_OF_WEEK + " TEXT,"
            + COLUMN_START_TIME + " TEXT," + COLUMN_END_TIME + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_DOCTOR_LOCATIONS = "CREATE TABLE "
            + TABLE_DOCTOR_LOCATION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_ID + " INTEGER," + COLUMN_LOCATION_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_LOCATION_ID + ") REFERENCES " + TABLE_LOCATIONS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_SERVICES = "CREATE TABLE "
            + TABLE_SERVICES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SERVICE_NAME + " TEXT," + COLUMN_SERVICE_DESCRIPTION + " TEXT,"
            + COLUMN_SERVICE_IMAGE + " INTEGER" + ")";

    private static final String CREATE_TABLE_SERVICE_SPECIALIZATIONS = "CREATE TABLE "
            + TABLE_SERVICE_SPECIALIZATIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SERVICE_ID + " INTEGER," + COLUMN_SPECIALIZATION + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_ID + "))";

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
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_DOCTORS);
        db.execSQL(CREATE_TABLE_DOCTOR_SCHEDULE);
        db.execSQL(CREATE_TABLE_DOCTOR_LOCATIONS);
        db.execSQL(CREATE_TABLE_SERVICES);
        db.execSQL(CREATE_TABLE_SERVICE_SPECIALIZATIONS);
        db.execSQL(CREATE_TABLE_INSURANCES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYMPTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASE_SYMPTOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_SPECIALIZATIONS);
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
        db.execSQL("DELETE FROM " + TABLE_APPOINTMENTS);
        db.execSQL("DELETE FROM " + TABLE_LOCATIONS);
        db.execSQL("DELETE FROM " + TABLE_DOCTORS);
        db.execSQL("DELETE FROM " + TABLE_DOCTOR_SCHEDULE);
        db.execSQL("DELETE FROM " + TABLE_DOCTOR_LOCATION);
        db.execSQL("DELETE FROM " + TABLE_SERVICES);
        db.execSQL("DELETE FROM " + TABLE_SERVICE_SPECIALIZATIONS);
        db.execSQL("DELETE FROM " + TABLE_INSURANCES);
        db.close();

    }

    public String getDoctorSpecialization(int doctorId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String specialization = null;

        String query = "SELECT " + COLUMN_DOCTOR_SPECIALIZATION + " FROM " + TABLE_DOCTORS + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(doctorId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                specialization = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_SPECIALIZATION));
            }
            cursor.close();
        }

        db.close();
        return specialization;

    }

}