package com.hz_apps.timebasedlocker.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class LockFileAdapter extends RecyclerView.Adapter<LockFileAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesList;
    private final ArrayAdapter spinnerAdapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private final int YEAR, MONTH, DAY_OF_MONTH;
    private LocalDateTime SetDateForAllItems;
    public LocalDateTime[] localDateTimesList;
    private boolean dateNotSetWarning = false;


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

        showDateTimeDialog(holder);

        System.out.println("dateNotSetWarning state " + dateNotSetWarning);

        if (dateNotSetWarning) {
            System.out.println("Date not set warning");
            holder.set_date.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView set_date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.lock_file_imageview);
            set_date = itemView.findViewById(R.id.set_date_textView);
        }
    }

    private void showDateTimeDialog(myViewHolder holder){

        // check : if user selected to set date for all items then set date and also save in list
        if (SetDateForAllItems != null){
            holder.set_date.setText(SetDateForAllItems.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            for (int i=0; i<imagesList.size(); i++){
                localDateTimesList[i] = SetDateForAllItems;
            }
        }
        // show date picker dialog on click of set date text view
        holder.set_date.setOnClickListener(v ->{
            datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> showTimePickerDialog(holder, year, month, dayOfMonth), YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.show();
        });
    }

    // Time picker dialog
    private void showTimePickerDialog(myViewHolder holder, int YEAR, int MONTH, int DAY_OF_MONTH){
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                LocalDateTime dateTime = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, hourOfDay, minute);
                localDateTimesList[holder.getAdapterPosition()] = dateTime;
                holder.set_date.setText(dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
        },12, 0, false );
        timePickerDialog.show();
    }

    public void setSetDateForAllItems(LocalDateTime localDateTime){
        SetDateForAllItems = localDateTime;
    }

    public LocalDateTime[] getLocalDateTimesList(){
        return localDateTimesList;
    }

    public void setDateNotSetWarning(boolean b){
        this.dateNotSetWarning = b;
    }

    private void setDateToItem(TextView textView){

    }
}
