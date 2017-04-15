package at.c02.tempus.db.repository;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EmployeeEntityDao;
import at.c02.tempus.db.entity.ProjectEntity;

/**
 * Klasse für Queries auf Employee-Tabelle
 * Created by Daniel on 09.04.2017.
 */

public class EmployeeRepository {

    private EmployeeEntityDao dao;

    public EmployeeRepository(EmployeeEntityDao dao) {
        this.dao = dao;
    }

    public EmployeeEntity findByUserName(String userName) {
        return dao.queryBuilder()
                .where(EmployeeEntityDao.Properties.UserName.eq(userName))
                .unique();
    }

    public List<EmployeeEntity> loadAll() {
        return dao.loadAll();
    }

    public EmployeeEntity createOrUpdate(EmployeeEntity entity) {
        dao.save(entity);
        return entity;
    }

    public void delete(EmployeeEntity entity) {
        dao.delete(entity);
    }
}
