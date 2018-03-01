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
import entities.Anzeige;
import entities.Benutzer;
import entities.ArtDerAnzeige;
import entities.Kategorie;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet für die Startseite bzw. jede Seite, die eine Liste der Aufgaben
 * zeigt.
 */
@WebServlet(urlPatterns = {"/app/favoritetasks/"})
public class FavoriteTaskListServlet extends HttpServlet {

    @EJB
    private KategorieBean categoryBean;
    
    @EJB
    private AnzeigeBean anzeigeBean;
    
    @EJB
    BenutzerBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        List<Anzeige> anzeige= this.userBean.getCurrentUser().getGemerkteAnzeigen();
        request.setAttribute("tasks", anzeige);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/favorite_task_list.jsp").forward(request, response);
    }
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben aus WEB-INF/login/signup.jsp auslesen
        
        request.setCharacterEncoding("utf-8");
            String i=request.getParameter("task_id_defavorisieren");
            
            
            long task_id = Long.parseUnsignedLong(i);
            HttpSession session = request.getSession();
            Benutzer benutzer= this.userBean.getCurrentUser();
            benutzer.getGemerkteAnzeigen().remove(this.anzeigeBean.findById(task_id));
            this.userBean.update(benutzer);
            response.sendRedirect(request.getRequestURI());
    }
    
}
