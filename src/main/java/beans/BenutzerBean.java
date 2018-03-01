/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package beans;

import entities.Benutzer;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Spezielle EJB zum Anlegen eines Benutzers und Aktualisierung des Passworts.
 */
@Stateless
public class BenutzerBean extends EntityBean<Benutzer, String> {

    @PersistenceContext
    EntityManager em;

    @Resource
    EJBContext ctx;

    public BenutzerBean() {
        super(Benutzer.class);
    }

    /**
     * Gibt das Datenbankobjekt des aktuell eingeloggten Benutzers zurück,
     *
     * @return Eingeloggter Benutzer oder null
     */
    public Benutzer getCurrentUser() {
        return this.em.find(Benutzer.class, this.ctx.getCallerPrincipal().getName());
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Methoden zum Anlegen von Usern">
    
    /**
     * Speichert User in Usertabelle und fügt den anglegten User der Gruppe zu
     * Wenn Benutzername schon existiert wirft er Exeption
     *
     * @param benutzername
     * @param password
     * @param vorname
     * @param nachname
     * @param strasse
     * @param hausnummer
     * @param telefonnummer
     * @param postleitzahl
     * @param land
     * @param ort
     * @param eMail
     * @throws BenutzerBean.UserAlreadyExistsException
     */
    public void signup(String benutzername, String password, String vorname, String nachname, String strasse, String hausnummer, String postleitzahl, String ort, String land, String eMail, String telefonnummer) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, benutzername) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", benutzername));
        }

        Benutzer user = new Benutzer(benutzername, password, vorname, nachname, strasse, hausnummer, postleitzahl, ort, land, eMail, telefonnummer);
        user.addToGroup("youbuy-app-user");
        em.persist(user);
    }
    /** Speichert User in Usertabelle und fügt den anglegten User der Gruppe zu
     * Wenn Benutzername schon existiert wirft er Exeption
     * @param user
     * @throws BenutzerBean.UserAlreadyExistsException
     * 
     */
    public void signup(Benutzer user) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, user.getBenutzername()) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", user.getBenutzername()));
        }
        user.addToGroup("youbuy-app-user");
        em.persist(user);
    }
    //</editor-fold>
   
    /**
     * Verändert den Bentuzer ohne zu persistieren
     * @param benutzername
     * @param password
     * @param oldPassword
     * @param vorname
     * @param nachname
     * @param strasse
     * @param hausnummer
     * @param postleitzahl
     * @param ort
     * @param land
     * @param eMail
     * @param telefonnummer
     * @throws InvalidCredentialsException
     */
    public void changeData(String benutzername, String password, String oldPassword, String vorname, String nachname, String strasse, String hausnummer, String postleitzahl, String ort, String land, String eMail, String telefonnummer) throws InvalidCredentialsException {

        Benutzer user = findById(benutzername);
        user.setVorname(vorname);
        user.setNachname(nachname);
        user.setStrasse(strasse);
        user.setHausnummer(hausnummer);
        user.setPostleitzahl(postleitzahl);
        user.setOrt(ort);
        user.setLand(land);
        user.setEmail(eMail);
        user.setTelefonnummer(telefonnummer);
        if (!user.checkPassword(oldPassword)) {
            throw new InvalidCredentialsException("Passwort ist falsch.");
        }
        if (password != null) {
            user.setPassword(password);
        }
        user.addToGroup("youbuy-app-user");
        update(user);
    }

    /**
     * Passwort ändern (ohne zu speichern)
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     * @throws beans.BenutzerBean.InvalidCredentialsException
     */
    @RolesAllowed("youbuy-app-user")
    public void changePassword(Benutzer user, String oldPassword, String newPassword) throws InvalidCredentialsException {
        if (user == null || !user.checkPassword(oldPassword)) {
            throw new InvalidCredentialsException("Benutzername oder Passwort sind falsch.");
        }
        user.setPassword(newPassword);
    }

    /**
     * Benutzer löschen
     *
     * @param user Zu löschender Benutzer
     */
    @RolesAllowed("youbuy-app-user")
    @Override
    public void delete(Benutzer user) {
        this.em.remove(user);
    }

    /**
     * Benutzer aktualisieren
     *
     * @param user Zu aktualisierender Benutzer
     * @return Gespeicherter Benutzer
     */
    @RolesAllowed("youbuy-app-user")
    @Override
    public Benutzer update(Benutzer user) {
        return em.merge(user);
    }

   //<editor-fold defaultstate="collapsed" desc="Eigene Exceptions">
    
    /**
     * Fehler: Der Benutzername ist bereits vergeben
     */
    public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Fehler: Das übergebene Passwort stimmt nicht mit dem des Benutzers
     * überein
     */
    public class InvalidCredentialsException extends Exception {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
    //</editor-fold>
}
