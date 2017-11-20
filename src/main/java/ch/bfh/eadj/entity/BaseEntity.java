package ch.bfh.eadj.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {

    //TODO warum diese strategy?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nr;

    @Version
    private int version;

    private  Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    protected String updatedBy;

    public Long getNr() {
        return nr;
    }

    public int getVersion() {
        return version;
    }

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
