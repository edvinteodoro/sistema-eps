/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author edvin
 */
public class DateUtils {

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es"));
        String formattedDate = getCurrentDate().format(formatter);
        return formattedDate;
    }

    public static String getFormatedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es"));
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public static String getDayFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.forLanguageTag("es"));
        String day = date.format(formatter);
        return day;
    }

    public static Integer getYearInt(LocalDate date) {
        return date.getYear();
    }

    public static String getFormatedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(formatter);
        return formattedTime;
    }
    
    public static LocalDateTime getCurrentLocalDateTime(){
        return LocalDateTime.now();
    }
    
    public static String getCurrentLocalDateTimeFormated(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String formattedDateTime = getCurrentLocalDateTime().format(formatter);
        return formattedDateTime;
    }

}
