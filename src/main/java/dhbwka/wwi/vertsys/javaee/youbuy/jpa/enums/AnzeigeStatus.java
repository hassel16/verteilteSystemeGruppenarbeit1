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
 * Statuswerte einer Aufgabe.
 */
public enum AnzeigeStatus {
OFFEN,ABGELAUFEN,VERKAUFT;

    public String getLabel() {
        switch (this) {
            case OFFEN:
                return "Biete";
            case ABGELAUFEN:
                return "Abgelaufen";
            case VERKAUFT:
                return "Verkauft";
            default:
                return this.toString();
        }
    }

}
