/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.jpa.enums;

/**
 *
 * @author HASSED
 */
public enum PreisArt {
    
    FESTPREIS, VERHANDLUNGSBASIS;
    public String getLabel() {
        switch (this) {
            case FESTPREIS:
                return "Festpreis";
            case VERHANDLUNGSBASIS:
                return "Verhandlungsbasis";
            default:
                return this.toString();
        }
    }

}
