package com.example.jewharyimer.newopas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class My_Adapter extends RecyclerView.Adapter<My_Adapter.ViewHolder> {
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> notes=new ArrayList<>();
    ArrayList<String> dates=new ArrayList<>();

    public void update(String note,String date){
        notes.add(note);
        dates.add(date);
        notifyDataSetChanged();
    }

    public My_Adapter(RecyclerView recyclerView, Context context, ArrayList<String> note,ArrayList<String> date) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.notes = note;
        this.dates = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.fileitem,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nameloffile.setText(notes.get(i));
        viewHolder.sdate.setText(dates.get(i));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameloffile,sdate;
        public ViewHolder(final View itemView){
            super(itemView);
            nameloffile=itemView.findViewById(R.id.text_filename);
            sdate=itemView.findViewById(R.id.text_date);

        }




    }
}
