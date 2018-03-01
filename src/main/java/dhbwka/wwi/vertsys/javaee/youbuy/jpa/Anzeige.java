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

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.AnzeigeStatus;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.AnzeigenArt;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums.PreisArt;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigenArt art;
    
    @Column(name="Titel")
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 3, max = 25, message = "Der Titel muss zwischen vier und 30 Zeichen lang sein.")
    private String titel;
    
    @Column(name="Beschreibung")
    @Lob
    private String beschreibung;
    
    @Column(name="Erstellungsdatum")
    @NotNull(message="Es muss ein Zeitpunkt der Erstellung festgelegt sein.")
    private LocalDateTime creationDateTime;
    
    @Column(name="Preisvorstellung")
    @NotNull(message = "Die Preisvorstellung darf nicht leer sein.")
    private double preisVorstellung;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private PreisArt preisArt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigeStatus anzeigeStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigenArt anzeigenArt;

    @ManyToMany(mappedBy = "gemerkteAnzeigen", fetch = FetchType.LAZY)
    List<Benutzer> interessierte = new ArrayList<>();
       
    @ManyToOne(fetch=FetchType.EAGER)
    @NotNull(message="Die Anzeige muss einem Besitzer zugeordnet sein.")
    private Benutzer besitzer;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @NotNull(message="Die Anzeige muss eine Kategorie beinhalten.")
    private Kategorie kategorie;
    
    @OneToMany (mappedBy="anzeige")
    private List<Foto> fotos = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Anzeige(long id, AnzeigenArt art, String titel, String beschreibung, LocalDateTime creationDate, double preisVorstellung, PreisArt preisArt,AnzeigenArt anzeigenArt,AnzeigeStatus anzeigeStatus, Kategorie kategorie,Benutzer besitzer) {
        this.id = id;
        this.art = art;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.creationDateTime = creationDate;
        this.preisVorstellung = preisVorstellung;
        this.preisArt = preisArt;
        this.anzeigenArt =anzeigenArt;
        this.anzeigeStatus = anzeigeStatus;
        this.kategorie = kategorie;
        this.besitzer = besitzer;
    }
   
    public Anzeige() {
    }
    //</editor-fold>
}
