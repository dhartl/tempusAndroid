package at.c02.tempus.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Daniel on 09.04.2017.
 */
@Entity
public class ProjectEntity {
    @Id
    private Long id;
    /**
     * API-Id des Projektes
     */
    private Long externalId;

    /**
     * Name des Projekts
     */
    private String name;

    @Generated(hash = 1007531186)
    public ProjectEntity(Long id, Long externalId, String name) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
    }

    @Generated(hash = 939074542)
    public ProjectEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return this.externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectEntity that = (ProjectEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
