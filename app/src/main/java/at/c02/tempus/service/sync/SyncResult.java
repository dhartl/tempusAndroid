package at.c02.tempus.service.sync;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncResult<T> {
    private ItemChange itemChange;
    private T source;
    private T target;

    public SyncResult(ItemChange itemChange, T source, T target) {
        this.itemChange = itemChange;
        this.source = source;
        this.target = target;
    }

    public ItemChange getItemChange() {
        return itemChange;
    }

    public T getSource() {
        return source;
    }

    public T getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyncResult<?> that = (SyncResult<?>) o;

        if (itemChange != that.itemChange) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        return target != null ? target.equals(that.target) : that.target == null;
    }

    @Override
    public int hashCode() {
        int result = itemChange != null ? itemChange.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SyncResult{" +
                "itemChange=" + itemChange +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
