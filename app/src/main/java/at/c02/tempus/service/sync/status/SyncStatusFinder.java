package at.c02.tempus.service.sync.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncStatusFinder<T> {

    private KeyExtractor<T> keyExtractor;
    private UpdateDetector<T> updateDetector;

    public SyncStatusFinder(KeyExtractor<T> keyExtractor, UpdateDetector<T> updateDetector) {
        this.keyExtractor = keyExtractor;
        this.updateDetector = updateDetector;
    }

    /**
     * @param sourceItems: Items von denen aus Synchronisiert wird (diese werden nicht verändert)
     * @param targetItems: Items, die synchronisiert werden (diese werden verändert)
     * @return Liste der Änderungen an Items
     */
    public List<SyncResult<T>> findSyncStatus(List<T> sourceItems, List<T> targetItems) {
        List<SyncResult<T>> results = new ArrayList<>();
        Map<Object, T> sourceKeyMap = createKeyMap(sourceItems);
        Map<Object, T> targetKeyMap = createKeyMap(targetItems);

        for (Map.Entry<Object, T> targetEntry : targetKeyMap.entrySet()) {
            Object targetKey = targetEntry.getKey();
            T targetItem = targetEntry.getValue();
            T sourceItem = sourceKeyMap.get(targetKey);
            ItemChange change;
            if (sourceItem == null) {
                change = ItemChange.DELETED;
            } else {
                if (updateDetector.isUpdated(sourceItem, targetItem)) {
                    change = ItemChange.UPDATED;
                } else {
                    change = ItemChange.NOT_CHANGED;
                }
                // SourceItem wurde bereits synchronisiert
                // So nur noch nicht vorhandene Einträge bleiben übrig
                sourceKeyMap.remove(targetKey);
            }
            results.add(new SyncResult<T>(change, sourceItem, targetItem));
        }

        for (T sourceItem : sourceKeyMap.values()) {
            results.add(new SyncResult<T>(ItemChange.CREATED, sourceItem, null));
        }
        return results;
    }

    private Map<Object, T> createKeyMap(List<T> items) {
        Map<Object, T> keyMap = new HashMap<>();
        for (T item : items) {
            keyMap.put(keyExtractor.extractKey(item), item);
        }
        return keyMap;
    }
}
