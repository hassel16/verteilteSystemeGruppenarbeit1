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

import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Category;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Kategorie;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Task;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 *
 * @author METELC
 */
@Stateless
@RolesAllowed("todo-app-user")
public class KategorieBean extends EntityBean<Kategorie, Long>{
    public KategorieBean() {
        super(Kategorie.class);
    }
      public List<Kategorie> findByUsername(String slag) {
        return em.createQuery("SELECT k FROM Kategorie k WHERE k.slag = :slag")
                 .setParameter("slag", slag)
                 .getResultList();
    }
    
    
}
