/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Kategorien, die den Aufgaben zugeordnet werden können.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Anzeige implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="Id",length = 30)
    @GeneratedValue(generator = "anzeige_ids")
    @TableGenerator(name = "anzeige_ids", initialValue = 0, allocationSize = 50)
    private long id;
    
    @NotNull(message = "Die Art darf nicht leer sein.")
    @Column(name="Art(Angebot oder Gesuch)")
    @Size(min = 3, max = 25, message = "Die Art muss zwischen drei und 30 Zeichen lang sein.")
    private String art;
    
    @Column(name="Titel")
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 3, max = 25, message = "Der Titel muss zwischen vier und 30 Zeichen lang sein.")
    private String titel;
    
    @Column(name="Beschreibung")
    @Lob
    private String beschreibung;
    
    @Column(name="Erstellungsdatum")
    @NotNull(message="Es muss ein Zeitpunkt der Erstellung festgelegt sein.")
    private long creationDate;
    
    @Column(name="Online bis")
    @NotNull (message="Es muss ein Ende der Anzeige festgelegt sein.")
    private long onlineBis;
    
    @Column(name="Preisvorstellung")
    @NotNull(message = "Die Preisvorstellung darf nicht leer sein.")
    private double preisVorstellung;
    
    @Column(name="Art des Preises")
    private String preisArt;
    
    @Column(name="Postleitzahl")
    private int plz;
    
    @Column(name="Ort")
    private String ort;
    
    //@ManyToMany(mappedBy = "gemerkteAnzeigen", fetch = FetchType.LAZY)
    //List<Benutzer> interessierte = new ArrayList<>();
       
    //@ManyToOne(fetch=FetchType.EAGER)
    //@NotNull(message="Die Anzeige muss einem Besitzer zugeordnet sein.")
    //private Benutzer besitzer;
    
    @ManyToMany(fetch=FetchType.EAGER)
    @NotNull(message="Die Anzeige muss eine Kategorie beinhalten.")
    private List<Kategorie> kategorien = new ArrayList<>();
    
    //@OneToMany (mappedBy="anzeige")
    @OneToMany
    private List<Foto> fotos = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">

    public Anzeige(long id, String art, String titel, String beschreibung, long creationDate, long onlineBis, double preisVorstellung, String preisArt, int plz, String ort) {
        this.id = id;
        this.art = art;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.creationDate = creationDate;
        this.onlineBis = onlineBis;
        this.preisVorstellung = preisVorstellung;
        this.preisArt = preisArt;
        this.plz = plz;
        this.ort = ort;
       // this.besitzer = besitzer;
    }
   


    public Anzeige() {
    }

}
