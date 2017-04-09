package at.c02.tempus.db.repository;

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
}
