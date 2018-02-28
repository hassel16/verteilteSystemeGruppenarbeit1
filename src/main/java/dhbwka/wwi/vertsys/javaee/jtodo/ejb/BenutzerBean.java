/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jtodo.ejb;

import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Benutzer;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Task;
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
     * @throws UserBean.UserAlreadyExistsException
     */
    public void signup(String benutzername, String passwort) throws UserAlreadyExistsException {
        if (em.find(Benutzer.class, benutzername) != null) {
            throw new UserAlreadyExistsException("Der Benutzername $B ist bereits vergeben.".replace("$B", benutzername));
        }

        Benutzer benutzer = new Benutzer(benutzername, passwort);
         em.persist(benutzer);
    }

    /**
     *
     * @param benutzername
     * @param altPasswort
     * @param neuPasswort
     * @throws UserBean.InvalidCredentialsException
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

}
