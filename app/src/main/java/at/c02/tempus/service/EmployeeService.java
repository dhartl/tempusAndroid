package at.c02.tempus.service;

import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.db.repository.EmployeeRepository;

/**
 * Created by Daniel on 09.04.2017.
 */

public class EmployeeService {
    private EmployeeApi employeeApi;
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeApi employeeApi, EmployeeRepository employeeRepository) {
        this.employeeApi = employeeApi;
        this.employeeRepository = employeeRepository;
    }
}
