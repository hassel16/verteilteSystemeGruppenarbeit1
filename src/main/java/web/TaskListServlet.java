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
import entities.Anzeige;
import entities.ArtDerAnzeige;
import entities.Kategorie;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die Startseite bzw. jede Seite, die eine Liste der Aufgaben
 * zeigt.
 */
@WebServlet(urlPatterns = {"/app/tasks/"})
public class TaskListServlet extends HttpServlet {

    @EJB
    private KategorieBean categoryBean;
    
    @EJB
    private AnzeigeBean anzeigeBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", ArtDerAnzeige.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchCategory = request.getParameter("search_category");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Aufgaben suchen
        Kategorie category = null;
        ArtDerAnzeige art = null;

        if (searchCategory != null) {
            try {
                category = this.categoryBean.findById(Long.parseLong(searchCategory));
            } catch (NumberFormatException ex) {
                category = null;
            }
        }

        if (searchStatus != null) {
            try {
                art = ArtDerAnzeige.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                art = null;
            }

        }

        List<Anzeige> anzeige = this.anzeigeBean.search(searchText, category, art);
        request.setAttribute("tasks", anzeige);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/task_list.jsp").forward(request, response);
    }
}
