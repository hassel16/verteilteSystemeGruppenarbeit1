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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * @author METELC
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Kategorie implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="slug")
    private String slug;
    
    @Column(name="Name")
    @NotNull(message = "Der Name darf nicht leer sein.")
    private String name;
               
    @OneToMany //Steht so im ER Diagramm
    @JoinColumn(name="slug")
    private List<Kategorie> elternKategorie = new ArrayList<>();
    
    Kategorie(){}
}