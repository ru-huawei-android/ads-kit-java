package com.sample.huawei.ads;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.ads.R;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvDataHolder> {

    private List<AdFormat> dataSet;

    private AppCompatActivity activity;

    public RvAdapter(ArrayList<AdFormat> dataSet, AppCompatActivity activity) {
        this.dataSet = dataSet;
        this.activity = activity;
    }

    class RvDataHolder extends RecyclerView.ViewHolder {

        public RvDataHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    @NonNull
    @Override
    public RvDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout textView = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        return new RvDataHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RvDataHolder holder, int position) {
        TextView tv = ((TextView)holder.itemView.findViewById(R.id.text));
        tv.setText(dataSet.get(position).title);

        holder.itemView.setOnClickListener(view -> activity.startActivity(new Intent(activity, dataSet.get(position).targetClass)));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
