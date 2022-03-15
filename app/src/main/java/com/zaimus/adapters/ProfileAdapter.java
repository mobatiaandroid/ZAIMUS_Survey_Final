package com.zaimus.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaimus.Profiles.Profile;
import com.zaimus.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private ArrayList<Profile> profiles;
    private ArrayList<Profile> cList;

    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName;
        public TextView userType;
        LinearLayout searchLAyout;

        public MyViewHolder(View view) {
            super(view);

            shopName = (TextView) view.findViewById(R.id.search_item);
            userType = (TextView) view.findViewById(R.id.search_type);
            searchLAyout = (LinearLayout) view.findViewById(R.id.searchLAyout);

        }
    }


    public ProfileAdapter(Activity mActivity, ArrayList<Profile> moviesList, ArrayList<Profile> cList) {
        this.profiles = moviesList;
        this.activity = mActivity;
        this.cList = cList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Profile pro = profiles.get(position);

        String data = pro.get_customer_shopname().toUpperCase() + ","
                + pro.get_customer_add1().toUpperCase() + ","
                + pro.get_customer_place().toUpperCase() + ","
                + pro.get_customer_dist().toUpperCase();
        //

        //pro.getZaimus_id().toUpperCase()
        holder.shopName.setTag(pro.get_customer_id());
        holder.shopName.setTag(R.id.addr1,
                pro.get_customer_status());

        String id = profiles.get(position).get_customer_id();
        holder.searchLAyout.setBackgroundColor(Color.TRANSPARENT);
        for (int j = 0; j < cList.size(); j++) {

            String c_id = cList.get(j).get_customer_id();

            if (id.equals(c_id)) {
                holder.searchLAyout.setBackgroundColor(Color.rgb(144,
                        238, 144));
            } else {

            }
        }
        holder.shopName.setText(data);

        if (pro.get_customer_category().equals("10")) {
            holder.userType.setText("R");

        } else if (pro.get_customer_category().equals("3")) {
            holder.userType.setText("D");

        } else {
            holder.userType.setText("S");

        }

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }
}