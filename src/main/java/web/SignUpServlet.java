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

import beans.BenutzerBean;
import beans.ValidationBean;
import entities.Benutzer;
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
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Servlet für die Registrierungsseite. Hier kann sich ein neuer Benutzer
 * registrieren. Anschließend wird der auf die Startseite weitergeleitet.
 */
@WebServlet(urlPatterns = {"/signup/"})
public class SignUpServlet extends HttpServlet {

    @EJB
    ValidationBean validationBean;

    @EJB
    BenutzerBean userBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //durch edit wird das 2. Passwortfeld freigeschaltet
        request.setAttribute("edit", false);

        // Anfrage an dazugerhörige JSP weiterleiten, der Nutzer kann dort seine Daten zur Registrierung eingeben
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/signup.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("signup_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben aus WEB-INF/login/signup.jsp auslesen
        request.setCharacterEncoding("utf-8");
        List<String> error = new ArrayList<>();

        String username = request.getParameter("signup_username");
        String password1 = request.getParameter("signup_password1");
        String password2 = request.getParameter("signup_password2");
        String name = request.getParameter("signup_name");
        String[] name_array = name.split(" ");
        String vorname = "";
        String nachname = "";
        if (name_array.length < 2) {
            error.add("Vor- und Nachname müssen mit einem Leerzeichen getrennt werden.");
        } else {
            vorname = name_array[0];
            for (int i = 1; i < name_array.length - 1; i++) {
                vorname += " " + name_array[i];
            }
            nachname = name_array[name_array.length - 1];
        }
        //Erhalte die anderen Daten.  (Wenn man es wirklich mit einem Feld für Hausnummer und Straße realisieren möchte:
        /*
        String anschrift = request.getParameter("signup_strasse");
        String[] anschrift_array = anschrift.split(" ");
        String strasse = "";
        String hausnummer = "";
        if (anschrift_array.length < 2) {
            error.add("Straße und Hausnummer müssen mit einem Leerzeichen getrennt werden.");
        } else {
            strasse = anschrift_array[0];
            for (int i = 1; i < anschrift_array.length - 1; i++) {
                strasse += " " + anschrift_array[i];
            }
            hausnummer = anschrift_array[anschrift_array.length - 1];
        }*/
        String hausnummer = request.getParameter("signup_hausnummer");
        String strasse = request.getParameter("signup_strasse");        
        String postleitzahl = request.getParameter("signup_postleitzahl");
        String ort = request.getParameter("signup_ort");
        String land = request.getParameter("signup_land");
        String eMail = request.getParameter("signup_eMail");
        if (!EmailValidator.getInstance().isValid(eMail)) {
            error.add("Die angegebene E-Mail Adresse ist ungültig");
        }
        String telefonnummer = request.getParameter("signup_telefonnummer");

        // Eingaben prüfen unter Nutzung des Validation Bean
        Benutzer user = new Benutzer(username, password1, vorname, nachname, strasse, hausnummer, postleitzahl, ort, land, eMail, telefonnummer);
        List<String> errors = this.validationBean.validate(user);
        if (!error.equals("")) {
            errors.addAll(error);
        }
        this.validationBean.validate(user.getPasswort(), errors);

        if (password1 != null && password2 != null && !password1.equals(password2)) {
            errors.add("Die Passwörter müssen übereinstimmen.");
        }

        // Neuen Benutzer anlegen
        if (errors.isEmpty()) {
            try {
                this.userBean.signup(username, password1, vorname, nachname, strasse, hausnummer, postleitzahl, ort, land, eMail, telefonnummer);
            } catch (BenutzerBean.UserAlreadyExistsException ex) {
                errors.add(ex.getMessage());
            }
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            request.login(username, password1);
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen, Daten in Input-Feldern lassen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("signup_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

}
