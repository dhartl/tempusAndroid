package at.c02.tempus.service.event;

import java.util.List;

import at.c02.tempus.db.entity.ProjectEntity;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class ProjectsChangedEvent {
    private List<ProjectEntity> projects;

    public ProjectsChangedEvent(List<ProjectEntity> projects) {
        this.projects = projects;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }
}
