/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author crash
 */
public class DateUtils {
    
    public static int getIntMonth(String monthName) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
            DateTime dateTime = formatter.withLocale(Locale.ENGLISH).parseDateTime(monthName);
            return dateTime.getMonthOfYear();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
