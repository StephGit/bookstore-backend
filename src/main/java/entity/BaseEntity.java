package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Version
    private int version;

    @Temporal(TemporalType.TIMESTAMP)
    private  Timestamp createdAt;

    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    protected String updatedBy;

    public Long getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (version != that.version) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        return updatedBy != null ? updatedBy.equals(that.updatedBy) : that.updatedBy == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + version;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        return result;
    }
}
