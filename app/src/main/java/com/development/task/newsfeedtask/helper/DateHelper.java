package com.development.task.newsfeedtask.helper;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class DateHelper {


    public static String convertToSpecificFormat(String dateToBeConverted) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
        String convertedDateStr ="";
        try {
            date = dateFormat.parse(dateToBeConverted);
            DateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
            convertedDateStr = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDateStr;
    }
}
