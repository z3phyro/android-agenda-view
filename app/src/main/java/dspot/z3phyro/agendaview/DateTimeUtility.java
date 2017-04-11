package dspot.z3phyro.agendaview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maykel on 30/11/2016.
 */

public class DateTimeUtility {
    Calendar cal = Calendar.getInstance();

    static DateTimeUtility Instance;

    public static DateTimeUtility getInstance(){
        if (Instance == null)
            Instance = new DateTimeUtility();

        return Instance;
    }

    public  long differenceInMilliseconds(Date firstDate, Date secondDate){
        long diff = firstDate.getTime() - secondDate.getTime();

        return Math.abs(diff);
    }

    public  long differenceInDays(Date firstDate, Date secondDate){
        long diff = differenceInMilliseconds(firstDate, secondDate);
        return diff / (24 * 60 * 60 * 1000);
    }

    public  long differenceInMinutes(Date firstDate, Date secondDate){
        long diff = differenceInMilliseconds(firstDate, secondDate);

        return diff == 0 ? 0 : diff / (60 * 1000);
    }

    public  Date setHourMinuteToDate(Date date, int hour, int minutes){
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public  boolean compareDatesByMinutePrecision(Date date1, Date date2){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        String Date1 = df.format(date1);
        String Date2 = df.format(date2);

        return Date1.equals(Date2);
    }

    public  boolean compareDatesDay(Date date1, Date date2){
        return getDayMonthYearString(date1).equals(getDayMonthYearString(date2));
    }

    public  int getMonth(Date date){
        return date.getMonth();
    }

    public  int getYear(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");

        String year = df.format(date);

        return Integer.parseInt(year);
    }

    public  int getDay(Date date){
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public  String getMonthYearString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");

        return df.format(date);
    }

    public  String getDayMonthYearString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        return df.format(date);
    }

    public  Date setMonth(Date date, int month){
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), month, 1);

        return cal.getTime();
    }

    public  int getHour(Date date) {
        cal.setTime(date);

        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public  Date addDays(Date date, int j) {
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, j);

        return  cal.getTime();
    }

    public  int getMinute(Date date) {
        cal.setTime(date);

        return cal.get(Calendar.MINUTE);
    }

    public  Date setDayMonthYear(Date date, int dayOfMonth,int monthOfYear, int year) {
        cal.setTime(date);
        cal.set(year,monthOfYear,dayOfMonth);

        return cal.getTime();
    }

    public  Date addMinutes(Date date, int minutes) {
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);

        return cal.getTime();
    }

    public  Date setSecondMinute(Date date, int minute,int second) {
        cal.setTime(date);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTime();
    }

    public  Date addSeconds(Date date, int seconds) {
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public  String getMinuteSecondString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");

        return df.format(date);
    }

    public  String getMinuteString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("mm");

        return df.format(date);
    }

    public  String getSecondString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("ss");

        return df.format(date);
    }
}
