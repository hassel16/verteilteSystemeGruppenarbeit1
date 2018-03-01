/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Kategorie;
import entities.Anzeige;
import entities.ArtDerAnzeige;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author FSche
 */
@Stateless
public class AnzeigeBean  extends EntityBean<Anzeige, Long> { 
   
    public AnzeigeBean() {
        super(Anzeige.class);
    }
    
    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<Anzeige> findByUsername(String benutzername) {
        return em.createQuery("SELECT t FROM Anzeige t WHERE t.benutzer.benutzername = :benutzername ORDER BY t.einstellungsdatum")
                 .setParameter("benutzername", benutzername)
                 .getResultList();
    }
    
    /**
     * Suche nach Aufgaben anhand ihrer Bezeichnung, Kategorie und Status.
     * 
     * Anders als in der Vorlesung behandelt, wird die SELECT-Anfrage hier
     * mit der CriteriaBuilder-API vollkommen dynamisch erzeugt.
     * 
     * @param search In der Kurzbeschreibung enthaltener Text (optional)
     * @param category Kategorie (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Aufgaben
     */
    public List<Anzeige> search(String search, Kategorie kategorie, ArtDerAnzeige art) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Task t
        CriteriaQuery<Anzeige> query = cb.createQuery(Anzeige.class);
        Root<Anzeige> from = query.from(Anzeige.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.asc(from.get("einstellungsdatum")), cb.asc(from.get("einstellungszeit")));
        
        // WHERE t.shortText LIKE :search
        if (search != null && !search.trim().isEmpty()) {
            query.where(cb.like(from.get("titel"), "%" + search + "%"));
        }
        
        // WHERE t.category = :category
        if (kategorie != null) {
            query.where(cb.equal(from.get("kategorie"), kategorie));
        }
        
        // WHERE t.status = :status
        if (art != null) {
            query.where(cb.equal(from.get("art"), art));
        }
        
        return em.createQuery(query).getResultList();
    }
    
}
