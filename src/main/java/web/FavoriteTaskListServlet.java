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
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
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
@WebServlet(urlPatterns = {"/app/favoritetasks/"})
public class FavoriteTaskListServlet extends HttpServlet {

    @EJB
    private KategorieBean categoryBean;
    
    @EJB
    private AnzeigeBean anzeigeBean;
    
    @EJB
    BenutzerBean userBean;

    //Anzeige einer Liste der favorisierten Anzeigen
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Liste 
        List<Anzeige> anzeige=new ArrayList<>();
        // Die Gemerkten Anzeigen des Nutzers aus der Datenbank auslesen.
        List<Anzeige> gemerkte= this.userBean.getCurrentUser().getGemerkteAnzeigen();
        //Prüfen ob die gemerkten Anzeigen wirklich noch als Daten existieren. Es kann sein, dass eine Anzeige nach dem löschen
        //nicht mehr in der Datenbank vorhanden ist, aber als Java Objekt noch existiert. Damit diese in der Datenbank nicht mehr 
        //vertretenen Anzeigen gelöscht werden, wurde hier der Algorithmus eingeführt:
        for(int i=0; i<gemerkte.size(); i++){
            if(anzeigeBean.findById(gemerkte.get(i).getId())!=null)
                anzeige.add(gemerkte.get(i));
            else
                gemerkte.remove(i);
        }
        
        //Die Anzeigen der Request anhängen
        request.setAttribute("tasks", anzeige);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/favorite_task_list.jsp").forward(request, response);
    }
    
    //Löschen einer Anzeige aus den favorisierten Anzeigen
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
            // Formulareingaben aus WEB-INF/app/favorite_tasks_list.jsp auslesen
            request.setCharacterEncoding("utf-8");
            String i=request.getParameter("task_id_defavorisieren");
            
            //Umwandeln der id ins Format long
            //(Kein Exception Handling, weil die id garantiert eine Nummer ist, stammt ja direkt aus unseren Daten)
            long task_id = Long.parseUnsignedLong(i);
            
            //Die ausgewählte Anzeige aus den favorisierten Anzeigen löschen
            Benutzer benutzer= this.userBean.getCurrentUser();
            benutzer.getGemerkteAnzeigen().remove(this.anzeigeBean.findById(task_id));
            this.userBean.update(benutzer);
            
            //Die Liste der favorisierten Anzeigen ohne die gelöschte Anzeige anzeigen
            response.sendRedirect(request.getRequestURI());
    }
    
}
