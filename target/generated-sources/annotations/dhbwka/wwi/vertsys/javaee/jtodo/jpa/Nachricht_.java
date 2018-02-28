package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Nachricht;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Benutzer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-28T16:22:59")
@StaticMetamodel(Nachricht.class)
public class Nachricht_ { 

    public static volatile SingularAttribute<Nachricht, Benutzer> anBenutzer;
    public static volatile SingularAttribute<Nachricht, Integer> id;
    public static volatile SingularAttribute<Nachricht, Benutzer> vonBenutzer;
    public static volatile SingularAttribute<Nachricht, String> text;

}