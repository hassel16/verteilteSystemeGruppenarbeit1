/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude = {"anzeige"})
@Table(name = "JYOUBUY_USER")
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "USERNAME", length = 64)
    @Size(min = 4, max = 64, message = "Der Benutzername muss zwischen fünf und 64 Zeichen lang sein.")
    @NotNull(message = "Der Benutzername darf nicht leer sein.")
    private String benutzername;

    @Column(name = "PASSWORD_HASH", length = 64)
    @NotNull(message = "Das Passwort darf nicht leer sein.")
    private String passwortHash;

    @Size(min = 1, max = 64, message = "Der Vorname muss zwischen zwei und 64 Zeichen lang sein.")
    @NotNull(message = "Der Vorname darf nicht leer sein.")
    private String vorname;

    @Size(min = 1, max = 64, message = "Der Nachname muss zwischen zwei und 64 Zeichen lang sein.")
    @NotNull(message = "Der Nachname darf nicht leer sein.")
    private String nachname;

    @Size(min = 1, max = 64, message = "Die Straße muss zwischen zwei und 64 Zeichen lang sein.")
    @NotNull(message = "Die Straße darf nicht leer sein.")
    private String strasse;

    //String weil zb 2a auch mgl ist
    @NotNull(message = "Die Hausnummer darf nicht leer sein.")
    private String hausnummer;

    @Size(min = 1, max = 10, message = "Die Postleitzahl muss zwischen drei und 10 Zeichen lang sein.")
    @NotNull(message = "Die Postleitzahl darf nicht leer sein.")
    private String postleitzahl;

    @Size(min = 2, max = 64, message = "Der Ort muss zwischen zwei und 64 Zeichen lang sein.")
    @NotNull(message = "Der Ort darf nicht leer sein.")
    private String ort;

    @Size(min = 1, max = 64, message = "Das Land muss zwischen zwei und 64 Zeichen lang sein.")
    @NotNull(message = "Das Land darf nicht leer sein.")
    private String land;

    @Size(min = 5, max = 64, message = "Die E-Mail muss zwischen fünf und 64 Zeichen lang sein.")
    @NotNull(message = "Die E-Mail darf nicht leer sein.")
    private String email;

    @Size(min = 5, max = 64, message = "Die Telefonnummer muss zwischen fünf und 64 Zeichen lang sein.")
    @NotNull(message="Die Telefonnummer darf nicht leer sein.")
    private String telefonnummer;

    @Transient
    @Size(min = 6, max = 64, message = "Das Passwort muss zwischen sechs und 64 Zeichen lang sein.")
    private String passwort;

    @ElementCollection
    @CollectionTable(
            name = "JYOUBUY_USER_GROUP",
            joinColumns = @JoinColumn(name = "USERNAME")
    )
    @Column(name = "GROUPNAME")
    List<String> groups = new ArrayList<>();

    @OneToMany(mappedBy = "benutzer", fetch = FetchType.LAZY)
    private List<Anzeige> anzeige;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Anzeige> gemerkteAnzeigen = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="Passwort setzen und prüfen">
    /**
     * Berechnet der Hash-Wert zu einem Passwort.
     *
     * @param password Passwort
     * @return Hash-Wert
     */
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

    /**
     * Berechnet einen Hashwert aus dem übergebenen Passwort und legt ihn im
     * Feld passwordHash ab. Somit wird das Passwort niemals als Klartext
     * gespeichert.
     *
     * Gleichzeitig wird das Passwort im nicht gespeicherten Feld password
     * abgelegt, um durch die Bean Validation Annotationen überprüft werden zu
     * können.
     *
     * @param password Neues Passwort
     */
    public void setPassword(String password) {
        this.passwort = password;
        this.passwortHash = this.hashPassword(password);
    }

    /**
     * Prüft, ob das übergebene Passwort korrekt ist.
     *
     * @param password Zu prüfendes Passwort
     * @return true wenn das Passwort stimmt sonst false
     */
    public boolean checkPassword(String password) {
        return this.passwortHash
                .equals(this.hashPassword(password));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Zuordnung zu Benutzergruppen">
    /**
     * @return Eine unveränderliche Liste aller Benutzergruppen
     */
    public List<String> getGroups() {
        List<String> groupsCopy = new ArrayList<>();

        this.groups.forEach((groupname) -> {
            groupsCopy.add(groupname);
        });

        return groupsCopy;
    }

    /**
     * Fügt den Benutzer einer weiteren Benutzergruppe hinzu.
     *
     * @param groupname Name der Benutzergruppe
     */
    public void addToGroup(String groupname) {
        if (!this.groups.contains(groupname)) {
            this.groups.add(groupname);
        }
    }

    /**
     * Entfernt den Benutzer aus der übergebenen Benutzergruppe.
     *
     * @param groupname Name der Benutzergruppe
     */
    public void removeFromGroup(String groupname) {
        this.groups.remove(groupname);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Benutzer() {
    }

    public Benutzer(String benutzername, String password, String vorname, String nachname, String strasse, String hausnummer, String postleitzahl, String ort, String land, String eMail, String telefonnummer) {
        this.benutzername = benutzername;
        this.passwort = password;
        this.passwortHash = this.hashPassword(password);
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.land = land;
        this.email = eMail;
        this.telefonnummer = telefonnummer;
    }

    //</editor-fold>
}
