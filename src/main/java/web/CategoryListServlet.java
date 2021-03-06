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
import beans.KategorieBean;
import beans.ValidationBean;
import entities.Anzeige;
import entities.Kategorie;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anzeigen und Bearbeiten der Kategorien. Die Seite besitzt ein
 Formular, mit dem ein neue Kategorie angelegt werden kann, sowie eine Liste,
 die zum Löschen der Kategorien verwendet werden kann.
 */
@WebServlet(urlPatterns = {"/app/categories/"})
public class CategoryListServlet extends HttpServlet {

    @EJB
    KategorieBean categoryBean;
    
    @EJB
    AnzeigeBean taskBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alle vorhandenen Kategorien ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/app/category_list.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("categories_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");
        
        //Gewünschte Aktion aus URL-Parameter holen
        String action = request.getParameter("action");

        //Wenn action noch null ist "" zuweisen damit switch geht
        if (action == null) {
            action = "";
        }

        //Richtige Aktion entsprechend action durchführen und request und response weiterleiten
        switch (action) {
            case "create":
                this.createCategory(request, response);
                break;
            case "delete":
                this.deleteCategories(request, response);
                break;
            default:
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue Kategorie anlegen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        String name = request.getParameter("name");

        //Kategorie instanzieren
        Kategorie category = new Kategorie(name);
        List<String> errors = this.validationBean.validate(category);

        // Neue Kategorie anlegen
        if (errors.isEmpty()) {
            try {
                 this.categoryBean.saveNewKategorie(category);
            } catch (KategorieBean.KategorieNameAlreadyExistsException ex) {
                errors.add(ex.getMessage());
            }
            
        }

        // Browser auffordern, die Seite neuzuladen
        if (!errors.isEmpty()) {
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("categories_form", formValues);
        }

        response.sendRedirect(request.getRequestURI());
    }

    /**
     * Aufgerufen in doPost(): Markierte Kategorien löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Markierte Kategorie IDs auslesen
        String[] categoryIds = request.getParameterValues("category");

        if (categoryIds == null) {
            categoryIds = new String[0];
        }

        // Kategorien löschen
        for (String categoryId : categoryIds) {
            // Zu löschende Kategorie ermitteln
            Kategorie kategorie;

            try {
                kategorie = this.categoryBean.findById(Long.parseLong(categoryId));
            } catch (NumberFormatException ex) {
                continue;
            }
            
            if (kategorie == null) {
                continue;
            }
            
            // Bei allen betroffenen Aufgaben, den Bezug zur Kategorie aufheben
            kategorie.getAnzeigen().forEach((Anzeige anzeige) -> {
                anzeige.setKategorie(null);
                this.taskBean.update(anzeige);
            });
            
            // Und weg damit
            this.categoryBean.delete(kategorie);
        }
        
        // Browser auffordern, die Seite neuzuladen
        response.sendRedirect(request.getRequestURI());
    }

}
