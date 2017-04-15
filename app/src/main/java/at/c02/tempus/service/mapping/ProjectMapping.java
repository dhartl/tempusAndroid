package at.c02.tempus.service.mapping;

import at.c02.tempus.api.model.Project;
import at.c02.tempus.db.entity.ProjectEntity;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class ProjectMapping {
    public static ProjectEntity toProjectEntity(Project project) {
        ProjectEntity entity = new ProjectEntity();
        entity.setExternalId(Long.valueOf(project.getProjectId()));
        entity.setName(project.getProjectName());
        return entity;
    }
}
