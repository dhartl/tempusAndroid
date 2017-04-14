package at.c02.tempus.service.sync;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public interface UpdateDetector<T> {

    boolean isUpdated(T source, T target);

}
