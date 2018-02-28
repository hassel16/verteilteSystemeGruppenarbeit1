package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Foto;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Kategorie;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-28T16:22:59")
@StaticMetamodel(Anzeige.class)
public class Anzeige_ { 

    public static volatile SingularAttribute<Anzeige, String> ort;
    public static volatile SingularAttribute<Anzeige, Long> onlineBis;
    public static volatile SingularAttribute<Anzeige, String> art;
    public static volatile SingularAttribute<Anzeige, String> titel;
    public static volatile SingularAttribute<Anzeige, String> preisArt;
    public static volatile SingularAttribute<Anzeige, Double> preisVorstellung;
    public static volatile SingularAttribute<Anzeige, Long> id;
    public static volatile SingularAttribute<Anzeige, Long> creationDate;
    public static volatile SingularAttribute<Anzeige, String> beschreibung;
    public static volatile ListAttribute<Anzeige, Kategorie> kategorien;
    public static volatile ListAttribute<Anzeige, Foto> fotos;
    public static volatile SingularAttribute<Anzeige, Integer> plz;

}