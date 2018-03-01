/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import web.WebUtils;
import java.util.ArrayList;


@Entity
@Data
@EqualsAndHashCode(exclude = {"benutzer", "kategorie"})
public class Anzeige implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "anzeige_ids")
    @TableGenerator(name = "anzeige_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ArtDerAnzeige art;

    @Size(min = 1, max = 64, message = "Der Titel darf nicht leer sein.")
    @NotNull(message = "Der Titel darf nicht leer sein.")
    private String titel;

    @Lob
    private String beschreibung;

    @NotNull(message = "Das Datum darf nicht leer sein.")
    private Date einstellungsdatum;

    @NotNull(message = "Die Zeit darf nicht leer sein.")
    private Time einstellungszeit;

    @Column(scale = 2)
    private long preisvorstellung;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ArtDesPreises artDesPreises;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Die Anzeige muss einem Benutzer zugeordnet werden.")
    private Benutzer benutzer;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy="gemerkteAnzeigen")
    private List<Benutzer> interessierte=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Kategorie kategorie;
    //Für Ausgabe in Tasklist
    public String formatEinstellungsdatum(){
        return WebUtils.formatDate(this.getEinstellungsdatum());
    }
    
     //<editor-fold defaultstate="collapsed" desc="Getter und Setter für LocalDate und LocalTime mit Umrechnung">
    //Darby kann kein LocalDate udn localtime
    public void setEinstellungsdatum(LocalDate date) {
        this.einstellungsdatum = Date.valueOf(date);
    }

    public LocalDate getEinstellungsdatum() {
        return this.einstellungsdatum.toLocalDate();
    }

    public void setEinstellungszeit(LocalTime time) {
        this.einstellungszeit = Time.valueOf(time);
    }

    public LocalTime getEinstellungszeit() {
        return this.einstellungszeit.toLocalTime();
    }
       //</editor-fold>
}
