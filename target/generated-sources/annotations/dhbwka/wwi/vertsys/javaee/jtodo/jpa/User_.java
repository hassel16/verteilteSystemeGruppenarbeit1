package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import dhbwka.wwi.vertsys.javaee.jtodo.jpa.Task;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T10:49:16")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile ListAttribute<User, String> groups;
    public static volatile SingularAttribute<User, String> passwordHash;
    public static volatile ListAttribute<User, Task> tasks;
    public static volatile SingularAttribute<User, String> username;

}