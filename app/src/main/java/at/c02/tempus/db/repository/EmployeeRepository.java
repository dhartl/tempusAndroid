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

public class EmployeeRepository extends AbstractRepository<EmployeeEntity, Long, EmployeeEntityDao>{


    public EmployeeRepository(EmployeeEntityDao dao) {
        super(dao);
    }

    public EmployeeEntity findByUserName(String userName) {
        return dao.queryBuilder()
                .where(EmployeeEntityDao.Properties.UserName.like(userName))
                .unique();
    }
}
