package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long nr;

    @Version
    private int version;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private  Timestamp createdAt;

    private String createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Timestamp updatedAt;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getNr() != null ? this.getNr().hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;

        BaseEntity other = (BaseEntity) object;
        if (this.getNr() != other.getNr() && (this.getNr() == null || !this.nr.equals(other.nr))) {
            return false;
        }
        return true;
    }
}
