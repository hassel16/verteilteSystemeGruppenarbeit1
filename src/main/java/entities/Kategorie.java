/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;


import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(exclude = "anzeigen")
public class Kategorie implements Serializable{
  
    @Id
    @GeneratedValue(generator = "category_ids")
    @TableGenerator(name = "category_ids", initialValue = 0, allocationSize = 50)
    private long id;

    private String name;

    @OneToMany(mappedBy = "kategorie", fetch = FetchType.LAZY)
    private List<Anzeige> anzeigen;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    
    public Kategorie() {
    }

    public Kategorie(String name) {
        this.name = name;
    }
    
    //</editor-fold>
}
