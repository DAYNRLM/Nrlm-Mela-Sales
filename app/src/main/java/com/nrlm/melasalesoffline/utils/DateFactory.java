package com.nrlm.melasalesoffline.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFactory {


    public static DateFactory dateFactory;
    private Locale locale;
    Context mContext;
    AppUtility appUtility;

    private DateFactory() {
        locale = Locale.US;
        appUtility =AppUtility.getInstance();// set locale
    }

    public static DateFactory getInstance() {
        if (dateFactory == null) {
            dateFactory = new DateFactory();

        }
        return dateFactory;
    }




    public String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }

    public String getTodayDate() {
        String dateoftodayis="";
        try {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            //dateoftodayis = year + "-" + (month+1) + "-" + day;
            dateoftodayis = day+ "-" +(month+1) + "-"+year;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date d = df.parse(dateoftodayis);
            dateoftodayis =df.format(d);

        }catch (Exception e){
            appUtility.showLog("get today date Expection:- "+e,DateFactory.class);
        }
        return dateoftodayis;
    }

    public String getDateInOtherFormate() {
        //SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String dateoftodayis = day + "-" + (month + 1) + "-" + year;
        return dateoftodayis;
    }

    public String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(Calendar.getInstance().getTime());

    }

    public String getPreviousDate(String dateString, int numOfDays, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateString);
        } catch (ParseException e) {
            AppUtility.getInstance().showLog("Date Parse Exception" + e, DateFactory.class);

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -numOfDays);
        Date previousDate = calendar.getTime();
        return sdf.format(previousDate);
    }

    public String getNextDate(String dateString, int noOfDays, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateString);
        } catch (ParseException e) {
            AppUtility.getInstance().showLog("ParseException"+e,DateFactory.class);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, +noOfDays);
        Date previousDate = calendar.getTime();
        return sdf.format(previousDate);
    }

    public long getStartTimeOfTodayInMillis() {
        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("UTC"));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    public long getStartTimeOfGivenDay(long millis) {
        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(millis);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        return day.getTimeInMillis();
    }

    public long getEndTimeOfTodayInMillis() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }

    public long getEndTimeOfGivenDay(long millis) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(millis);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }

    public String changeDateFormat(String date, String oldDateFormat, String newDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(oldDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(newDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    public String changeDateValue(String date) {
        String changedValue = "";
        Locale locale = new Locale("ENGLISH");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
        changedValue = sdf.format(new Date());
        return changedValue;
    }

    public String changeDatePattern(String inputDate) {
        String outputPattern = "dd-MMM-yyyy";
        String inputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    public String changeFormate(String inputDate) {
        String outputPattern = "dd-MM-yyyy";
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }
    public String changeDateValueFordatePicker(String date) {
        String changedValue = "";
        Locale locale = new Locale("ENGLISH");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", locale);
        // Date date1 = new Date(date);
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        changedValue = sdf.format(date1);
        return changedValue;
    }

    public Date getDateFormate(String date){
        Date convertedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            convertedDate = sdf.parse(date);
            sdf.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public String geteDateFromTimeStamp(String evaluationdate) {
        String getdate = "";
        String date[] = evaluationdate.split(" ");
        for (int i = 0; i < date.length; i++) {
            getdate = date[0];
            appUtility.showLog("dateIs" + getdate, DateFactory.class);
        }
        try {
            getdate = changeDateFormat(getdate, "yyyy-MM-dd", "dd-MM-yyyy");
            appUtility.showLog("dateIsAfterChange" + getdate, DateFactory.class);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getdate;
    }

    public String getCountOfDays(Date evaluationDate, Date todayDate) {

        int eYear = 0, eMonth = 0, eDay = 0;

        //chech if today date is greater then evaluation given date and get the celender object
        if (evaluationDate.after(todayDate)) {
            Calendar eCal = Calendar.getInstance();
            eCal.setTime(evaluationDate);
            eYear = eCal.get(Calendar.YEAR);
            eMonth = eCal.get(Calendar.MONTH);
            eDay = eCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar eCal = Calendar.getInstance();
            eCal.setTime(todayDate);
            eYear = eCal.get(Calendar.YEAR);
            eMonth = eCal.get(Calendar.MONTH);
            eDay = eCal.get(Calendar.DAY_OF_MONTH);
        }

        //get the today date calender object
        Calendar tCal = Calendar.getInstance();
        tCal.setTime(todayDate);
        int tYear = tCal.get(Calendar.YEAR);
        int tMonth = tCal.get(Calendar.MONTH);
        int tDay = tCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        //set evaluation date in calender object
        date1.clear();
        date1.set(eYear, eMonth, eDay);

        //set today date in calender object
        date2.clear();
        date2.set(tYear, tMonth, tDay);

        //get the total difference b/w two calender objects
        long diff = date1.getTimeInMillis() - date2.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount );
    }

}
