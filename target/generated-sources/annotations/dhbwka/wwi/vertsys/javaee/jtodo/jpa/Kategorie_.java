package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Kategorie;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-28T16:22:59")
@StaticMetamodel(Kategorie.class)
public class Kategorie_ { 

    public static volatile SingularAttribute<Kategorie, String> name;
    public static volatile ListAttribute<Kategorie, Kategorie> elternKategorie;
    public static volatile SingularAttribute<Kategorie, String> slug;

}