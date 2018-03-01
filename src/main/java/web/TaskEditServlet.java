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
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", ArtDerAnzeige.values());
        request.setAttribute("values", ArtDesPreises.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Anzeige task = this.getRequestedTask(request);
        request.setAttribute("edit", task.getId() != 0);
        request.setAttribute("user", task.getBenutzer());
        request.setAttribute("other_user", !userBean.getCurrentUser().getBenutzername().equals(task.getBenutzer().getBenutzername()));

        if (session.getAttribute("task_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("task_form", this.createTaskForm(task));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/task_edit.jsp").forward(request, response);

        session.removeAttribute("task_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

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
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
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

        String taskCategory = request.getParameter("task_category");
        String taskStatus = request.getParameter("task_status");
        String taskShortText = request.getParameter("task_short_text");
        String taskLongText = request.getParameter("task_long_text");
        String taskValue = request.getParameter("task_value");
        String taskValueType = request.getParameter("task_value_type");

        Anzeige task = this.getRequestedTask(request);

        if (taskCategory != null && !taskCategory.trim().isEmpty()) {
            try {
                task.setKategorie(this.categoryBean.findById(Long.parseLong(taskCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        try {
            task.setArtDesPreises(ArtDesPreises.valueOf(taskValueType));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art des Preises ist nicht vorhanden.");
        }
        if (taskValue != null && !taskValue.trim().isEmpty()) {
            try {
                task.setPreisvorstellung(Integer.parseInt(taskValue));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        try {
            task.setArt(ArtDerAnzeige.valueOf(taskStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art ist nicht vorhanden.");
        }

        task.setTitel(taskShortText);
        task.setBeschreibung(taskLongText);

        this.validationBean.validate(task, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.anzeigeBean.update(task);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
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
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Anzeige getRequestedTask(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Anzeige anzeige = new Anzeige();
        anzeige.setBenutzer(this.userBean.getCurrentUser());
        anzeige.setEinstellungsdatum(LocalDate.now());
        anzeige.setEinstellungszeit(LocalTime.now());
        anzeige.setArt(ArtDerAnzeige.BIETE);
        anzeige.setArtDesPreises(ArtDesPreises.FESTPREIS);

        // ID aus der URL herausschneiden
        String taskId = request.getPathInfo();

        if (taskId == null) {
            taskId = "";
        }

        taskId = taskId.substring(1);

        if (taskId.endsWith("/")) {
            taskId = taskId.substring(0, taskId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            anzeige = this.anzeigeBean.findById(Long.parseLong(taskId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
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
            task.getPreisvorstellung() + ""
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
