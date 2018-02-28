package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Nachricht;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-28T16:22:59")
@StaticMetamodel(Benutzer.class)
public class Benutzer_ { 

    public static volatile SingularAttribute<Benutzer, String> straße;
    public static volatile SingularAttribute<Benutzer, String> vorname;
    public static volatile ListAttribute<Benutzer, Nachricht> nachrichten;
    public static volatile SingularAttribute<Benutzer, String> benutzername;
    public static volatile SingularAttribute<Benutzer, String> ort;
    public static volatile SingularAttribute<Benutzer, String> passwortHash;
    public static volatile SingularAttribute<Benutzer, String> hausnummer;
    public static volatile SingularAttribute<Benutzer, Integer> telefonnummer;
    public static volatile SingularAttribute<Benutzer, String> nachname;
    public static volatile SingularAttribute<Benutzer, String> land;
    public static volatile ListAttribute<Benutzer, Anzeige> veroeffentlichteAnzeigen;
    public static volatile ListAttribute<Benutzer, Anzeige> gemerkteAnzeigen;
    public static volatile SingularAttribute<Benutzer, String> email;
    public static volatile SingularAttribute<Benutzer, Integer> plz;

}