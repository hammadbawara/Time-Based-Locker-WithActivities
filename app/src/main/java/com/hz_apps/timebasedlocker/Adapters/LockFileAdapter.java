package com.hz_apps.timebasedlocker.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class LockFileAdapter extends RecyclerView.Adapter<LockFileAdapter.myViewHolder> {

    private final Context context;
    // List of images that will shown to user
    private final ArrayList<File> imagesList;
    private final int YEAR, MONTH, DAY_OF_MONTH;
    // This is the default colo of TextView of "Select Date".
    private final int defaultTextViewColor;
    // This is the array in which all dates will store that user select
    public DateAndTime[] dateAndTimeList;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    // If this true then it will set button red to show warning that date is not selelcted
    private boolean dateNotSetWarning = false;
    private LocalTime TimeForAllItems;


    public LockFileAdapter(Context context, ArrayList<File> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        this.dateAndTimeList = new DateAndTime[imagesList.size()];
        // Initializing all items in dateAndTimeList. So we do not get null Pointer exception.
        for (int i = 0; i < imagesList.size(); i++) {
            dateAndTimeList[i] = new DateAndTime();
        }
        // TODO: This color does not change with theme
        defaultTextViewColor = context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary);
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
        holder.lock_file_title.setText(image.getName());

        showDatePickerDialog(holder);

        setDateOnItem(holder);
        setTimeOnItem(holder);

        holder.set_time.setOnClickListener(v -> showTimePickerDialog(holder));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    private void showDatePickerDialog(myViewHolder holder) {
        // show date picker dialog on click of set date text view
        holder.set_date.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                dateAndTimeList[holder.getBindingAdapterPosition()].setDate(LocalDate.of(year, month+1, dayOfMonth));
                this.setDateOnItem(holder);
            }, YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    // Time picker dialog
    private void showTimePickerDialog(myViewHolder holder) {
        timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            // storing time in dateAndTimeList
            dateAndTimeList[holder.getBindingAdapterPosition()].setTime(LocalTime.of(hourOfDay, minute));
            // setting time in textView
            setTimeOnItem(holder);
        }, 12, 0, true);
        timePickerDialog.show();
    }

    public DateAndTime[] getDateAndTimeList() {
        return dateAndTimeList;
    }

    private void setDateOnItem(myViewHolder holder){
        LocalDate date = dateAndTimeList[holder.getBindingAdapterPosition()].getDate();
        holder.set_date.setTextColor(defaultTextViewColor);
        if (date == null){
            holder.set_date.setText(context.getString(R.string.set_date));
        }else {
            holder.set_date.setText(date.toString());
        }
    }

    private void setTimeOnItem(myViewHolder holder){
        LocalTime time = dateAndTimeList[holder.getBindingAdapterPosition()].getTime();
        if (time == null){
            holder.set_time.setText(context.getString(R.string.set_time));
        }else {
            holder.set_time.setText(time.toString());
        }
    }

    public void setDateOnAllItems(LocalDate date){
        for (int i=0; i<dateAndTimeList.length; i++) {
            dateAndTimeList[i].setDate(date);
            notifyItemChanged(i);
        }
    }

    public void setTimeOnAllItems(LocalTime time){
        for (int i=0; i<dateAndTimeList.length; i++) {
            dateAndTimeList[i].setTime(time);
            notifyItemChanged(i);
        }
    }

    private String getMonthInText(int month) {
        switch (month) {
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
}
