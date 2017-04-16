package at.c02.tempus.service.sync;

import android.util.Log;

import java.util.Collection;
import java.util.List;

import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.service.event.BookingsChangedEvent;
import at.c02.tempus.service.sync.status.SyncResult;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public abstract class AbstractSyncService<T> {

    private static final String TAG = "AbstractSyncService";

    private SyncStatusFinder<T> syncStatusFinder;


    public AbstractSyncService(SyncStatusFinder<T> syncStatusFinder) {
        this.syncStatusFinder = syncStatusFinder;
    }

    protected abstract String getName();

    public Observable<Boolean> syncronize() {
        Log.d(TAG, String.format("%s: sync started", getName()));
        return loadLegacyItems()
                .map(items -> {
                    Log.d(TAG, String.format("%s: started evaluation", getName()));
                    return items;
                })
                .map(this::createSyncStatus)
                .map(syncResults -> {
                    boolean itemChanged = false;
                    for (SyncResult<T> syncResult : syncResults) {
                        try {
                            itemChanged |= applySyncResult(syncResult);
                        } catch (Exception ex) {
                            Log.e(TAG, String.format("%s: Error while applying sync %s",
                                    getName(), syncResult), ex);
                        }
                    }
                    Log.d(TAG, String.format("%s: Finished applying sync", getName()));
                    return itemChanged;
                }).map(itemChanged -> {
                    if (itemChanged) {
                        publishResults();
                    }
                    return itemChanged;
                });
    }

    protected abstract Observable<List<T>> loadLegacyItems();

    protected abstract List<T> loadLocalItems();

    protected abstract void publishResults();

    protected List<SyncResult<T>> createSyncStatus(List<T> legacyItems) {
        List<T> localItems = loadLocalItems();
        Log.d(TAG, String.format("%s: Syncronization ItemCount local=%d, legacy=%d",
                getName(), localItems.size(), legacyItems.size()));
        return syncStatusFinder.findSyncStatus(legacyItems, localItems);
    }

    private boolean applySyncResult(SyncResult<T> syncResult) {
        boolean changed = false;
        T source = syncResult.getSource();
        T target = syncResult.getTarget();
        switch (syncResult.getItemChange()) {
            case CREATED:
            case UPDATED: {
                Log.d(TAG,String.format("%s: create or update %s",getName(), syncResult));
                changed = true;
                createOrUpdate(source, target);
                break;
            }
            case DELETED: {
                Log.d(TAG,String.format("%s: delete %s",getName(), syncResult));
                delete(target);
                break;
            }
            case NOT_CHANGED:
                //nichts tun
                break;
        }
        return changed;
    }

    protected abstract void createOrUpdate(T source, T target);

    protected abstract void delete(T target);
}
