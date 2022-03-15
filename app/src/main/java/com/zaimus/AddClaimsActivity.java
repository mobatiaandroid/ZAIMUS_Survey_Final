package com.zaimus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.api.VkcApis;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

public class AddClaimsActivity extends Activity {
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Button submitClaims, captureImgBtn;
    ProgressDialog Dialog;
    Survey survey, plan;
    Context context = this;
    EditText edtDescription, edtAmount;
    ImageView takenImg;
    String image;
    Bitmap bitmap;
    Bitmap thumbnail;
    // ArrayList<Survey> surveylist = new ArrayList<Survey>();
    public ArrayList<String> list = new ArrayList<String>();
    String sid;
    SQLiteAdapter madapter;
    Spinner surveySpinner, typeSpinner, distSpinner;
    public ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<Survey> tourList = new ArrayList<Survey>();
    String type;
    ArrayList<Survey> dateList = new ArrayList<Survey>();
    public ArrayList<String> distList = new ArrayList<String>();
    String detailId;
    TextView balanceAmountTitle;
    String balanceamount;
    String description, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.claims);
        survey = new Survey();
        plan = new Survey();
        madapter = new SQLiteAdapter(getApplicationContext());
        header = (LinearLayout) findViewById(R.id.header);
        submitClaims = (Button) findViewById(R.id.submitCliams);
        captureImgBtn = (Button) findViewById(R.id.captureImage);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        distSpinner = (Spinner) findViewById(R.id.edtPlanDist);
        balanceAmountTitle = (TextView) findViewById(R.id.balanceamount);
        // edtTitle = (EditText) findViewById(R.id.edtTitle);
        takenImg = (ImageView) findViewById(R.id.image);

        surveySpinner = (Spinner) findViewById(R.id.edtSelectSurvey);
        typeSpinner = (Spinner) findViewById(R.id.edtType);
        headermanager = new Headermanager(activity, "Submit Claims");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClaimsActivity.this.finish();
            }
        });

        if (Utils.checkInternetConnection(context)) {
            doPlansAsynchTask task = new doPlansAsynchTask();
            task.execute();
        } else {
            Toast.makeText(context, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }

        typeList.add(0, "Food");
        typeList.add(1, "Accomadation");
        typeList.add(2, "Travel");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(context,
                R.layout.simple_spinner_item, typeList);
        typeSpinner.setAdapter(typeAdapter);
        captureImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                takenImg.setVisibility(View.VISIBLE);
                selectImage();
            }
        });

        surveySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                sid = tourList.get(position).plan_id;

                doPlanDetailAsynchTask detailtask = new doPlanDetailAsynchTask();
                detailtask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                type = typeList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        distSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                detailId = dateList.get(position).detailId;
                doBalanceAsynchTask balTask = new doBalanceAsynchTask();
                balTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        submitClaims.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
                 * if (edtTitle.getText().toString().equals("")) {
                 * edtTitle.setError("Please enter the title"); } else
                 */
                if (edtDescription.getText().toString().equals("")) {
                    edtDescription
                            .setError("Please enter the description about the bill");
                } else if (edtAmount.getText().toString().equals("")) {
                    edtAmount.setError("Please enter the amount");
                } else if (AppPreferenceManager.getClaimImage(context).equals(
                        "")) {
                    Toast.makeText(context, "Take Picture ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (Utils.checkInternetConnection(context)) {
                        description = edtDescription.getText().toString();
                        amount = edtAmount.getText().toString();
                        doClaimsAsynchTask claimstak = new doClaimsAsynchTask();
                        claimstak.execute();

                    } else {
                        Toast.makeText(context, "No internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public class doClaimsAsynchTask extends AsyncTask<Void, Integer, Void> {
        private String success;

        @Override
        protected void onPostExecute(Void result) {
            // setProgrees(true);
            Dialog.dismiss();
            if (survey.status.equals("success")) {
                Toast.makeText(context, "Claims successfully completed",
                        Toast.LENGTH_SHORT).show();
                AddClaimsActivity.this.finish();
                Intent intent = new Intent(AddClaimsActivity.this,
                        ClaimListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Claims submission failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(AddClaimsActivity.this);
            Dialog.setMessage(AddClaimsActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {

            survey = VkcApis.export_claims(AppPreferenceManager
                            .getUserId(context), "0", type,
                    description, amount,
                    AppPreferenceManager.getClaimImage(context), sid, detailId, context);
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 0) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            // takenImg.setImageBitmap(bitmap);
            takenImg.setVisibility(View.VISIBLE);
            decodeImage(thumbnail);
            // finish();
        } else if (requestCode == 10) {
            takenImg.setVisibility(View.VISIBLE);
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(
                    selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            // Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
            decodeImage(thumbnail);
            takenImg.setImageBitmap(thumbnail);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void decodeImage(Bitmap bitmap) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] ba = bytes.toByteArray();
        image = Base64.encodeToString(ba, Base64.DEFAULT);
        AppPreferenceManager.saveClaimImage(image, context);
        StringToBitMap(AppPreferenceManager.getClaimImage(context));
        takenImg.setVisibility(View.VISIBLE);
        takenImg.setImageBitmap(thumbnail);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), 10);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    public class doPlansAsynchTask extends AsyncTask<Void, Integer, Void> {

        private String success;

        @Override
        protected void onPostExecute(Void result) {

            Dialog.dismiss();

            if (tourList.size() == 0) {
                Toast.makeText(context, "No tourplans available",
                        Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < tourList.size(); i++) {
                    list.add(tourList.get(i).plan_name);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                        context, R.layout.simple_spinner_item, list);
                surveySpinner.setAdapter(dataAdapter);
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            Dialog = new ProgressDialog(AddClaimsActivity.this);
            Dialog.setMessage(AddClaimsActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {
            //madapter.makeEmpty("survey_tourplan");
            tourList = VkcApis.get_tourplan(
                    AppPreferenceManager.getUserId(context), context);
            return null;
        }

    }

    public class doPlanDetailAsynchTask extends AsyncTask<Void, Integer, Void> {

        String success;
        ProgressDialog Dialog;

        @Override
        protected void onPostExecute(Void result) {

            Dialog.dismiss();
            if (plan != null) {
                dateList = plan.getPlanDate();
                distList.clear();
                for (int k = 0; k < dateList.size(); k++) {
                    distList.add(dateList.get(k).districtName + " ( " + dateList.get(k).plan_from_date + " to " +
                            dateList.get(k).plan_to_date + " )");

                }
                ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(context,
                        R.layout.simple_spinner_item, distList);
                distSpinner.setAdapter(distAdapter);

            }


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            Dialog = new ProgressDialog(AddClaimsActivity.this);
            Dialog.setMessage(AddClaimsActivity.this.getResources()
                    .getString(R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {

            plan = VkcApis.get_tourplanindetail(
                    AppPreferenceManager.getUserId(context), sid, context);

            return null;
        }

    }

    public class doBalanceAsynchTask extends AsyncTask<Void, Integer, Void> {
        private String success;

        @Override
        protected void onPostExecute(Void result) {
            // setProgrees(true);
            Dialog.dismiss();
            if (balanceamount.equals("0")) {
                Toast.makeText(getApplicationContext(), "You have no balance", Toast.LENGTH_SHORT).show();
                balanceAmountTitle.setVisibility(View.GONE);

            } else {
                balanceAmountTitle.setText("Balance Amount :" + balanceamount);
            }
        }

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(AddClaimsActivity.this);
            Dialog.setMessage(AddClaimsActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {


            balanceamount = VkcApis.getbalance(detailId);
            return null;
        }
    }


}
