package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    //TODO warum diese strategy?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nr;

    @Version
    private int version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

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
