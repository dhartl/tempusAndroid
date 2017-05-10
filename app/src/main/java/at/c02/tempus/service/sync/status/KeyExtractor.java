package at.c02.tempus.service.sync.status;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public interface KeyExtractor<T> {

    /**
     * Keys werden mit equals-Methode verglichen!!!
     *
     * @param item
     * @return
     */
    public Object extractKey(T item);
}
