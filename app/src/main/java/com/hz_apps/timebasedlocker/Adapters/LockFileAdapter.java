package com.hz_apps.timebasedlocker.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class LockFileAdapter extends RecyclerView.Adapter<LockFileAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesList;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private final int YEAR, MONTH, DAY_OF_MONTH;
    private LocalDate DateForAllItems;
    public DateAndTime[] dateAndTimeList;
    private boolean dateNotSetWarning = false;
    private LocalTime TimeForAllItems;


    public LockFileAdapter(Context context, ArrayList<File> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        this.dateAndTimeList = new DateAndTime[imagesList.size()];
        for (int i=0; i<imagesList.size(); i++){
            dateAndTimeList[i] = new DateAndTime();
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_for_lock_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if (TimeForAllItems==null && DateForAllItems==null){
            File image = imagesList.get(position);
            Glide.with(context).load(image).into(holder.imageView);
            holder.lock_file_title.setText(image.getName());
        }

        showDatePickerDialog(holder);

        if (dateNotSetWarning) {
            System.out.println("Date not set warning");
            holder.set_date.setTextColor(Color.RED);
        }

        // check : if user selected to set date for all items then set date and also save in list
        if (DateForAllItems != null){
            dateAndTimeList[position].setDate(DateForAllItems);
            holder.set_date.setText(DateForAllItems.getDayOfMonth() + "-" + getMonthInText(DateForAllItems.getMonthValue()) + "-" + DateForAllItems.getYear());
        }

        if (TimeForAllItems != null){
            dateAndTimeList[position].setTime(TimeForAllItems);
            holder.set_time.setText(TimeForAllItems.getHour() + " : " + TimeForAllItems.getMinute());
        }

        holder.set_time.setOnClickListener(v -> {
            showTimePickerDialog(holder);
        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView set_date;
        private final TextView lock_file_title;
        private final TextView set_time;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.lock_file_imageview);
            set_date = itemView.findViewById(R.id.set_date_lock_file);
            lock_file_title = itemView.findViewById(R.id.lock_file_title);
            set_time = itemView.findViewById(R.id.set_time_lock_file);
        }
    }

    private void showDatePickerDialog(myViewHolder holder){
        // show date picker dialog on click of set date text view
        holder.set_date.setOnClickListener(v ->{
            datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                dateAndTimeList[holder.getAdapterPosition()].setDate(LocalDate.of(year, month, dayOfMonth));
                holder.set_date.setText(dayOfMonth + "-" + getMonthInText(month) + "-" + year);
            }, YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.show();
        });
    }

    // Time picker dialog
    private void showTimePickerDialog(myViewHolder holder){
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTimeList[holder.getAdapterPosition()].setTime(LocalTime.of(hourOfDay, minute));
                holder.set_time.setText(hourOfDay + " : " + minute);
            }
        },12, 0, true );
        timePickerDialog.show();
    }

    public void setDateForAllItems(LocalDate localDate){
        TimeForAllItems = null;
        DateForAllItems = localDate;
    }

    public DateAndTime[] getDateAndTimeList(){
        return dateAndTimeList;
    }

    public void setTimeForAllItems(LocalTime timeForAllItems) {
        this.DateForAllItems = null;
        TimeForAllItems = timeForAllItems;
    }

    public void setDateNotSetWarning(boolean b){
        this.dateNotSetWarning = b;
    }



    private String getMonthInText(int month){
        switch (month){
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return "";
    }
}
