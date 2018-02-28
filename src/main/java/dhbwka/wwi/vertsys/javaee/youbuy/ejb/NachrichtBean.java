/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.ejb;

import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Kategorie;
import dhbwka.wwi.vertsys.javaee.youbuy.jpa.Nachricht;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;
/**
 *
 * @author METELC
 */

@Stateless
@RolesAllowed("youbuy-app-user")
public class NachrichtBean extends EntityBean<Nachricht, Long>{
    public NachrichtBean() {
        super(Nachricht.class);
    }
    
    
}
