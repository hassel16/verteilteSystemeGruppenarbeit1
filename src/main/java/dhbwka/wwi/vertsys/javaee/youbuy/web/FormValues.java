/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.youbuy.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Kleine Hilfsklasse, die als Objekt in der HTTP-Session abgelegt werden
 * kann, um die fehlerhaften Eingaben eines Formulars zwischenzuspeichern.
 */
@Getter
@Setter
public class FormValues {
    
    private Map<String, String[]> values = new HashMap<>();
    private List<String> errors = new ArrayList<>();
   
}
