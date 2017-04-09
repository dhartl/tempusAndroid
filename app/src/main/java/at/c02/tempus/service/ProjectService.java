package at.c02.tempus.service;

import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.api.model.Project;
import at.c02.tempus.db.repository.ProjectRepository;

/**
 * Created by Daniel on 09.04.2017.
 */

public class ProjectService {
    private ProjectApi projectApi;
    private ProjectRepository projectRepository;

    public ProjectService(ProjectApi projectApi, ProjectRepository projectRepository) {
        this.projectApi = projectApi;
        this.projectRepository = projectRepository;
    }
}
