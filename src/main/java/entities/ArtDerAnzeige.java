/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author FSche
 */
public enum ArtDerAnzeige {
    BIETE, SUCHE;
    public String getLabel() {
        switch (this) {
            case BIETE:
                return "Biete";
            case SUCHE:
                return "Suche";
            default:
                return this.toString();
        }
    }
}
