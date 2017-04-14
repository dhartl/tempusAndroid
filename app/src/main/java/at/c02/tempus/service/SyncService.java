package at.c02.tempus.service;


import android.util.Log;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncService {

    private static final String TAG = "SyncService";

    private ProjectService projectService;

    public SyncService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void synchronize() {
        Log.d(TAG, "Sync started");
        projectService.loadProjects();
    }
}
