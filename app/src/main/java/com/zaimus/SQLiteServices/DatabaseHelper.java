package com.zaimus.SQLiteServices;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.vkcrestore/databases/";

    private static String DB_NAME = "zaimussurvey.sql";

    private SQLiteDatabase myDataBase;

    private final Context myContext;
    String myPath;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        ////System.out.println("Database  exists boolean outside ---------->" + dbExist);
        if (dbExist) {

            ////System.out.println("Database  exists boolean ---------->" + dbExist);
            // do nothing - database already exist
        } else {
            ////Log.v("Need to create", "Need to create");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /*
     * private boolean checkDataBase(){
     *
     * SQLiteDatabase checkDB = null;
     *
     * try{ String myPath = DB_PATH + DB_NAME; checkDB =
     * SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     * //Log.v("DB Exists","DB Exists"); }catch(SQLiteException e){
     *
     * //Log.v("Database Not Exist","Database Not Exist");
     *
     * }
     *
     * if(checkDB != null){ checkDB.close(); }
     *
     * return checkDB != null ? true : false; }
     */
    private boolean checkDataBase() {
        boolean checkdb = false;
        try {
            /*String myPath = myContext.getFilesDir().getAbsolutePath()
                    .replace("files", "databases")
					+ File.separator + DB_NAME;*/
            File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }
            /*File mydir = myContext.getDir("VKCSurvey", Context.MODE_PRIVATE); //Creating an internal dir;
            if (!mydir.exists()) {
                mydir.mkdirs();
            }*/
            myPath = mydir.getAbsolutePath() + "/" + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
            ////System.out.println("Database  exists---------->" + myPath);

        } catch (SQLiteException e) {
            ////System.out.println("Database doesn't exist");
        }

        return checkdb;
    }

    private void copyDataBase() throws IOException {
        // //System.out.println("copyDataBase");
        try {
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            String outFileName = myPath;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];//1024
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception e) {

        }
    }

    public void openDataBase() throws SQLException {
        // Open the database

        File dbFile = myContext.getDatabasePath(DB_NAME);
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null,
                SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // //System.out.println("Oncreate Database----------->");
        //String s = "CREATE TABLE IF NOT EXISTS survey_details(id  INTEGER PRIMARY KEY AUTOINCREMENT,surveyset_id INTEGER,surveyset_name TEXT NOT NULL,state_id INTEGER,state_name TEXT NOT NULL)";
        //db.execSQL(s);
    }

    /*public void addSurveyDetails(Survey survey) {
        //System.out.println("addSurveyDetails Database----------->");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("surveyset_id", survey.survey_id);
        values.put("surveyset_name", survey.survey_name);
        values.put("state_id", survey.state_id);
        values.put("state_name", survey.state_name);

        db.insert("survey_details", null, values);

        db.close();
    }
*/
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
