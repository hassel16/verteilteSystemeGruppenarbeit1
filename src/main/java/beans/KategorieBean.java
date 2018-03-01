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

import entities.Anzeige;
import entities.Kategorie;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Kategorien.
 */
@Stateless
@RolesAllowed("youbuy-app-user")
public class KategorieBean extends EntityBean<Kategorie, Long> {

    public KategorieBean() {
        super(Kategorie.class);
    }

    /**
     * Auslesen aller Kategorien, alphabetisch sortiert.
     *
     * @return Liste mit allen Kategorien
     */
    public List<Kategorie> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Kategorie c ORDER BY c.name").getResultList();
    }

    public List<Kategorie> findbyName(String name) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // SELECT t FROM Task t
        CriteriaQuery<Kategorie> query = cb.createQuery(Kategorie.class);
        Root<Kategorie> from = query.from(Kategorie.class);
        query.select(from);

        // WHERE t.status = :status
        if (name != null) {
            query.where(cb.equal(from.get("name"), name));
        }

        return em.createQuery(query).getResultList();
    }


    public Kategorie saveNewKategorie(Kategorie nKateogorie) throws KategorieNameAlreadyExistsException{
        if (this.findbyName(nKateogorie.getName()).size()==1) {
            throw new KategorieNameAlreadyExistsException("Kategoriename existiert schon");
        }
        return super.saveNew(nKateogorie);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Eigene Exceptions">
    
    /**
     * Fehler: Der Name ist bereits vergeben
     */
    public class KategorieNameAlreadyExistsException extends Exception {
        public KategorieNameAlreadyExistsException(String message) {
            super(message);
        }
    }
    //</editor-fold>
}
