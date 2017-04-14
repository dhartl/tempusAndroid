package at.c02.tempus.db.repository;

import java.util.List;

import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.entity.ProjectEntityDao;

/**
 * Klasse für Queries auf Projekt-Tabelle
 * Created by Daniel on 09.04.2017.
 */
public class ProjectRepository {

    private ProjectEntityDao dao;

    public ProjectRepository(ProjectEntityDao dao) {
        this.dao = dao;
    }


    public ProjectEntity createOrUpdate(ProjectEntity entity) {
        dao.save(entity);
        return entity;
    }

    public void delete(ProjectEntity entity) {
        dao.delete(entity);
    }

    public List<ProjectEntity> loadProjects() {
        return dao.loadAll();
    }
}
