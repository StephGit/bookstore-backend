package ch.bfh.eadj.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{


    /*
    GenerationType.IDENTITY verwendet separate primary key indentity column
    AUTO sollte nur für Entwicklung und Prototyping verwendet werden weil der Provider seine eigene Strategie wählt.1
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nr;

    @Version
    private int version;

    public Long getNr() {
        return nr;
    }

    public void setNr(Long nr) {
        this.nr = nr;
    }

    public int getVersion() {
        return version;
    }
}
