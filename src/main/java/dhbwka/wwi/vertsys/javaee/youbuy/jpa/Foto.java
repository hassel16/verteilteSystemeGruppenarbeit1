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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Kategorien, die den Aufgaben zugeordnet werden können.
 */
@Entity
@Getter
@AllArgsConstructor
@Setter
public class Foto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "foto_ids")
    @TableGenerator(name = "foto_ids", initialValue = 0, allocationSize = 50)
    @Column(name="Id")
    private long id;

    @Column(length = 30, name="Bezeichnung")
    @NotNull(message = "Die Bezeichnung darf nicht leer sein.")
    private String bezeichnung;
    
    @Column(name="Bilddaten")
    private String bilddaten;
    
    Foto(){}
}
