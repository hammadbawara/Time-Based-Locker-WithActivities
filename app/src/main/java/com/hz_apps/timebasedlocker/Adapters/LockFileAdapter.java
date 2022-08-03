package com.hz_apps.timebasedlocker.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class LockFileAdapter extends RecyclerView.Adapter<LockFileAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesList;
    private final ArrayAdapter spinnerAdapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private final int YEAR, MONTH, DAY_OF_MONTH;
    private String DateForAllItems;
    private LocalDateTime[] localDateTimesList;


    public LockFileAdapter(Context context, ArrayList<File> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        spinnerAdapter = new ArrayAdapter(context, R.layout.drop_down_item, new String[] {"By Day", "By Date"});
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        this.localDateTimesList = new LocalDateTime[imagesList.size()];
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

        setSpinnerClickListener(holder.spinner);

        showCalendarDialog(holder);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final Spinner spinner;
        private final TextView set_date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.lock_file_imageview);
            spinner = itemView.findViewById(R.id.spinner);
            set_date = itemView.findViewById(R.id.set_date_textView);
        }
    }

    private void setSpinnerClickListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showCalendarDialog(myViewHolder holder){

        if (DateForAllItems != null){
            holder.set_date.setText(DateForAllItems);
        }

        holder.set_date.setOnClickListener(v ->{
            datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> showDatePicker(holder, year, month, dayOfMonth), YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.show();
        });
    }

    private void showDatePicker(myViewHolder holder, int YEAR, int MONTH, int DAY_OF_MONTH){
        timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) ->
                localDateTimesList[holder.getAdapterPosition()].of(YEAR, MONTH, DAY_OF_MONTH, hourOfDay, minute), 12, 60, false);
        timePickerDialog.show();
    }

    public void setDateForAllItems(String text){
        DateForAllItems = text;
    }
}
