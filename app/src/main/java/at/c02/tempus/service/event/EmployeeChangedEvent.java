package at.c02.tempus.service.event;

import at.c02.tempus.db.entity.EmployeeEntity;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class EmployeeChangedEvent {

    private EmployeeEntity employee;

    public EmployeeChangedEvent(EmployeeEntity employee) {
        this.employee = employee;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }
}
