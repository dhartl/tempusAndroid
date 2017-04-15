package at.c02.tempus.service;

import android.util.Log;

import com.fernandocejas.arrow.optional.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.api.model.Project;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.ProjectRepository;
import at.c02.tempus.service.event.ProjectsChangedEvent;
import at.c02.tempus.service.sync.SyncResult;
import at.c02.tempus.service.sync.SyncStatusFinder;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

public class ProjectService {
    private static final String TAG = "ProjectService";
    private ProjectApi projectApi;
    private ProjectRepository projectRepository;
    private EventBus eventBus;

    private SyncStatusFinder<ProjectEntity> syncStatusFinder = new SyncStatusFinder<>(ProjectEntity::getExternalId,
            (source, target) -> !Objects.equals(source.getName(), target.getName()));

    public ProjectService(ProjectApi projectApi, ProjectRepository projectRepository, EventBus eventBus) {
        this.projectApi = projectApi;
        this.projectRepository = projectRepository;
        this.eventBus = eventBus;
    }

    public Observable<List<ProjectEntity>> getProjects() {

        return Observable.fromCallable(() -> {
            return projectRepository.loadProjects();
        });
    }

    public void loadProjects() {
        projectApi.loadProjects().map(this::convertAllProjectsToEntities)
                .map(apiProjectEntities -> {
                    List<ProjectEntity> projectEntities = projectRepository.loadProjects();
                    Log.d(TAG, "Syncronisiere Projekte: " + apiProjectEntities.size() + " externe Projekte, "
                            + projectEntities.size() + " Projekte in Datenbank");
                    return syncStatusFinder.findSyncStatus(apiProjectEntities, projectEntities);
                })
                .subscribe(syncResults -> {
                    boolean emitChangedEvent = false;
                    for (SyncResult<ProjectEntity> syncResult : syncResults) {
                        try {
                            emitChangedEvent |= applySyncResult(syncResult);
                        } catch (Exception ex) {
                            Log.e(TAG, "Fehler bei der Synchronisation von Projekten; " + syncResult, ex);
                        }
                    }
                    Log.d(TAG, "Projekt Synchronisation abgeschlossen");
                    if (emitChangedEvent) {
                        eventBus.post(new ProjectsChangedEvent(projectRepository.loadProjects()));
                    }
                }, throwable -> Log.e(TAG, "Fehler bei der Projektsynchronisation", throwable));
    }

    private boolean applySyncResult(SyncResult<ProjectEntity> syncResult) {
        boolean changed = false;
        ProjectEntity source = syncResult.getSource();
        ProjectEntity target = syncResult.getTarget();
        switch (syncResult.getItemChange()) {
            case CREATED:
            case UPDATED: {
                changed = true;
                if (target != null) {
                    source.setId(target.getId());
                }
                projectRepository.createOrUpdate(source);
                break;
            }
            case DELETED: {
                changed = true;
                projectRepository.delete(target);
                break;
            }
            case NOT_CHANGED:
                //nichts tun
                break;
        }
        return changed;
    }

    public Observable<Optional<ProjectEntity>> getDefaultProject() {
        return Observable.fromCallable(()-> Optional.fromNullable(projectRepository.findDefaultProject()));
    }

    private List<ProjectEntity> convertAllProjectsToEntities(List<Project> projects) {
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertProjectToEntity(project));
        }
        return entities;
    }

    private ProjectEntity convertProjectToEntity(Project project) {
        ProjectEntity entity = new ProjectEntity();
        entity.setExternalId(Long.valueOf(project.getProjectId()));
        entity.setName(project.getProjectName());
        return entity;
    }
}
