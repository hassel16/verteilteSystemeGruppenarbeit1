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

import dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean;
import dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean.InvalidPLZException;
import dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean.UserAlreadyExistsException;
import dhbwka.wwi.vertsys.javaee.youbuy.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Benutzer;
import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet für die Registrierungsseite. Hier kann sich ein neuer Benutzer
 * registrieren. Anschließend wird der auf die Startseite weitergeleitet.
 */
@WebServlet(urlPatterns = {"/signup/"})
public class SignUpServlet extends HttpServlet {
    
    @EJB
    ValidationBean validationBean;
            
    @EJB
    BenutzerBean benutzerBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/signup.jsp");
        dispatcher.forward(request, response);
        
        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("signup_form");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Formulareingaben auslesen
        request.setCharacterEncoding("utf-8");
        
        String benutzername = request.getParameter("signup_username");
        String password1 = request.getParameter("signup_password1");
        String password2 = request.getParameter("signup_password2");
        String vorname = request.getParameter("signup_vorname");
        String nachname = request.getParameter("signup_name");
        String streetnumber = request.getParameter("signup_street_number");
        String street = request.getParameter("signup_street");
        String plzcity = request.getParameter("signup_plz_city");
        String ort = request.getParameter("signup_city");
        String land = request.getParameter("signup_land");
        String email = request.getParameter("signup_email");
        String tel = request.getParameter("signup_tel");


        List<String> errors = new ArrayList();
        //Validiert alle Daten und logt ein wenn ok
        InputCheck_Validate_SignUp(errors,benutzername, password1,password2,vorname,nachname,street,streetnumber,plzcity,ort,land,email,tel);

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            //Wenn keine Fehler dann login auf Server
            request.login(benutzername, password1);
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);
            
            HttpSession session = request.getSession();
            session.setAttribute("signup_form", formValues);
            
            response.sendRedirect(request.getRequestURI());
        }
    }
    
     /**
     *  Prüft ob es Fehler gab bei der Eingabe w
     */
       private void InputCheck_Validate_SignUp(List<String> errors, String benutzername, String password1, String password2, String vorname, String nachname, String street, String hausnummer, String plz, String ort, String land, String email, String telefonnummer){
            if (password1 != null && password2 != null && !password1.equals(password2)) {
                errors.add("Die beiden Passwörter stimmen nicht überein.");
            }
            try {
                //Fängt Exception wenn bei plz ein String kommt
                int plzAsInt=Integer.parseInt(plz);
                Benutzer user = new Benutzer(benutzername,password1,vorname,nachname,street,hausnummer,plzAsInt,ort,land,email,telefonnummer);
                errors.addAll(validationBean.validate(user));
                //Neuen Benutzer anlegen
                if (errors.isEmpty()) {
                    try {
                        this.benutzerBean.signup(user);
                    } catch (UserAlreadyExistsException ex) {
                        errors.add(ex.getMessage());
                    }
                }
            } catch (InvalidPLZException ex) {
                errors.add("Die eingegebene Postleitzahl ist keine Zahl.");
            }
        }

}
