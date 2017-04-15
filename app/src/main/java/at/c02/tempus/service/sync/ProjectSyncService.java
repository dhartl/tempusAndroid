package at.c02.tempus.service.sync;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.ProjectRepository;
import at.c02.tempus.service.event.ProjectsChangedEvent;
import at.c02.tempus.service.mapping.ProjectMapping;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;
import at.c02.tempus.utils.CollectionUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class ProjectSyncService extends AbstractSyncService<ProjectEntity> {

    @Inject
    protected ProjectApi projectApi;
    @Inject
    protected ProjectRepository projectRepository;
    @Inject
    protected EventBus eventBus;

    @Inject
    public ProjectSyncService() {
        super(new SyncStatusFinder<>(ProjectEntity::getExternalId,
                UpdateDetectorFactory.create(ProjectEntity::getName)));
    }

    @Override
    protected String getName() {
        return "Project";
    }

    @Override
    protected Observable<List<ProjectEntity>> loadLegacyItems() {
        return projectApi.loadProjects()
                .map(legacyProjects -> CollectionUtils.convertList(legacyProjects, ProjectMapping::toProjectEntity));
    }

    @Override
    protected List<ProjectEntity> loadLocalItems() {
        return projectRepository.findAll();
    }

    @Override
    protected void publishResults() {
        eventBus.post(new ProjectsChangedEvent(loadLocalItems()));
    }

    @Override
    protected void createOrUpdate(ProjectEntity source, ProjectEntity target) {
        if (target != null) {
            source.setId(target.getId());
        }
        projectRepository.createOrUpdate(source);
    }

    @Override
    protected void delete(ProjectEntity target) {
        projectRepository.delete(target);
    }
}
