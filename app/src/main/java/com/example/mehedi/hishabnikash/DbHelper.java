package com.example.mehedi.hishabnikash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hishabNikash";

    private static final String TBL_SAVINGS_PLAN = "savings_plan";
    private static final String TBL_OTHERS_COST = "others_cost";
    private static final String TBL_TONG_DOKAN = "tong_dokan";
    private static final String TBL_VEHICLE_COST = "vehicle_cost";

    private static final String ID_SAVINGS_PLAN = "_id";
    private static final String ID_OTHERS_COST  = "_id";
    private static final String ID_TONG_DOKAN   = "_id";
    private static final String ID_VEHICLE_COST = "_id";

    private static final String CREATE_TBL_SAVINGS_PLAN= "CREATE TABLE "+TBL_SAVINGS_PLAN+"(\n" +
            "   _ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "   EXPECTED_AMOUNT INT NOT NULL,\n" +
            "   ACTUAL_AMOUNT INT NOT NULL,\n" +
            "   MONTH INT,\n" +
            "   YEAR INT\n" +
            ");";

    private static final String CREATE_TBL_OTHERS_COST = "CREATE TABLE "+TBL_OTHERS_COST+"(\n" +
            "   _ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "   PURPOSE VARCHAR(255) NOT NULL,\n" +
            "   AMOUNT INT NOT NULL,\n" +
            "   DATE INT,\n" +
            "   MONTH INT,\n" +
            "   YEAR INT\n" +
            ");";

    private static final String CREATE_TBL_TONG_DOKAN = "CREATE TABLE "+TBL_TONG_DOKAN+"(\n" +
            "   _ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "   AMOUNT INT NOT NULL,\n" +
            "   DATE INT,\n" +
            "   MONTH INT,\n" +
            "   YEAR INT\n" +
            ");";

    private static final String CREATE_TBL_VEHICLE_COST = "CREATE TABLE "+TBL_VEHICLE_COST+"(\n" +
            "   _ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "   START_POINT VARCHAR(100) NOT NULL,\n" +
            "   DESTINATION VARCHAR(100) NOT NULL,\n" +
            "   VEHICLE_TYPE VARCHAR(100) NOT NULL,\n" +
            "   AMOUNT INT NOT NULL,\n" +
            "   DATE INT,\n" +
            "   MONTH INT,\n" +
            "   YEAR INT\n" +
            ");";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TBL_SAVINGS_PLAN);
        sqLiteDatabase.execSQL(CREATE_TBL_OTHERS_COST);
        sqLiteDatabase.execSQL(CREATE_TBL_TONG_DOKAN);
        sqLiteDatabase.execSQL(CREATE_TBL_VEHICLE_COST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_TONG_DOKAN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_OTHERS_COST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_SAVINGS_PLAN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_VEHICLE_COST);

        onCreate(sqLiteDatabase);
    }

    public long addSavingsPlan (int amount,int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("EXPECTED_AMOUNT",amount);
        contentValues.put("ACTUAL_AMOUNT",0);
        contentValues.put("MONTH",month);
        contentValues.put("YEAR",year);

        long insertedID = db.insert(TBL_SAVINGS_PLAN, null,contentValues);
        return insertedID;
    }

    public Cursor checkSavingsPlan (int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TBL_SAVINGS_PLAN,null,"MONTH = ? AND YEAR = ?",new String[]{month+"",year+""},null,null,null);
        return cursor;
    }

    public Cursor getAllSavingsPlan () {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TBL_SAVINGS_PLAN, null, null,null,null,null, "_ID DESC");
        return cursor;
    }

    public void updateSavingsPlan (int amount, int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXPECTED_AMOUNT", amount);

        db.update(TBL_SAVINGS_PLAN, contentValues, "MONTH = ? AND YEAR = ?", new String[] {month+"", year+""});
    }

    public long addOtherCost (OtherCostHolder otherCostHolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PURPOSE", otherCostHolder.getPurpose());
        contentValues.put("AMOUNT", otherCostHolder.getAmount());
        contentValues.put("DATE", otherCostHolder.getDate());
        contentValues.put("MONTH", otherCostHolder.getMonth());
        contentValues.put("YEAR", otherCostHolder.getYear());

        long id = db.insert(TBL_OTHERS_COST, null, contentValues);
        return id;
    }

    public ArrayList<OtherCostHolder> getAllOthersCost() {
        ArrayList<OtherCostHolder> costList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TBL_OTHERS_COST, null, null,null,null,null, "_ID DESC");
        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_ID")));
            String purpose = cursor.getString(cursor.getColumnIndex("PURPOSE"));
            int amount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("AMOUNT")));
            int date = Integer.parseInt(cursor.getString(cursor.getColumnIndex("DATE")));
            int month = Integer.parseInt(cursor.getString(cursor.getColumnIndex("MONTH")));
            int year = Integer.parseInt(cursor.getString(cursor.getColumnIndex("YEAR")));

            costList.add(new OtherCostHolder(id,purpose,amount,date,month,year));
        }

        return costList;
    }

}
