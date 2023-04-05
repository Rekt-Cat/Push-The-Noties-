package com.example.pushthenoties;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class sentNotiAdapter extends RecyclerView.Adapter<sentNotiAdapter.viewHolder>{

    ArrayList<receivedModel> data;
    Context context;

    public sentNotiAdapter(ArrayList<receivedModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public sentNotiAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notiitem,parent,false);
        return new sentNotiAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull sentNotiAdapter.viewHolder holder, int position) {

        for(int i =0;i<data.size();i++){
            Log.d("size", "Onevent: "+data.get(i));
        }
        Log.d("size", "onEvent: "+data.size());
        holder.title.setText(data.get(position).getTitle());
        holder.body.setText(data.get(position).getDescription());
        Log.d("size", "onEvent: "+data.get(position).getTitle());
        Log.d("size", "onEvent: "+data.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView title,body;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.titleH);
            body=itemView.findViewById(R.id.bodyH);

        }
    }
}
