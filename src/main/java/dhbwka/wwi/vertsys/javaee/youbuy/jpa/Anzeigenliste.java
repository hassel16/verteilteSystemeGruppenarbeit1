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

import java.util.ArrayList;

/**
 *
 * @author HASSED
 */
public class Anzeigenliste extends ArrayList<Anzeige> {

     /**
     * Fügt der Anzeigenliste einen weitere Anzeige hinzu.
     *
     * @param anzeige Name der Benutzergruppe
     * @return ob die Operation erfolgreich war
     */
    @Override
    public boolean add(Anzeige anzeige){
        if (!this.contains(anzeige)) {
            super.add(anzeige);
            return true;
        }
        return false;
    }
    
    Anzeigenliste(){
        super();
    }
}
