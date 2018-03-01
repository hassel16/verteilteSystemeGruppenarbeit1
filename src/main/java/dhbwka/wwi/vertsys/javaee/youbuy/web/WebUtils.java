/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.web;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.servlet.http.HttpServletRequest;

/**
 * Statische Hilfsmethoden
 */
public class WebUtils {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    /**
     * Stellt sicher, dass einer URL der Kontextpfad der Anwendung vorangestellt
     * ist. Denn sonst ruft man aus Versehen Seiten auf, die nicht zur eigenen
     * Webanwendung gehören.
     * 
     * @param request HttpRequest-Objekt
     * @param url Die aufzurufende URL
     * @return  Die vollständige URL
     */
    public static String appUrl(HttpServletRequest request, String url) {
        return request.getContextPath() + url;
    }
    
    /**
     * Formatiert ein Datum für die Ausgabe, z.B. 31.12.9999
     * @param date Datum
     * @return String für die Ausgabe
     */
    public static String formatDate(LocalDate date) {
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Formatiert eine Uhrzeit für die Ausgabe, z.B. 11:50:00
     * @param time Uhrzeit
     * @return String für die Ausgabe
     */
    public static String formatTime(LocalTime time) {
        return TIME_FORMAT.format(time);
    }

    /**
     * Formatiert eine DatumUhrzeit für die Ausgabe, z.B. 11:50:00
     * @param time Uhrzeit
     * @return String für die Ausgabe
     */
    public static String formatDateTime(LocalDateTime time) {
        return DATETIME_FORMAT.format(time);
    }
    
    /**
     * Erzeugt ein Datumsobjekt aus dem übergebenen String, z.B. 03.06.1986
     * @param input Eingegebener String
     * @return Datumsobjekt oder null bei einem Fehler
     */
    public static LocalDate parseDate(String input) {
        try {
            LocalDate date = LocalDate.parse(input);
            return date;
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Erzeugt ein Uhrzeitobjekt aus dem übergebenen String, z.B. 09:20:00
     * @param input Eingegebener String
     * @return Uhrzeitobjekt oder null bei einem Fehler
     */
    public static LocalTime parseTime(String input) {
        try {
            LocalTime time = LocalTime.parse(input);
            return time;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Erzeugt ein DatumUhrzeitobjekt aus dem übergebenen String, z.B. 11.12.2001 09:20:00
     * @param input Eingegebener String
     * @return Uhrzeitobjekt oder null bei einem Fehler
     */
    public static LocalDateTime parseDateTime(String input) {
        try {
            LocalDateTime time = LocalDateTime.parse(input);
            return time;
        } catch (Exception ex) {
            return null;
        }
    }

}
