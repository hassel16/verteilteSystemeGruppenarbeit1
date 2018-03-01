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

import beans.AnzeigeBean;
import beans.BenutzerBean;
import beans.KategorieBean;
import beans.ValidationBean;
import entities.Anzeige;
import entities.ArtDerAnzeige;
import entities.ArtDesPreises;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Anzeige.
 */
@WebServlet(urlPatterns = "/app/task/*")
public class TaskEditServlet extends HttpServlet {

    @EJB
    AnzeigeBean anzeigeBean;

    @EJB
    KategorieBean categoryBean;

    @EJB
    BenutzerBean userBean;

    @EJB
    ValidationBean validationBean;

    //Anzeige des Formulars zum Ändern einer Anzeige
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien, Statusse und Preisarten für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", ArtDerAnzeige.values());
        request.setAttribute("values", ArtDesPreises.values());

        HttpSession session = request.getSession();
        //Die Möglichen Werte in den Request stecken, damit es später angezeigt werden kann.
        Anzeige task = this.getRequestedTask(request);
        request.setAttribute("edit", task.getId() != 0);
        request.setAttribute("user", task.getBenutzer());
        request.setAttribute("other_user", !userBean.getCurrentUser().getBenutzername().equals(task.getBenutzer().getBenutzername()));

        // Wenn keine Formulardaten mit fehlerhaften Daten in der Session,
        if (session.getAttribute("task_form") == null) {   
            // dann können die Formulardaten aus dem Datenbankobjekt übernommen werden.
            request.setAttribute("task_form", this.createTaskForm(task));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/task_edit.jsp").forward(request, response);

        session.removeAttribute("task_form");
    }

    //Bearbeitung der Anzeigen auf Wunsch des Nutzers: Löschen, anpassen oder erstellen einer Anzeige. 
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        request.setCharacterEncoding("utf-8");
        // Auslesen der Parameter aus der Request
        String action = request.getParameter("action");

        //Umgehen von einem Nullpointer
        if (action == null) {
            action = "";
        }
        
        //Fallunterscheidung ob das Objekt gelöscht oder verändert (bzw. gespeichert) werden soll.
        switch (action) {
            case "save":
                this.saveTask(request, response);
                break;
            case "delete":
                this.deleteTask(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Anzeige speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        //Formulareingaben auslesen.
        String taskCategory = request.getParameter("task_category");
        String taskStatus = request.getParameter("task_status");
        String taskShortText = request.getParameter("task_short_text");
        String taskLongText = request.getParameter("task_long_text");
        String taskValue = request.getParameter("task_value");
        String taskValueType = request.getParameter("task_value_type");
        
        //Die zu manipulierende Anzeige wird aus der Request direkt ausgelesen, durch die unten implementierte Methode
        Anzeige task = this.getRequestedTask(request);
        //Anpassen der Kategorie, falls diese verändert werden sollte.
        if (taskCategory != null && !taskCategory.trim().isEmpty()) {
            try {
                task.setKategorie(this.categoryBean.findById(Long.parseLong(taskCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }
        
        //Anpassen der Preisart
        try {
            task.setArtDesPreises(ArtDesPreises.valueOf(taskValueType));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art des Preises ist nicht vorhanden.");
        }
        //Anpassen des Preises der Anzeige
        if (taskValue != null && !taskValue.trim().isEmpty()) {
            try {
                taskValue = taskValue.replace(",", "."); 
                task.setPreisvorstellung(Double.parseDouble(taskValue));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }
        
        try {
            task.setArt(ArtDerAnzeige.valueOf(taskStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art ist nicht vorhanden.");
        }
        //Titel und Beschreibung der Anzeige wird übergeben.
        task.setTitel(taskShortText);
        task.setBeschreibung(taskLongText);
        
        //Wenn Eingaben falsch sind, werden diese in "errors" hinzugefügt
        this.validationBean.validate(task, errors);

        // Datensatz speichern, wenn keine Fehler
        if (errors.isEmpty()) {
            this.anzeigeBean.update(task);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite/Übersicht aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            //Fehler in dem Formular anzeigen
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Anzeige löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Anzeige task = this.getRequestedTask(request);
        this.anzeigeBean.delete(task);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
    }

    /**
     * Zu bearbeitende Anzeige aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Anzeige getRequestedTask(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass eine neue Anzeige angelegt werden soll
        Anzeige anzeige = new Anzeige();
        anzeige.setBenutzer(this.userBean.getCurrentUser());
        anzeige.setEinstellungsdatum(LocalDate.now());
        anzeige.setEinstellungszeit(LocalTime.now());
        anzeige.setArt(ArtDerAnzeige.BIETE);
        anzeige.setArtDesPreises(ArtDesPreises.FESTPREIS);

        // ID aus der URL herausschneiden
        String taskId = request.getPathInfo();

            //Nullpointer umgehen
        if (taskId == null) {
            taskId = "";
        }

        taskId = taskId.substring(1);

        if (taskId.endsWith("/")) {
            taskId = taskId.substring(0, taskId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            //Wenn es den Datensatz bereits gab, wird die bestehende Variable durch jenes Objekt ersetzt
            anzeige = this.anzeigeBean.findById(Long.parseLong(taskId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten, macht aber nix, also gehts direkt weiter.
        }
        return anzeige;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createTaskForm(Anzeige task) {
        Map<String, String[]> values = new HashMap<>();

        values.put("task_owner", new String[]{
            task.getBenutzer().getBenutzername()
        });

        if (task.getKategorie() != null) {
            values.put("task_category", new String[]{
                task.getKategorie().toString()
            });
        }

        values.put("task_date", new String[]{
            WebUtils.formatDate(task.getEinstellungsdatum())
        });

        values.put("task_time", new String[]{
            WebUtils.formatTime(task.getEinstellungszeit())
        });

        values.put("task_status", new String[]{
            task.getArt().toString()
        });

        values.put("task_short_text", new String[]{
            task.getTitel()
        });

        values.put("task_long_text", new String[]{
            task.getBeschreibung()
        });

        values.put("task_value_type", new String[]{
            task.getArtDesPreises() + ""
        });

        values.put("task_value", new String[]{
            WebUtils.formatDouble(task.getPreisvorstellung()) + ""
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
