package com.zaimus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zaimus.Survey.Claims;
import com.zaimus.api.VkcApis;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimListActivity extends Activity {
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, addClaim;
    Activity activity = this;
    ProgressDialog Dialog;
    ArrayList<Claims> claimsList = new ArrayList<Claims>();
    Context context = this;
    ListView cliamslistView;
    EditText edtSearch;
    ClaimListAdapter adapter;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.claimlist);
        header = (LinearLayout) findViewById(R.id.header);
        cliamslistView = (ListView) findViewById(R.id.claimslistView);
        edtSearch = (EditText) findViewById(R.id.searchTextEntry);
        cliamslistView.setTextFilterEnabled(true);

        edtSearch.setVisibility(View.GONE);
        headermanager = new Headermanager(activity, "Claims");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        addClaim = headermanager.getRightButton();
        addClaim.setVisibility(View.VISIBLE);
        if ((int) Build.VERSION.SDK_INT >= 23)


        {
            TedPermission.with(this)
                    .setPermissionListener(permissionVidyoCalllistener)
                    .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.CAMERA)
                    .check();
        }
        addClaim.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ClaimListActivity.this,
                        AddClaimsActivity.class);
                startActivity(intent);
            }
        });
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ClaimListActivity.this.finish();

            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //Log.e("", "afterTextChanged called");
                //adapter.getFilter().filter(s, cliamslistView);
                //adapter.notifyDataSetChanged();
            }
        });
        if (Utils.checkInternetConnection(context)) {
            doClaimsAsynchTask task = new doClaimsAsynchTask();
            task.execute();
        } else {
            Toast.makeText(context, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


    }

    public class doClaimsAsynchTask extends AsyncTask<Void, Integer, Void> {

        private String success;

        @Override
        protected void onPostExecute(Void result) {

            Dialog.dismiss();

            if (claimsList.size() == 0) {
                Toast.makeText(context, "No claims available",
                        Toast.LENGTH_SHORT).show();
            } else {
                adapter = new ClaimListAdapter(context, claimsList);
                cliamslistView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

                //cliamslistView.setTextFilterEnabled(true);

            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            Dialog = new ProgressDialog(ClaimListActivity.this);
            Dialog.setMessage(ClaimListActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {

            claimsList = VkcApis.get_claims(
                    AppPreferenceManager.getUserId(context), context);
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(ClaimListActivity.this, VKCAppActivity.class);
        startActivity(intent);
    }

    public class ClaimListAdapter extends BaseAdapter {
        SimpleDateFormat sdf, formatter;
        private Context context;
        private View mView;
        private LayoutInflater inflater;
        ArrayList<Claims> mArrayList;
        ArrayList<Claims> cArrayList;

        TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
                statusTextView, dateTextView;
        Date tempDate;
        String newformat;
        private Filter filter;

        // String[] items=new String[]{"Whole Sale","Retail"};

        public ClaimListAdapter(Context context, ArrayList<Claims> arraylist) {
            this.context = context;
            this.mArrayList = arraylist;
            this.cArrayList = new ArrayList<Claims>();
            this.cArrayList.addAll(mArrayList);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // return items.length;
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {

            return 0;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        public Filter getFilter() {
            if (filter == null) {
                filter = new ClaimListFilter();
            }
            return filter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mView = inflater.inflate(R.layout.claimlist_item, null);
            } else {
                mView = convertView;
            }
            Typeface typeface = Utils.setFontTypeToArial(context);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            titleTextView = (TextView) mView.findViewById(R.id.titleTextView);
            descTextView = (TextView) mView.findViewById(R.id.descTextView);
            // surveyType.setText(items[position]);
            reqAmountTextView = (TextView) mView.findViewById(R.id.reqTextView);
            aprAmountTextView = (TextView) mView.findViewById(R.id.aprTextView);
            statusTextView = (TextView) mView.findViewById(R.id.statusTextView);
            dateTextView = (TextView) mView.findViewById(R.id.DateTextView);

            try {
                tempDate = sdf.parse(mArrayList.get(position).date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            formatter = new SimpleDateFormat("dd-MMM-yy");
            newformat = formatter.format(tempDate);

            titleTextView.setText(mArrayList.get(position).title);
            descTextView.setText(mArrayList.get(position).description);
            reqAmountTextView.setText(mArrayList.get(position).reqAmount);
            aprAmountTextView.setText(mArrayList.get(position).aprAmount);
            statusTextView.setText(mArrayList.get(position).staus);
            if (mArrayList.get(position).staus.equals("Approved")) {
                statusTextView.setTextColor(Color.rgb(0, 128, 0));
            } else if (mArrayList.get(position).staus.equals("Pending")) {
                statusTextView.setTextColor(Color.rgb(255, 69, 0));

            } else if (mArrayList.get(position).staus.equals("Rejected")) {
                statusTextView.setTextColor(Color.rgb(220, 20, 60));

            }
            dateTextView.setText(newformat);

            return mView;
        }

        private class ClaimListFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Claims> filteredItems = new ArrayList<Claims>();

                    for (int i = 0, l = mArrayList.size(); i < l; i++) {
                        Claims claims = mArrayList.get(i);
                        if (claims.staus.toLowerCase().contains(constraint))
                            filteredItems.add(claims);
                    }
                    result.count = filteredItems.size();
                    result.values = filteredItems;
                } else {

                    result.values = mArrayList;
                    result.count = mArrayList.size();

                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                cArrayList = (ArrayList<Claims>) results.values;
                notifyDataSetChanged();
                mArrayList.clear();
                for (int i = 0, l = cArrayList.size(); i < l; i++)
                    mArrayList.add(cArrayList.get(i));
                notifyDataSetInvalidated();
            }

        }
    }

    PermissionListener permissionVidyoCalllistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            // splash();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(ClaimListActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };
}
