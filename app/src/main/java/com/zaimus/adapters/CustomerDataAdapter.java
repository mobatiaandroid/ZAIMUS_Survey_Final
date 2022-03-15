package com.zaimus.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaimus.R;
import com.zaimus.api.model.CustomerData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
    private ArrayList<CustomerData> mDataset;
    Activity mActivity;

    public CustomerDataAdapter(ArrayList<CustomerData> myDataset, Activity mActivity) {
        mDataset = myDataset;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder customerViewHolder, int i) {

        String data = mDataset.get(i).getShopName().toUpperCase() + ","
                + mDataset.get(i).getBuilding().toUpperCase() + ","
                + mDataset.get(i).getPlace().toUpperCase() + ","
                + mDataset.get(i).getDistrict().toUpperCase();
        customerViewHolder.mTextView.setText(data);


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.search_item);
            //v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //recyclerViewItemClickListener.clickOnItem(mDataset.get(getAdapterPosition()));

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(String data);
    }
}
