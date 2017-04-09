package at.c02.tempus.db.repository;

import at.c02.tempus.db.entity.EmployeeEntityDao;

/**
 * Klasse für Queries auf Employee-Tabelle
 * Created by Daniel on 09.04.2017.
 */

public class EmployeeRepository {

    private EmployeeEntityDao dao;

    public EmployeeRepository(EmployeeEntityDao dao) {
        this.dao = dao;
    }
}
