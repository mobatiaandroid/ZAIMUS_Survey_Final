package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaimus.R;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import java.util.List;
import java.util.Locale;

public class ShowPicture extends Activity {

    private Button closeButton;
    private ImageView imageView;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    TextView location;
    String strAdd;
    Context context = this;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);
        setContentView(R.layout.show_picture);
        LocationTrack locationTrack = new LocationTrack(ShowPicture.this);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowPicture.this.finish();

            }
        });

        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        imageView = (ImageView) findViewById(R.id.cameraImgview);
        location = (TextView) findViewById(R.id.location);
        location.setTypeface(typeface);
        getCompleteAddressString(locationTrack.getLatitude(), locationTrack.getLongitude());
        //location.setText("Latitude : "+gps.getLatitude()+" Longitude : "+gps.getLongitude()+" You are at "+strAdd);
        imageView.setImageBitmap(bitmap);

        closeButton = (Button) findViewById(R.id.bt_pic_close);
        closeButton.setTypeface(typeface);
        closeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, ShowPicture.this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AudioRecorder.stopTimer();
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                strAdd = "Address not found";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strAdd;

    }
}
