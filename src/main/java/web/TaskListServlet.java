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
import entities.ArtDesPreises;
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
 * Servlet für die Startseite, es zeigt eine Lister der Anzeigen an.
 */
@WebServlet(urlPatterns = {"/app/tasks/"})
public class TaskListServlet extends HttpServlet {

    @EJB
    private KategorieBean categoryBean;

    @EJB
    private AnzeigeBean anzeigeBean;

    @EJB
    BenutzerBean userBean;

    //Die Liste wird nach den wünschen des Nutzers angepasst und an das JSP zur Darstellung übergeben
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien, Statusse, Preisarten für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", ArtDerAnzeige.values());
        request.setAttribute("preisarten", ArtDesPreises.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchCategory = request.getParameter("search_category");
        String searchStatus = request.getParameter("search_status");
        String searchPreisArt = request.getParameter("search_preisart");

        //Definition der Variablen, welche später die Suche einschränken
        Kategorie category = null;
        ArtDerAnzeige art = null;
        ArtDesPreises preisart = null;

        //Prüfen ob eine Kategorie mitgegeben/ausgewählt wurde, wenn nicht wird die Variable "genullt"
        if (searchCategory != null) {
            try {
                category = this.categoryBean.findById(Long.parseLong(searchCategory));
            } catch (NumberFormatException ex) {
                category = null;
            }
        }
        
        //Prüfen ob ein Status mitgegeben/ausgewählt wurde, wenn nicht wird die Variable "genullt"
        if (searchStatus != null) {
            try {
                art = ArtDerAnzeige.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                art = null;
            }
        }
        
        //Prüfen ob es eine Preisart gibt die mitgegeben/ausgewählt wurde, wenn nicht wird die Variable "genullt"
        if (searchPreisArt != null) {
            try {
                preisart = ArtDesPreises.valueOf(searchPreisArt);
            } catch (IllegalArgumentException ex) {
                preisart = null;
            }
        }
        
        //Datenbankanfrage durch das anzeigeBean durchführen
        List<Anzeige> anzeige = this.anzeigeBean.search(searchText, category, art,preisart);
        request.setAttribute("tasks", anzeige);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/task_list.jsp").forward(request, response);
    }
    
    //Hinzufügen einer Anzeige zu den favorisierten Anzeigen.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            // Formulareingaben auslesen
            request.setCharacterEncoding("utf-8");
            String i=request.getParameter("task_id_favorisieren");
            //Die id zu long konvertieren
            long task_id = Long.parseUnsignedLong(i);
            
            //Prüfen ob die Favorisierte Anzeige bereits in den favorisierten Anzeigen enthalten ist. Die Prüfe wird über die Id vollzogen.
            Benutzer benutzer= this.userBean.getCurrentUser();
            List<Anzeige> gemerkte=benutzer.getGemerkteAnzeigen();
            boolean bereitsGemerkt=false;
            //Schleife durch alle Elemente der Liste der favorisierten Anzeigen
            for(int j=0;j<gemerkte.size();j++){
                if(gemerkte.get(j).getId()==task_id){
                   bereitsGemerkt=true;
                    break;
                }
            }//wenn die Anzeige noch nicht gemerkt war, dann wird diese hinzugefügt
            if(!bereitsGemerkt){
                benutzer.getGemerkteAnzeigen().add(this.anzeigeBean.findById(task_id));
                this.userBean.update(benutzer);
            } //wenn die Anzeige bereits hinzugefügt war, wird der Benutzer nicht weiter belästigt.
            
            response.sendRedirect(request.getRequestURI());
        
    }

}
