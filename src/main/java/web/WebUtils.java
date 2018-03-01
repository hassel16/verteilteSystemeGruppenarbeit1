/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package web;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * Statische Hilfsmethoden
 */
public class WebUtils {

    //Weile Java 8 API
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Stellt sicher, dass einer URL der Kontextpfad der Anwendung vorangestellt
     * ist. Denn sonst ruft man aus Versehen Seiten auf, die nicht zur eigenen
     * Webanwendung gehören.
     *
     * @param request HttpRequest-Objekt
     * @param url Die aufzurufende URL
     * @return Die vollständige URL
     */
    public static String appUrl(HttpServletRequest request, String url) {
        return request.getContextPath() + url;
    }

    /**
     * Formatiert ein Datum für die Ausgabe, z.B. 31.12.9999
     *
     * @param date Datum
     * @return String für die Ausgabe
     */
    public static String formatDate(LocalDate date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Formatiert eine Uhrzeit für die Ausgabe, z.B. 11:50:00
     *
     * @param time Uhrzeit
     * @return String für die Ausgabe
     */
    public static String formatTime(LocalTime time) {
        return TIME_FORMAT.format(time);
    }

    /**
     * Erzeugt ein Datumsobjekt aus dem übergebenen String, z.B. 03.06.1986
     *
     * @param input Eingegebener String
     * @return Datumsobjekt oder null bei einem Fehler
     */
    public static LocalDate parseDate(String input) {
        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT);
            return date;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Erzeugt ein Uhrzeitobjekt aus dem übergebenen String, z.B. 09:20:00
     *
     * @param input Eingegebener String
     * @return Uhrzeitobjekt oder null bei einem Fehler
     */
    public static LocalTime parseTime(String input) {
        try {
            LocalTime time = LocalTime.parse(input, TIME_FORMAT);
            return time;
        } catch (Exception ex) {
            return null;
        }
    }
        
    public String formatDouble(Double d) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.GERMANY));
        return df.format(d);
    }
}
