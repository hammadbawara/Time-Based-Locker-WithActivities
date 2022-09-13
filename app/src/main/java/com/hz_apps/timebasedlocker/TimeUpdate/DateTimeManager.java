package com.hz_apps.timebasedlocker.TimeUpdate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.concurrent.Executors;

public class DateTimeManager {

    /*
    {"abbreviation":"GMT",
    "client_ip":"111.119.185.20",
    "datetime":"2022-09-02T10:27:04.393224+00:00",
    "day_of_week":5,
    "day_of_year":245,"dst":false,
    "dst_from":null,
    "dst_offset":0,"dst_until":null,
    "raw_offset":0,
    "timezone":"Atlantic/Reykjavik",
    "unixtime":1662114424,
    "utc_datetime":"2022-09-02T10:27:04.393224+00:00",
    "utc_offset":"+00:00",
    "week_number":35}
     */

    private static DateAndTime dateAndTime  = null;


    private static DateAndTime getTimeFromInternet(){

        DateAndTime dateTime = null;


        try{
            // connecting with server
            URL url = new URL("http://worldtimeapi.org/api/timezone/Atlantic/Reykjavik");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Reading response if connection is successful
            if (connection.getResponseCode() == 200){
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                // Making json object from reponse
                JSONObject json = new JSONObject(reader.readLine());

                // Making OffsetDateTime object
                OffsetDateTime offsetDT = OffsetDateTime.parse(json.getString("datetime"))
                        .withOffsetSameInstant(OffsetDateTime.now().getOffset());
                dateTime = DateAndTime.of(offsetDT.getYear(),
                                        offsetDT.getMonthValue(),
                                        offsetDT.getDayOfMonth(),
                                        offsetDT.getHour(),
                                        offsetDT.getMinute());

            }else{
                System.out.println("Failed to connect. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();

        }catch(Exception e){
            System.out.println("Error " + e);
        }


        return dateTime;
    }

    public static void update(Context context, UpdateTimeListener listener){
        Executors.newSingleThreadExecutor().execute(() ->{

            DateAndTime mDateAndTime = null;

            if (isNetworkAvailable(context)){
                mDateAndTime = getTimeFromInternet();
            }

            if (mDateAndTime != null){
                // updating dateAndTime
                dateAndTime = mDateAndTime;
                //Saving in shared preference
                SharedPreferences preferences = context.getApplicationContext().
                        getSharedPreferences(context.getString(R.string.application_shared_preference), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(context.getString(R.string.saved_time_preference), mDateAndTime.toString());
                editor.apply();

                // triggering time updater
                listener.onUpdateTime(mDateAndTime);
            }
        });
    }

    /**
      - This function get date and time from shared preferences.
     */
    private static void getDTFromSharedPref(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(context.getString(R.string.application_shared_preference), Context.MODE_PRIVATE);
        String sDateAndTime =  preferences.getString(context.getString(R.string.saved_time_preference), "");
        if (!sDateAndTime.equals("")){
            dateAndTime = DateAndTime.parse(sDateAndTime);
        }
    }

    public static DateAndTime getDateAndTime(Context context) {
        if (dateAndTime == null){
            getDTFromSharedPref(context);
        }
        return dateAndTime;
    }

    public interface UpdateTimeListener{
        void onUpdateTime(DateAndTime dateAndTime);
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
