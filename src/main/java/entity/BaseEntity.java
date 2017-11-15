package entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    protected Timestamp createdAt;
    protected String createdFrom;
    protected Timestamp updatedAt;
    protected String updatedFrom;
}
