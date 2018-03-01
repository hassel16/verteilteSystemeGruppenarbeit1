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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "YOUBUY_USER")
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
    @Column(name="Street")
    private String street;
    @Column(name="Hausnummer")
    private String hausnummer;
    @Column(name="Postleitzahl")
    private int plz;
    @Column(name="Ort")
    private String ort;
    @Column(name="Land")
    private String land;
    @Column(name="E-Mail")
    @Pattern(regexp = "^\\w+@\\w+\\..{2,3}(.{2,3})?$")
    @NotNull(message = "Die E-Mail darf nicht leer sein.")
    private String email;
    @Column(name="Telefonnummer")
        @Size(min = 3, max = 30, message = "Die Telefonnummer muss zwischen drei und 30 Zeichen lang sein.")
    private String telefonnummer;
    
    @OneToMany
    private List<Nachricht> nachrichten = new ArrayList();
    @ManyToMany
    private Anzeigenliste gemerkteAnzeigen = new Anzeigenliste();

    @OneToMany(mappedBy="besitzer")
    private Anzeigenliste veroeffentlichteAnzeigen = new Anzeigenliste();
    
    
    

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Benutzer() {
    }

    public Benutzer(String benutzername, String passwortHash, String vorname, String nachname, String street, String hausnummer, int plz, String ort, String land, String email, String telefonnummer) {
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
        this.vorname = vorname;
        this.nachname = nachname;
        this.street = street;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
        this.email = email;
        this.telefonnummer = telefonnummer;
      
    }

    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Passwortaufgaben">
    /**
     * Berechnet einen Hashwert aus dem übergebenen Passwort und legt ihn im
     * Feld passwordHash ab. Somit wird das Passwort niemals als Klartext
     * gespeichert.
     *
     * Gleichzeitig wird das Passwort im nicht gespeicherten Feld password
     * abgelegt, um durch die Bean Validation Annotationen überprüft werden
     * zu können.
     *
     * @param password Neues Passwort
     */
    public void setPasswordHash(String password) {
        this.passwortHash = this.hashPassword(password);
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
    //</editor-fold>
}
