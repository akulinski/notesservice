package com.akulinski.notesservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getFormattedDate() throws ParseException {
        String dateString = format.format(new Date());
        System.out.println(dateString);
        return format.parse(dateString);
    }
}
