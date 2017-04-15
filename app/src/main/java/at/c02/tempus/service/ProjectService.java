package at.c02.tempus.service;

import android.util.Log;

import com.fernandocejas.arrow.optional.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.ProjectRepository;
import at.c02.tempus.service.event.ProjectsChangedEvent;
import at.c02.tempus.service.mapping.ProjectMapping;
import at.c02.tempus.service.sync.status.SyncResult;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;
import at.c02.tempus.utils.CollectionUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

@Singleton
public class ProjectService {
    private static final String TAG = "ProjectService";

    @Inject
    protected ProjectRepository projectRepository;

    @Inject
    public ProjectService() {
    }

    public Observable<List<ProjectEntity>> getProjects() {
        return Observable.fromCallable(projectRepository::findAll);
    }

    public Observable<Optional<ProjectEntity>> getDefaultProject() {
        return Observable.fromCallable(() -> Optional.fromNullable(projectRepository.findDefaultProject()));
    }


}
