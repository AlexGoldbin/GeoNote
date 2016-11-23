/**
 * Created by Goldbin on 10.10.2016.
 */
package comgoldbin.vk.geonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class RepoBDHelper {

    // Данные базы данных и таблиц
    private static final String DATABASE_NAME = "geoNotesDb.db";
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_NAME = "GeoData";

    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_PHOTO = "photo";
    private static final String COLUMN_GEOLATI = "geolat";
    private static final String COLUMN_GEOLONGI = "geolong";

    // Номера столбцов
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_DATE = 1;
    private static final int NUM_COLUMN_TEXT = 2;
    private static final int NUM_COLUMN_PHOTO = 3;
    private static final int NUM_COLUMN_GEOLATI = 4;
    private static final int NUM_COLUMN_GEOLONGI = 5;

    private SQLiteDatabase mDataBase;


        // открываем (или создаем и открываем) БД для записи и чтения
    public RepoBDHelper(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_TEXT + " TEXT, " +
                    COLUMN_PHOTO + " TEXT, " +
                    COLUMN_GEOLATI + " INTEGER, " +
                    COLUMN_GEOLONGI + " INTEGER);  ";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public long insert(DBData md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_DATE, md.getDate());
        cv.put(COLUMN_TEXT, md.getText());
        cv.put(COLUMN_PHOTO, md.getPhoto());
        cv.put(COLUMN_GEOLATI, md.getLat());
        cv.put(COLUMN_GEOLONGI, md.getLon());
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

   public int update(DBData md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_DATE, md.getDate());
        cv.put(COLUMN_TEXT, md.getText());
       cv.put(COLUMN_GEOLATI, md.getLat());
       cv.put(COLUMN_GEOLONGI, md.getLon());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(md.getID()) });
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public  DBData  select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, COLUMN_DATE);

        mCursor.moveToFirst();
        String date = mCursor.getString(NUM_COLUMN_DATE);
        String text = mCursor.getString(NUM_COLUMN_TEXT);
        String photo = mCursor.getString(NUM_COLUMN_PHOTO);
        double geolat = Double.parseDouble(mCursor.getString(NUM_COLUMN_GEOLATI));
        double geolon = Double.parseDouble(mCursor.getString(NUM_COLUMN_GEOLONGI));
        return new DBData(id,date,text,photo,geolat,geolon);
    }

    public ArrayList<DBData> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<DBData> datenotes = new ArrayList<DBData>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String date = mCursor.getString(NUM_COLUMN_DATE);
                String text = mCursor.getString(NUM_COLUMN_TEXT);
                String photo = mCursor.getString(NUM_COLUMN_PHOTO);
                double geolat = Double.parseDouble(mCursor.getString(NUM_COLUMN_GEOLATI));
                double geolon = Double.parseDouble(mCursor.getString(NUM_COLUMN_GEOLONGI));
                datenotes.add(new DBData(id,date,text,photo,geolat,geolon));
            } while (mCursor.moveToNext());
        }
        return datenotes;
    }
}