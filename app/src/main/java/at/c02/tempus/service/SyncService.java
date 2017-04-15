package at.c02.tempus.service;


import android.util.Log;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncService {

    private static final String TAG = "SyncService";

    private ProjectService projectService;
    private EmployeeService employeeService;

    public SyncService(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    public void synchronize() {
        Log.d(TAG, "Sync started");
        projectService.loadProjects();
        employeeService.loadEmployee();

    }
}
