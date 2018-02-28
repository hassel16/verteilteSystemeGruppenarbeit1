/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jtodo.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author METELC
 */
@Entity
@Getter
@AllArgsConstructor
@Setter
public class Nachricht implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="Id",length = 30)
    @GeneratedValue(generator = "nachricht_ids")
    private int id;
    
    @ManyToOne
    @Column(name="Von Benutzer")
    @NotNull(message = "Der VonBenutzer darf nicht leer sein.")
    private Benutzer vonBenutzer;
               
    @ManyToOne
    @Column(name="An Benutzer")
    @NotNull(message = "Der VonBenutzer darf nicht leer sein.")
    private Benutzer anBenutzer;        
    
    @Column(name="Text")
    @NotNull(message = "Der Text darf nicht leer sein.")
    private String text;
}
