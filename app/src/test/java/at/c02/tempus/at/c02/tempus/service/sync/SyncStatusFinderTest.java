package at.c02.tempus.at.c02.tempus.service.sync;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.c02.tempus.service.sync.ItemChange;
import at.c02.tempus.service.sync.SyncResult;
import at.c02.tempus.service.sync.SyncStatusFinder;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncStatusFinderTest {

    private static class Item {
        int id;
        String name;

        public Item(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            if (id != item.id) return false;
            return name != null ? name.equals(item.name) : item.name == null;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    private SyncStatusFinder<Item> syncStatusFinder = new SyncStatusFinder<Item>(
            item -> item.id,
            (source, target) -> !Objects.equals(source.name, target.name));

    @Test
    public void testNewItem() throws Exception {
        List<Item> sourceList = createItemList(new Item(1, "one"));
        List<Item> targetList = createItemList();

        List<SyncResult<Item>> syncResults = syncStatusFinder.findSyncStatus(sourceList, targetList);
        Assert.assertThat(syncResults, Matchers.containsInAnyOrder(
                new SyncResult<>(ItemChange.CREATED, new Item(1, "one"), null)
        ));
    }

    @Test
    public void testUpdatedItem() throws Exception {
        List<Item> sourceList = createItemList(new Item(1, "one"));
        List<Item> targetList = createItemList(new Item(1, "changed"));

        List<SyncResult<Item>> syncResults = syncStatusFinder.findSyncStatus(sourceList, targetList);
        Assert.assertThat(syncResults, Matchers.containsInAnyOrder(
                new SyncResult<>(ItemChange.UPDATED, new Item(1, "one"), new Item(1, "changed"))
        ));
    }

    @Test
    public void testDeletedItem() throws Exception {
        List<Item> sourceList = createItemList();
        List<Item> targetList = createItemList(new Item(1, "one"));

        List<SyncResult<Item>> syncResults = syncStatusFinder.findSyncStatus(sourceList, targetList);
        Assert.assertThat(syncResults, Matchers.containsInAnyOrder(
                new SyncResult<>(ItemChange.DELETED, null, new Item(1, "one"))
        ));
    }

    @Test
    public void testNotChangedItem() throws Exception {
        List<Item> sourceList = createItemList(new Item(1, "one"));
        List<Item> targetList = createItemList(new Item(1, "one"));

        List<SyncResult<Item>> syncResults = syncStatusFinder.findSyncStatus(sourceList, targetList);
        Assert.assertThat(syncResults, Matchers.containsInAnyOrder(
                new SyncResult<>(ItemChange.NOT_CHANGED, new Item(1, "one"), new Item(1, "one"))
        ));
    }

    @Test
    public void testMixedItems() throws Exception {
        List<Item> sourceList = createItemList(new Item(1, "not changed"), new Item(2, "new updated"), new Item(3, "created"));
        List<Item> targetList = createItemList(new Item(1, "not changed"), new Item(2, "old updated"), new Item(4, "deleted"));

        List<SyncResult<Item>> syncResults = syncStatusFinder.findSyncStatus(sourceList, targetList);
        Assert.assertThat(syncResults, Matchers.containsInAnyOrder(
                new SyncResult<>(ItemChange.NOT_CHANGED, new Item(1, "not changed"), new Item(1, "not changed")),
                new SyncResult<>(ItemChange.UPDATED, new Item(2, "new updated"), new Item(2, "old updated")),
                new SyncResult<>(ItemChange.CREATED, new Item(3, "created"), null),
                new SyncResult<>(ItemChange.DELETED, null, new Item(4, "deleted"))
        ));
    }

    private List<Item> createItemList(Item... items) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items) {
            itemList.add(item);
        }
        return itemList;
    }
}
