/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.ejb;

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Benutzer;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Task;
import java.util.List;
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
public class BenutzerBean {

    @PersistenceContext
    EntityManager em;
    
    @Resource
    EJBContext ctx;

      public List<Benutzer> findByUsername(String benutzername) {
        return em.createQuery("SELECT b FROM Benutzer b WHERE b.username = :username")
                 .setParameter("username", benutzername)
                 .getResultList();
    }
    /**
     * Gibt das Datenbankobjekt des aktuell eingeloggten Benutzers zurück,
     *
     * @return Eingeloggter Benutzer oder null
     */
    public Benutzer getCurrentBenutzer() {
        return this.em.find(Benutzer.class, this.ctx.getCallerPrincipal().getName());
    }

    /**
     *
     * @param benutzername
     * @param passwort
     * @param vorname
     * @param nachname
     * @param street
     * @param hausnummer
     * @param plz
     * @param ort
     * @param land
     * @param email
     * @param telefonnummer
     * @throws dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean.UserAlreadyExistsException
     */
    public void signup(String benutzername, String passwort,String vorname, String nachname, String street, String hausnummer, int plz, String ort, String land, String email, String telefonnummer) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, benutzername) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", benutzername));
        }

        Benutzer benutzer = new Benutzer(benutzername, passwort,vorname,nachname,street,hausnummer,plz,ort,land,email,telefonnummer);
        em.persist(benutzer);
    }
    
    
     /**
     *
     * @param Benutzer
     * @throws dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean.UserAlreadyExistsException
     */
    public void signup(Benutzer benutzer) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, benutzer.getBenutzername()) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", benutzer.getBenutzername()));
        }

        em.persist(benutzer);
    }
    

    /**
     *
     * @param benutzername
     * @param altPasswort
     * @param neuPasswort
     * @throws dhbwka.wwi.vertsys.javaee.youbuy.ejb.BenutzerBean.InvalidCredentialsException
     */
    @RolesAllowed("youbuy-app-user")
    public void changePassword(String benutzername, String altPasswort, String neuPasswort) throws InvalidCredentialsException {
        Benutzer benutzer = em.find(Benutzer.class, benutzername);

        if (benutzer == null || !benutzer.checkPasswort(altPasswort)) {
            throw new InvalidCredentialsException("Benutzername oder Passwort sind falsch.");
        }
        benutzer.setPasswortHash(neuPasswort);
        em.merge(benutzer);
    }

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
    
     /**
     * Fehler: Die übergebene PLZ ist keine Zahl
     * überein
     */
    public class InvalidPLZException extends IllegalArgumentException {
        public InvalidPLZException(String message) {
            super(message);
        }
    }

}
