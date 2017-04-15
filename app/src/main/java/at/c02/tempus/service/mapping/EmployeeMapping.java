package at.c02.tempus.service.mapping;

import at.c02.tempus.api.model.Employee;
import at.c02.tempus.db.entity.EmployeeEntity;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class EmployeeMapping {

    public static EmployeeEntity toEmployeeEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setExternalId(Long.valueOf(employee.getEmployeeId()));
        entity.setUserName(employee.getUserName());
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        return entity;
    }
}
