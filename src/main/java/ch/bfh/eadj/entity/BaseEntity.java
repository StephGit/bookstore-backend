package ch.bfh.eadj.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {


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

    public int getVersion() {
        return version;
    }
}
