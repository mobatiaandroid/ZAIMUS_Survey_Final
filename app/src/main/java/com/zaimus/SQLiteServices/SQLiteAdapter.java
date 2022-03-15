package com.zaimus.SQLiteServices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import com.zaimus.Profiles.Profile;
import com.zaimus.Survey.Survey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter {

    private Context context;
    private static final String DB_NAME = "zaimussurvey.sql";
    private static String DB_PATH = "";

    private static final int DB_VERSION = 1;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    public static int MAX_INSERT_RECORDS;
    public static int CURRENT_RECORD;

    public SQLiteAdapter(Context c) {
        this.context = c;

    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");
        DB_PATH = mydir.getAbsolutePath() + "/" + DB_NAME;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");
        DB_PATH = mydir.getAbsolutePath() + "/" + DB_NAME;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void executeDump() {

    }

    public void excuteRawQuery(String query) throws SQLException {
        // //System.out.println("QUERY LENGTH------------->"+query);
        sqLiteDatabase.execSQL(query);
    }

    public void close() {
        // sqLiteHelper.close();rijo commented add following if
        if (sqLiteHelper != null) {
            sqLiteHelper.close();
        }
    }

    public long getCount(String DB_TABLE, String[][] constraints) {
        String constraint = "";
        SQLiteStatement s;
        for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
            if (j == constraints.length - 1 || constraints.length == 1) {
                constraint += constraints[j][0] + " = " + constraints[j][1];
            } else {
                constraint += constraints[j][0] + " = " + constraints[j][1]
                        + " AND ";
            }
        }
        if (constraint != "") {
            s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
                    + DB_TABLE + " WHERE " + constraint);
        } else {
            s = sqLiteDatabase.compileStatement("SELECT COUNT(*) FROM "
                    + DB_TABLE);
        }

        long count = s.simpleQueryForLong();
        return count;
    }

    public void update(String[][] content, String DB_TABLE,
                       String[][] constraints) {

        // String[][] cons={{"qid","2"}};
        // String[][]
        // data={{"type","1"},{"question","Term Fee"},{"option1","Op1"},{"option2","Op2"},{"option3","Op3"},{"option4","Op4"},{"option5","Op5"}};
        //
        // mySQLiteAdapter = new SQLiteAdapter(this);
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.update(data, "questions", cons);
        // mySQLiteAdapter.openToRead();
        // mySQLiteAdapter.close();

        ContentValues contentValues = new ContentValues();
        String constraint = "";
        List<String> columns = getAllColumns(DB_TABLE);
        int i = 0;
        for (String COLUMN : columns) {
            for (i = 0; i < content.length; i++) {
                if (content[i][0].equalsIgnoreCase(COLUMN)
                        && (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
                    contentValues.put(COLUMN, content[i][1]);
                    //Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
                }
            }
        }

        for (int j = 0; j < constraints.length && constraints.length > 0; j++) {
            if (j == constraints.length - 1 || constraints.length == 1) {
                constraint += constraints[j][0] + " = " + constraints[j][1];
            } else {
                constraint += constraints[j][0] + " = " + constraints[j][1]
                        + " && ";
            }
        }
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.update(DB_TABLE, contentValues, constraint, null);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.v("Error:", "Error: " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public void insert(String[][] content, String DB_TABLE) {

        // String[][]data={{"qid","5"},{"surid","3"},{"type","1"},{"_id",""},{"question","What is your name?"},{"option1","Test1"},{"option2","Test2"},{"option3","Test3"},{"option4","Test4"},{"option5","Test5"}};
        // mySQLiteAdapter = new SQLiteAdapter(this);
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.insert(data, "questions");
        // mySQLiteAdapter.openToRead(); mySQLiteAdapter.close();

        ContentValues contentValues = new ContentValues();
        List<String> columns = getAllColumns(DB_TABLE);
        int i = 0;
        for (String COLUMN : columns) {
            for (i = 0; i < content.length; i++) {
                if (content[i][0].equalsIgnoreCase(COLUMN)
                        && (!content[i][1].equalsIgnoreCase("") || content[i][1] == null)) {
                    contentValues.put(COLUMN, content[i][1]);
                    //Log.v("column" + (i + 1), COLUMN + " " + content[i][1]);
                }
            }
        }
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(DB_TABLE, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            //Log.v("Error:", "Error: " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        //Log.v("-----------------", "------------------");
    }

    public int makeEmpty(String DB_TABLE) {
        return sqLiteDatabase.delete(DB_TABLE, null, null);
    }

    public int makeEmpty(String DB_TABLE, String args) {
        return sqLiteDatabase.delete(DB_TABLE, args, null);
    }

    public List<String> getAllColumns(String DB_TABLE) {

        // List<String> columns= mySQLiteAdapter.getAllColumns("questions");
        // for(String column: columns){ //Log.v("",column); }

        String[] columns;
        List<String> list = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.query(DB_TABLE, null, null, null, null,
                null, null);
        columns = cursor.getColumnNames();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        for (int i = 0; i < columns.length; i++) {
            list.add(columns[i]);
        }
        return list;
    }

    public Cursor queueAll(String DB_TABLE, String[] Columns, String order,
                           String condition) {
        Cursor cursor = sqLiteDatabase.query(DB_TABLE, Columns, condition,
                null, null, null, order);
        return cursor;
    }

    public Cursor queueAll(String DB_TABLE, String[] Columns, String order,
                           String condition, String groupby) {
        Cursor cursor = sqLiteDatabase.query(DB_TABLE, Columns, condition,
                null, groupby, null, order);
        return cursor;
    }

    private static class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

    // to get master id
    public String getMasterId(String id) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM survey_questions where question_id='" + id
                + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("master_id"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }

        return details;

    }

    public String getQuesId(String value, String qid) {// getQuesId(String
        // value,String qid) {

        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        // String sql =
        // "SELECT * FROM tabularoptions where tabularOptionValue='"
        // + value + "'";

        String sql = "SELECT * FROM tabularoptions where tabularOptionValue='"
                + value + "' and questionId='" + qid + "'";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("id"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public ArrayList<String> getAllTabularOptions(String master_id) {
        // String
        // sql="SELECT * FROM NotificationList ORDER BY notification DESC";

        String sql = "SELECT * FROM tabularoptions where questionId='"
                + master_id + "'";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<String> array = new ArrayList<String>();
        while (c.moveToNext()) {

            String options = c
                    .getString(c.getColumnIndex("tabularOptionValue"));
            array.add(options);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }// rijo added if
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    public ArrayList<String> getAllTabularSubOptions(String id) {
        // String
        // sql="SELECT * FROM NotificationList ORDER BY notification DESC";

        String sql = "SELECT * FROM tabularSuboptions where tabularOptionId='"
                + id + "'";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<String> array = new ArrayList<String>();
        while (c.moveToNext()) {

            String options = c.getString(c
                    .getColumnIndex("tabularSuboptionValue"));
            array.add(options);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }// rijo added if
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    public void addTabularSurVeyResults(Survey details) {

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("customer_id", details.customer_id);
        values.put("survey_question_id", details.survey_questionId);
        values.put("surveyset_id", details.surveyset_id);
        values.put("survey_question", details.survey_question);
        values.put("survey_question_type", details.survey_qn_type);
        values.put("survey_id", details.survey_id);
        values.put("device_id", details.deviceid);
        values.put("survey_answer", details.survey_answers);
        values.put("survey_time", details.survey_time);
        values.put("tabular_option_id", details.tabular_option_id);
        values.put("tabular_suboption_id", details.tabular_suboption_id);
        values.put("tabular_suboption_value", details.tabular_suboption_value);
        values.put("tabular_option_value", details.tabular_option_value);
        values.put("survey_no", details.survey_no);
        db.insert("survey_result", null, values);

        db.close();
    }

    public String getTabularSubOpId(String value) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularSuboptions where tabularSuboptionValue='"
                + value + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("tabularOptionId"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public String getTabularSubOpIdFromQuestion(String value) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularoptions where tabularOptionValue='"
                + value + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("id"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public String getTabularOpIds(String value) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularoptions where tabularOptionValue='"
                + value + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("id"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public String getTabularId(String value) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularSuboptions where tabularSuboptionValue='"
                + value + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("id"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public String getTabularIdUsingSubOption(String value, String subOptionId) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularSuboptions where tabularSuboptionValue='"
                + value + "' AND tabularOptionId='" + subOptionId + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("id"));

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public String getTabularOp(String id) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularoptions where id='" + id + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("tabularOptionValue"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }

        return details;

    }

    public String getTabularSubOps(String id, String tabOpValue) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM tabularSuboptions where tabularOptionId='"
                + id + "' AND tabularSuboptionValue='" + tabOpValue + "'";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("tabularOptionValue"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public ArrayList<Profile> getAllCustomer() {

        String sql = "SELECT distinct customer_id FROM survey_result_temp";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Profile> array = new ArrayList<Profile>();
        while (c.moveToNext()) {
            Profile pro = new Profile();
            pro.customer_id = c.getString(c.getColumnIndex("customer_id"));
            array.add(pro);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    public String getStateName() {

        String name = "";
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        /*
         * String sql1 = "SELECT * FROM survey_result"; sqLiteHelper = new
         * SQLiteHelper(context, DB_NAME, null, DB_VERSION); SQLiteDatabase db =
         * sqLiteHelper.getReadableDatabase(); Cursor c1 = db.rawQuery(sql1,
         * null); int index = c1.getColumnIndex("survey_no"); if (index == -1) {
         * //System.out.println("No column found"); } else {
         */
        String sql1 = "SELECT survey_distcts.state_id FROM survey_customerdetails inner join survey_distcts on survey_distcts.dist_id=survey_customerdetails.customer_dist";
        Cursor c = db.rawQuery(sql1, null);
	/*int m_count=Integer.parseInt(c.getString(c
			.getColumnIndex("survey_no")));*/


        if (c.moveToFirst()
        ) {

            try {
                String sql = "SELECT state_name  FROM survey_customerdetails where state_id=" + ' ' + " ";


                Cursor c1 = db.rawQuery(sql, null);
                c1.moveToNext();
                name = c1.getString(c1
                        .getColumnIndex("state_name"));

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Exception " + e);
            }

            c.close();
        } else {
            //Log.i("TAG", "No Data" + "Empty Table");
        }


        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }

        return name;


    }

    public int getAllCustomerSurveyDistinct() {
        int countSurvey = 0;
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        /*
         * String sql1 = "SELECT * FROM survey_result"; sqLiteHelper = new
         * SQLiteHelper(context, DB_NAME, null, DB_VERSION); SQLiteDatabase db =
         * sqLiteHelper.getReadableDatabase(); Cursor c1 = db.rawQuery(sql1,
         * null); int index = c1.getColumnIndex("survey_no"); if (index == -1) {
         * //System.out.println("No column found"); } else {
         */
        String sql1 = "SELECT * FROM survey_result";
        Cursor c = db.rawQuery(sql1, null);
		/*int m_count=Integer.parseInt(c.getString(c
				.getColumnIndex("survey_no")));*/


        if (c.moveToFirst()
        ) {

            try {
                String sql = "SELECT count(distinct survey_no) as survey_count FROM survey_result";


                Cursor c1 = db.rawQuery(sql, null);
                c1.moveToNext();
                countSurvey = Integer.parseInt(c1.getString(c1
                        .getColumnIndex("survey_count")));

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Exception " + e);
            }

            c.close();
        } else {
            //Log.i("TAG", "No Data" + "Empty Table");
        }


        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }

        return countSurvey;

    }

    public ArrayList<Survey> getAllSurvey() {


        String sql = "SELECT * FROM survey_sets";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Survey> array = new ArrayList<Survey>();
        while (c.moveToNext()) {
            Survey survey = new Survey();
            survey.survey_name = c
                    .getString(c.getColumnIndex("surveyset_name"));
            survey.survey_id = c.getString(c.getColumnIndex("surveyset_id"));
            array.add(survey);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    /*
     * public String getFinalCustomerId(){
     *
     * String str=null; Log.d("TAG", "getTabularOp>>>>>"); sqLiteHelper = new
     * SQLiteHelper(context, DB_NAME, null, DB_VERSION); Log.d("TAG",
     * "getTabularOp>>>>>1");
     *
     * String sql="SELECT MAX(customer_id) FROM survey_customerdetails";
     * SQLiteDatabase db = sqLiteHelper.getReadableDatabase(); Cursor
     * cursor=db.rawQuery(sql,null); int placeColumn =
     * cursor.getColumnIndex("customer_id"); Log.d("TAG",
     * "getTabularOp>>>>>2"+placeColumn); //c.moveToLast();
     * //details=c.getString(c.getColumnIndex("customer_id")); //Log.d("TAG",
     * "getTabularOp>>>>>2"+c.getString(0)); //if(cursor.moveToN(placeColumn)){
     * str = cursor.getString(placeColumn); Log.d("TAG",
     * "getTabularOp>>>>>3"+str); //} return str; }
     */
    public ArrayList<Survey> getAllSurveyMax() {
        // String
        // sql="SELECT * FROM NotificationList ORDER BY notification DESC";

        String sql = "SELECT * FROM survey_customerdetails";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Survey> array = new ArrayList<Survey>();
        while (c.moveToNext()) {
            Survey survey = new Survey();
            survey.customer_id = c.getString(c.getColumnIndex("customer_id"));
            // survey.survey_id=c.getString(c.getColumnIndex("surveyset_id"));

            // Log.d("TAG", survey.survey_id);

            array.add(survey);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    public int surveyResultCount() {

        int result_count = 0;

        String sql = "SELECT COUNT(device_id) FROM survey_result";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();
        result_count = c.getInt(0);
        // result_count=c.getCount();
        c.close();
        db.close();
        return result_count;
    }

    public int surveyResultCounting() {

        int result_count = 0;

        String sql = "SELECT COUNT(*) FROM survey_result";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();
        // result_count=c.getCount();
        result_count = c.getInt(0);
        c.close();
        db.close();
        return result_count;
    }

    public void addGpsCoordinatesToDb(Survey details) {
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        // ContentValues values = new ContentValues();
        //
        // values.put("customer_id", details.cus_id);
        // values.put("latitude", details.latitude);
        // values.put("longitude", details.longitude);//survey_customerdetails
        ContentValues values = new ContentValues();
        values.put("latitude", details.latitude);
        values.put("longitude", details.longitude);

        db.update("survey_customerdetails", values, "customer_id="
                + details.cus_id, null);

        // db.insert("survey_location", null, values);

        db.close();
    }

    /*
     * public ArrayList<String> getAllGPSCoordinatesFromDb(){ //String
     * sql="SELECT * FROM NotificationList ORDER BY notification DESC";
     * Log.d("TAG", "getAllTabularOptions>>>>>");
     *
     * String sql="SELECT * FROM survey_location'"; sqLiteHelper = new
     * SQLiteHelper(context, DB_NAME, null, DB_VERSION);
     *
     * SQLiteDatabase db = sqLiteHelper.getReadableDatabase(); Cursor
     * c=db.rawQuery(sql,null); ArrayList<String> array = new
     * ArrayList<String>(); while (c.moveToNext()) { Log.d("TAG",
     * "getAllTabularOptions>>>>>1");
     *
     * String latitude = c.getString(c.getColumnIndex("latitude"));
     * array.add(options);
     *
     * } return array;
     *
     *
     * }*
     */

    public void update_GPS(Survey details, String id) {
        //System.out.println("update_GPS" + details.latitude);

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", details.latitude);
		/*//System.out.println("GPS 123" + details.latitude);
		//System.out.println("GPS 123" + details.longitude);*/

        values.put("longitude", details.longitude);
        db.update("survey_customerdetails", values, "customer_id=" + id, null);
        db.close();
    }

    public void update_UserDeletion(String custId, String markDeletion) {

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("mark_for_deletion", markDeletion);
        values.put("status", "2");
        db.update("survey_customerdetails", values, "customer_id=" + custId,
                null);
        db.close();
    }

    public void update_UserVerify(String custId, String isVerify) {

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("verify_status", isVerify);
        values.put("status", "2");
        db.update("survey_customerdetails", values, "customer_id=" + custId,
                null);
        db.close();
    }

    public void updateCustometStatus() {

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        String[][] con_editcust = {{"status", "2"}};
        mySQLiteAdapter.openToWrite();
        ContentValues values = new ContentValues();
        values.put("status", "0");
        db.update("survey_customerdetails", values, "1 or 2", null);
        db.close();
    }

    public void addtourPlan(Survey details) {
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("customer_id", details.cus_id);
        values.put("customer", details.customer_name);
        values.put("plan_date", details.plan_date);
        values.put("districtname", details.districtName);
        // values.put("longitude", details.longitude);

        db.insert("survey_tourplan", null, values);

        db.close();
    }

    public ArrayList<Survey> getDatePlans(String date, String district) {
        // String
        // sql="SELECT * FROM NotificationList ORDER BY notification DESC";

        // String
        // sql="SELECT * FROM survey_tourplan where plan_date='"+date+"' ";
        String sql = "SELECT distinct  customer,customer_id FROM survey_tourplan where plan_date='"
                + date + "' AND districtname='" + district + "'";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Survey> array = new ArrayList<Survey>();
        while (c.moveToNext()) {
            Survey survey = new Survey();
            survey.customer_name = c.getString(c.getColumnIndex("customer"));
            survey.cus_id = c.getString(c.getColumnIndex("customer_id"));

            array.add(survey);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return array;

    }

    public String getSearchID(String id) {
        String details = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT search_ids FROM survey_multitextoptions where question_id='"
                + id + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("search_ids"));


        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return details;

    }

    public ArrayList<Survey> getAllImages() {
        // String
        // sql="SELECT * FROM NotificationList ORDER BY notification DESC";

        // String
        // sql="SELECT * FROM survey_tourplan where plan_date='"+date+"' ";
        String sql = "select * from question_images ";
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Survey> array = new ArrayList<Survey>();
        while (c.moveToNext()) {
            Survey survey = new Survey();
            survey.questionId = c.getString(c.getColumnIndex("questionId"));
            survey.imagePath = c.getString(c.getColumnIndex("imagePath"));
            array.add(survey);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return array;
    }

    public void UpdateDownloadedimage(String image, String id) {

        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("image_download", image);
        db.update("question_images", values, "questionId=" + id, null);
        db.close();
    }

    public String getImageByID(String id) {
        String image = null;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM question_images where questionId='" + id
                + "' ";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            image = c.getString(c.getColumnIndex("image_download"));
            // image = c.getBlob(3);

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return image;

    }

    public int getResultCount() {
        int count = 0;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM survey_result";
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        count = c.getCount();
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return count;

    }

    public Cursor getSearchedProfiles(String condition) {
        int count = 0;
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        String sql = "SELECT * FROM survey_customerdetails WHERE " + condition + "";

        System.out.println("Query " + sql);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        // count = c.getCount();
        if (c != null && !c.isClosed()) {
            c.close();
        }
        if (db != null) {
            db.close();// rijo added if
        }
        return c;

    }

    public void addtoTabularSubOption(Survey details) {
        sqLiteHelper = new SQLiteHelper(context, DB_PATH, null, DB_VERSION);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("questionId", details.questionId);
        values.put("tabularOptionId", details.tabular_option_id);
        values.put("tabularSuboptionValue", details.tabular_suboption_value);
        // values.put("districtname", details.districtName);
        // values.put("longitude", details.longitude);

        db.insert("tabularsuboptions", null, values);

        db.close();
    }

}
