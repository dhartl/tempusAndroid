package at.c02.tempus.service.sync;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class Pair<T> {
    private T source;
    private T target;

    public Pair(T source, T target) {
        this.source = source;
        this.target = target;
    }

    public T getSource() {
        return source;
    }

    public T getTarget() {
        return target;
    }
}
