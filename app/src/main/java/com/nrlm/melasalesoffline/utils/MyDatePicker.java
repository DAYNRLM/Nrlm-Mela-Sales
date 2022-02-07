package com.nrlm.melasalesoffline.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nrlm.melasalesoffline.intrface.SetDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDatePicker extends DialogFragment {
    SetDate  setDate =null;
    DateFactory dateFactory;

    Context mContext;
    AppUtility appUtility;
    AppSharedPreferences appSharedPreferences;
    long starDateTime,endDateTime;
    public String date;

    public MyDatePicker(Context context,SetDate setDate) {
        this.mContext = context;
        this.setDate =setDate;
        appUtility =AppUtility.getInstance();
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(context);
        dateFactory = DateFactory.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(appSharedPreferences.getMelaFrom());
            starDateTime = d.getTime();

            /**this date used for mela end date*****/
            /*Date d2 = sdf.parse(appSharedPreferences.getMelaTo());
            endDateTime = d2.getTime();*/

            Date d2 = sdf.parse(dateFactory.changeDateValue(dateFactory.getTodayDate()));
            endDateTime = d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            appUtility.showLog("date dialog Execption:- "+e,MyDatePicker.class);
        }

        DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(starDateTime);
        datePickerDialog.getDatePicker().setMaxDate(endDateTime);
        return datePickerDialog;
    }
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                  //  date = ""+ view.getDayOfMonth()   + "-" + (view.getMonth()+1) + "-" +view.getYear();
                    date = ""+ view.getYear()  + "-" + (view.getMonth()+1) + "-" +view.getDayOfMonth();
                    getDate();

                }
            };

    public void getDate(){
        setDate.notifyDate("date",date);
    }
}
