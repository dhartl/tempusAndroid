package at.c02.tempus.service;

import java.util.ArrayList;
import java.util.List;

import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.api.model.Project;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.ProjectRepository;
import io.reactivex.Flowable;
import io.reactivex.Observable;

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

    public Flowable<List<ProjectEntity>> getProjects() {

        return Flowable.fromCallable(() -> {
            List<ProjectEntity>projects = new ArrayList<>();
            projects.add(new ProjectEntity(1L,1L,"Projekt 1"));
            projects.add(new ProjectEntity(2L,2L,"Projekt 2"));
            projects.add(new ProjectEntity(3L,3L,"Projekt 3"));
            projects.add(new ProjectEntity(4L,4L,"Projekt 4"));
            projects.add(new ProjectEntity(5L,5L,"Projekt 5"));
            return projects;
        });
    }
}
