package com.hz_apps.timebasedlocker.Adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;

import java.io.File;
import java.util.ArrayList;

public class LockFileAdapter extends RecyclerView.Adapter<LockFileAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesList;
    private final ArrayAdapter spinnerAdapter;


    public LockFileAdapter(Context context, ArrayList<File> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        spinnerAdapter = new ArrayAdapter(context, R.layout.drop_down_item, new String[] {"By Day", "By Date"});
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_for_lock_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        File image = imagesList.get(position);
        Glide.with(context).load(image).into(holder.imageView);
        holder.spinner.setAdapter(spinnerAdapter);

        setSpinnerClickListener(holder.spinner, holder.days, holder.date, position);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final Spinner spinner;
        private final EditText days;
        private final EditText date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.lock_file_imageview);
            spinner = itemView.findViewById(R.id.spinner);
            days = itemView.findViewById(R.id.days_editText);
            date = itemView.findViewById(R.id.date_editText);
        }
    }

    private void setSpinnerClickListener(Spinner spinner, EditText days, EditText date, int adapterPosition){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        date.setVisibility(View.GONE);
                        days.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        days.setVisibility(View.GONE);
                        date.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void launchDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Picker_Date_Calendar);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
