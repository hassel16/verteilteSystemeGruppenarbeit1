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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@WebServlet(urlPatterns = {"/app/edit_user/"})
public class EditUserServlet extends HttpServlet {

    @EJB
    ValidationBean validationBean;

    @EJB
    BenutzerBean userBean;

    //Die Seite aufrufen und somit das Formular bereitgestellt bekommen
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("edit", true);

        if (request.getSession().getAttribute("signup_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("signup_form", this.createUserForm());
        }
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/signup.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("signup_form");
    }

    //Die Registrierung durchführen (Das ausgefüllte Formular wird hier verarbeitet)
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> error = new ArrayList<>();
        
        // Formulareingaben auslesen
        request.setCharacterEncoding("utf-8");
 
        String username = request.getParameter("signup_username");
        String oldpassword = request.getParameter("signup_oldpassword");
        String password1 = request.getParameter("signup_password1");
        String password2 = request.getParameter("signup_password2");
        String name = request.getParameter("signup_name");
        
        //Den Namen in 2 Teilen
        String[] name_array = name.split(" ");
        String vorname = "";
        String nachname = "";
        if (name_array.length < 2) {
            //Das Namensfeld wurde nicht hinreichend ausgefüllt
            error.add("Vor- und Nachname müssen mit einem Leerzeichen getrennt werden.");
        } else {
            //Alle Woerter (Strings) vor dem letzten Leerzeichen werden dem Vornamen zugeordnet 
            vorname = name_array[0];
            for (int i = 1; i < name_array.length - 1; i++) {
                //Dem Vornamen wird das nächste Wort angehängt
                vorname += " " + name_array[i];
            }
            //Der Nachname besteht nur aus dem letzten Wort, der letzten Sequenz
            nachname = name_array[name_array.length - 1];
        }

        String hausnummer = request.getParameter("signup_hausnummer");
        String strasse = request.getParameter("signup_strasse"); 
        /* Hier die Möglichkeit, die Straße und Hausnummer über ein einzelnes Textfeld zu ernmitteln.
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
        }
        */
        String postleitzahl = request.getParameter("signup_postleitzahl");
        String ort = request.getParameter("signup_ort");
        String land = request.getParameter("signup_land");
        String eMail = request.getParameter("signup_eMail");
        
        //Prüfen ob die Email dem Format entspricht, also *@*.*  , wobei * mindestens ein Zeichen ist.
        if (!EmailValidator.getInstance().isValid(eMail)) {
            error.add("Die angegebene E-Mail Adresse ist nicht gültig");
        }
        String telefonnummer = request.getParameter("signup_telefonnummer");
        if(telefonnummer==null)
            telefonnummer="";

        // Eingaben prüfenBenutzer user
        List<String> errors = new ArrayList<>();
        //Hier wird schon ein Nutzer-Objekt angelegt, um das später mit der validationBean validieren zu können.
        Benutzer user = new Benutzer(username, password1, vorname, nachname, strasse, hausnummer, postleitzahl, ort, land, eMail, telefonnummer);

        //Überprüfung der Daten im User
        if (password1 == null || password1.equals("")) {
            password1 = null;
        } else {
            errors.addAll(this.validationBean.validate(user));
        }
        if (!error.equals("")) {
            errors.addAll(error);
        }
        if (password1 != null) {
            this.validationBean.validate(user.getPasswort(), errors);
        }
        
        //Überprfüfung ob die Passwörter übereinstimmen
        if (password1 != null && password2 != null && !password1.equals(password2)) {
            errors.add("Die beiden Passwörter stimmen nicht überein.");
        }

        // Wenn es keine Fehler gab, werden die Daten angepasst.
        if (errors.isEmpty()) {
            try {
                this.userBean.changeData(username, password1, oldpassword, vorname, nachname, strasse, hausnummer, postleitzahl, ort, land, eMail, telefonnummer);
            } catch (BenutzerBean.InvalidCredentialsException ex ) {
                errors.add(ex.getMessage());
            }catch (javax.ejb.EJBException ex ) {
                errors.add("Die Telefonnummer ist nicht korrekt.");
            }
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
            session.setAttribute("signup_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    //Das Formular wird mit dne bisher gespeicherten Daten des Nutzers befüllt.
    private FormValues createUserForm() {
        Benutzer currentUser = userBean.getCurrentUser();
        Map<String, String[]> values = new HashMap<>();

        values.put("signup_username", new String[]{
            currentUser.getBenutzername()
        });

        values.put("signup_name", new String[]{currentUser.getVorname() + " " + currentUser.getNachname()
        });

        values.put("signup_strasse", new String[]{currentUser.getStrasse()
        });
        
        values.put("signup_hausnummer", new String[]{currentUser.getHausnummer()
        });

        values.put("signup_postleitzahl", new String[]{currentUser.getPostleitzahl()
        });

        values.put("signup_ort", new String[]{currentUser.getOrt()
        });

        values.put("signup_land", new String[]{currentUser.getLand()
        });

        values.put("signup_eMail", new String[]{currentUser.getEmail()
        });

        values.put("signup_telefonnummer", new String[]{currentUser.getTelefonnummer()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
