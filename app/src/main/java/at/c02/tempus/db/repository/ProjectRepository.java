package at.c02.tempus.db.repository;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.entity.ProjectEntityDao;

/**
 * Klasse für Queries auf Projekt-Tabelle
 * Created by Daniel on 09.04.2017.
 */
public class ProjectRepository extends AbstractRepository<ProjectEntity, Long, ProjectEntityDao> {


    public ProjectRepository(ProjectEntityDao dao) {
        super(dao);
    }

    public ProjectEntity findDefaultProject() {
        return dao.queryBuilder()
                .limit(1)
                .unique();
    }

    public ProjectEntity findByExternalId(Long externalEmployeeId) {
        return dao.queryBuilder()
                .where(ProjectEntityDao.Properties.ExternalId.eq(externalEmployeeId))
                .unique();
    }
}
