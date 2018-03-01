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

import dhbwka.wwi.vertsys.javaee.youbuy.ejb.*;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.AnzeigeStatus;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.AnzeigenArt;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.PreisArt;

import java.io.IOException;
import java.time.LocalDateTime;
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
@WebServlet(urlPatterns = "/app/anzeige/*")
public class AnzeigeEditServlet extends HttpServlet {

    @EJB
    AnzeigeBean anzeigeBean;

    @EJB
    KategorieBean kategorieBean;

    @EJB
    BenutzerBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("kategorien", this.kategorieBean.findAll());
        request.setAttribute("preisarten", PreisArt.values());
        request.setAttribute("anzeigenarten", AnzeigenArt.values());
        request.setAttribute("anzeigenstatuse", AnzeigeStatus.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Anzeige anzeige = this.getRequestedAnzeige(request);
        request.setAttribute("edit", anzeige.getId() != 0);
                                
        if (session.getAttribute("anzeige_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("anzeige_form", this.createAnzeigeForm(anzeige));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_edit.jsp").forward(request, response);

        session.removeAttribute("anzeige_form");
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
                this.saveAnzeige(request, response);
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
    private void saveAnzeige(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String anzeige_category = request.getParameter("anzeige_category");
        String anzeige_status = request.getParameter("anzeige_status");
        String anzeige_art = request.getParameter("anzeige_art");
        String anzeige_preis = request.getParameter("anzeige_preis");
        String anzeige_preisart = request.getParameter("anzeige_preisart");
        String anzeige_titel = request.getParameter("anzeige_titel");
        String anzeige_beschreibung = request.getParameter("anzeige_beschreibung");

        //Legt aufjedenfall CreateDatetime und besitzer fest
        Anzeige anzeige = this.getRequestedAnzeige(request);

        anzeige.setTitel(anzeige_titel);
        anzeige.setBeschreibung(anzeige_beschreibung);

        if (anzeige_category != null && !anzeige_category.trim().isEmpty()) {
            try {
                anzeige.setKategorie(this.kategorieBean.findById(Long.parseLong(anzeige_category)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }


        try {
            anzeige.setPreisVorstellung(Double.parseDouble(anzeige_preis));
        } catch (IllegalArgumentException ex) {
            errors.add("Falsches Preisformat");
        }

        try {
            anzeige.setAnzeigenArt(AnzeigenArt.valueOf(anzeige_art));
        } catch (IllegalArgumentException ex) {
            errors.add("Die Ausgewählt Anzeigen Art ist nicht vorhanden.");
        }

        try {
            anzeige.setPreisArt(PreisArt.valueOf(anzeige_preisart));
        } catch (IllegalArgumentException ex) {
            errors.add("Die Ausgewählt Anzeigen Art ist nicht vorhanden.");
        }

        try {
            anzeige.setAnzeigeStatus(AnzeigeStatus.valueOf(anzeige_status));
        } catch (IllegalArgumentException ex) {
            errors.add("Die Ausgewählt Anzeigen Art ist nicht vorhanden.");
        }


        this.validationBean.validate(anzeige, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.anzeigeBean.update(anzeige);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeige/"));
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
        Anzeige anzeige = this.getRequestedAnzeige(request);
        this.anzeigeBean.delete(anzeige);

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
    private Anzeige getRequestedAnzeige(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Anzeige anzeige = new Anzeige();
        anzeige.setBesitzer(this.userBean.getCurrentBenutzer());
        anzeige.setCreationDateTime(LocalDateTime.now());

        // ID aus der URL herausschneiden
        String anzeigeId = request.getPathInfo();

        if (anzeigeId == null) {
            anzeigeId = "";
        }

        anzeigeId = anzeigeId.substring(1);

        if (anzeigeId.endsWith("/")) {
            anzeigeId = anzeigeId.substring(0, anzeigeId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            anzeige = this.anzeigeBean.findById(Long.parseLong(anzeigeId));
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
     * @param anzeige Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createAnzeigeForm(Anzeige anzeige) {
        Map<String, String[]> values = new HashMap<>();

        values.put("anzeige_owner", new String[]{
            anzeige.getBesitzer().getBenutzername()
        });

        if (anzeige.getKategorie() != null) {
            values.put("anzeige_kategorie", new String[]{
                anzeige.getKategorie().toString()
            });
        }

        values.put("task_due_datetime", new String[]{
            WebUtils.formatDateTime(anzeige.getCreationDateTime())
        });
        values.put("anzeige_preis", new String[]{
                String.valueOf(anzeige.getPreisVorstellung())
        });

        values.put("anzeige_preisart", new String[]{
                anzeige.getPreisArt().toString()
        });
        values.put("anzeige_art", new String[]{
                anzeige.getAnzeigenArt().toString()
        });

        values.put("anzeige_status", new String[]{
                anzeige.getAnzeigeStatus().toString()
        });

        values.put("anzeige_titel", new String[]{
            anzeige.getTitel()
        });

        values.put("anzeige_beschreibung", new String[]{
            anzeige.getBeschreibung()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
