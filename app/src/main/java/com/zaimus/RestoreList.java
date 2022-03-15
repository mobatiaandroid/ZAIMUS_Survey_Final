package com.zaimus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestoreList extends ListActivity implements View.OnClickListener {

    final String[] items = new String[]{"Item1", "Item2", "Item3", "Item4"};
    ArrayAdapter<String> ad;

    private List<String> item = null;

    private List<String> path = null;

    //private String root = "/sdcard/vkcrestore";
    private String root;
    ListView lv;
    private Button btnRestore;
    private int pos;
    ProgressDialog progressDialog;
    Boolean isSuccess = false;
    Boolean isException = false;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        root = mydir.getAbsolutePath();

        //setTitleColor(getResources().getColor(android.R.color.white));
        setContentView(R.layout.restore_list);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "Restore");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RestoreList.this.finish();

            }
        });
        initialiseUI();
        GetBackupDb getBackupDb = new GetBackupDb();
        getBackupDb.execute();
    }

    private void initialiseUI() {
        lv = getListView();
        btnRestore = (Button) findViewById(R.id.button_restore);
        // ad =new
        // ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,items);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // lv.setAdapter(ad);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                lv.setItemChecked(position, true);

                pos = position;


            }

        });
        btnRestore.setOnClickListener(this);
    }

    private void getDir(String dirPath) {
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        if (f.exists()) {
            File[] files = f.listFiles();
            /*
             * if (!dirPath.equals(root)) { item.add(root); path.add(root);
             * item.add("../"); path.add(f.getParent()); }
             */
            for (int i = (files.length - 1); i >= 0; i--) {

//			for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().endsWith(".sql")) {
                    path.add(file.getPath());
                    if (file.isDirectory())
                        item.add(file.getName() + "/");
                    else
                        item.add(file.getName());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnRestore) {
            restoreAlert();
        }

    }

    private class GetBackupDb extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RestoreList.this, "",
                    "Loading...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            getDir(root);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (item.size() > 0) {
                ad = new ArrayAdapter<String>(RestoreList.this,
                        android.R.layout.simple_list_item_single_choice, item);
                setListAdapter(ad);
                lv.setItemChecked(0, true);
            } else {
                Toast.makeText(RestoreList.this, "No items found to restore",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    private class RestoreDb extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RestoreList.this, "",
                    "In Progress...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
				/*Utils.checkDirExists("/VKCSurvey");
				try {
					Utils.copyFile(
							new File(
									Environment.getDataDirectory()
											+ "VKCSurvey/"+ lv.getItemAtPosition(pos).toString()),
							new File(Environment.getExternalStorageDirectory()
									+ "VKCSurvey/vkcsurvey"
									+ Utils.getCalender() + ".sql"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

                // File mydir = getDir("VKCSurvey", Context.MODE_PRIVATE); //Creating an internal dir;
                File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");


                if (mydir.exists()) {

                    String path = mydir.getAbsolutePath() + "/"
                            + lv.getItemAtPosition(pos).toString();
                    Utils.copyFile(
                            new File(path),
                            new File(mydir.getAbsolutePath()
                                    + "/zaimussurvey.sql"));
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isException = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (isSuccess) {
                PreferenceManager.saveUpgrade("yes", RestoreList.this);
                Toast.makeText(RestoreList.this,
                        "The data is successfully restored.",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else if (isException) {
                Toast.makeText(RestoreList.this,
                        "Cannot continue at this time, Please try again later",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestoreList.this,
                        "SD Card unmounted or not present", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void restoreAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RestoreList.this);
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.setMessage("Restore option will replace the existing data, Do you wish to restore?");
        alert.setPositiveButton("Yes", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                RestoreDb restoreDb = new RestoreDb();
                restoreDb.execute();
            }
        });
        alert.setNegativeButton("No", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create();
        alert.setCancelable(false);
        Dialog d = alert.show();
        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.white));
    }
}
