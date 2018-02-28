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

import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Benutzer;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Task;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author METELC
 */
@Stateless
@RolesAllowed("todo-app-user")
public class AnzeigeBean extends EntityBean<Anzeige, Long> {
    public AnzeigeBean() {
        super(Anzeige.class);
    }
    
    public List<Anzeige> findByBesitzer(Benutzer besitzer) {
        
         CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // SELECT a FROM Anzeige a
        CriteriaQuery<Anzeige> query = cb.createQuery(Anzeige.class);
        Root<Anzeige> from = query.from(Anzeige.class);
        query.select(from);
        // WHERE a.besitzer = :besitzer
        if (besitzer != null) {
            query.where(cb.equal(from.get("besitzer"), besitzer));
        }
        return em.createQuery(query).getResultList();
    }
    
    
   // public List<Anzeige> findByBesitzerBenutzername(String benutzername) {
   //     CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // SELECT b FROM Benutzer b
    //    CriteriaQuery<Benutzer> query = cb.createQuery(Benutzer.class);
    //    Root<Benutzer> from = query.from(Benutzer.class);
    //    query.select(from);
        // WHERE a.besitzer = :besitzer
    //    if (benutzername != null && !benutzername.equaks("")) {
    //        query.where(cb.equal(from.get("bentuzername"), username));
   //     }
   //     return em.createQuery(query).getResultList();
  //  }
}
