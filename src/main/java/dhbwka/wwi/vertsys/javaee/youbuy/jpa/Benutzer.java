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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Kategorien, die den Aufgaben zugeordnet werden können.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="Benutzername",length = 30)
    @NotNull(message = "Der Benutzername darf nicht leer sein.")
    @Size(min = 3, max = 20, message = "Der Benutzername muss zwischen drei und 20 Zeichen lang sein.")
    private String benutzername;
    @Column(name="PasswortHash")
    private String passwortHash;
    @Column(name="Vorname")
    @NotNull(message = "Der Vornamername darf nicht leer sein.")
    private String vorname;
    @Column(name="Nacnhame")
    @NotNull(message = "Der Nachname darf nicht leer sein.")
    private String nachname;
    @Column(name="Straße")
    private String straße;
    @Column(name="Hausnummer")
    private String hausnummer;
    @Column(name="Postleitzahl")
    private int plz;
    @Column(name="Ort")
    private String ort;
    @Column(name="Land")
    private String land;
    @Column(name="E-Mail")
    @NotNull(message = "Die E-Mail darf nicht leer sein.")
    private String email;
    @Column(name="Telefonnummer")
        @Size(min = 3, max = 30, message = "Die Telefonnummer muss zwischen drei und 30 Zeichen lang sein.")
    private int telefonnummer;
    
    @OneToMany
    private List<Nachricht> nachrichten = new ArrayList();
    @ManyToMany
    private List<Anzeige> gemerkteAnzeigen = new ArrayList();
    //@OneToMany(mappedBy="besitzer")
    @OneToMany
    private List<Anzeige> veroeffentlichteAnzeigen = new ArrayList();
    
    
    

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Benutzer() {
    }

    public Benutzer(String benutzername, String passwortHash, String vorname, String nachname, String straße, String hausnummer, int plz, String ort, String land, String email, int telefonnummer, String name) {
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
        this.vorname = vorname;
        this.nachname = nachname;
        this.straße = straße;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
        this.email = email;
        this.telefonnummer = telefonnummer;
      
    }

    
    //</editor-fold>

    
    //</editor-fold>
    
    

    public Benutzer(String benutzername, String passwortHash) {
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
    }
    
    public boolean checkPasswort(String passwort){
        return this.passwortHash.equals(this.hashPassword(passwort));
    }
    
    //der Code stammt voll aus dem Projekt jTodo aus der Vorlesung
     private String hashPassword(String password) {
        byte[] hash;

        if (password == null) {
            password = "";
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            hash = "!".getBytes(StandardCharsets.UTF_8);
        }

        BigInteger bigInt = new BigInteger(1, hash);
        return bigInt.toString(16);
     }
     
    
}
