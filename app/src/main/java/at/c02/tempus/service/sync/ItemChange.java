package at.c02.tempus.service.sync;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public enum ItemChange {
    /**
     * Item wurde neu erstellt
     */
    CREATED,
    /**
     * Item wurde verändert
     */
    UPDATED,
    /**
     * Item wurde gelöscht
     */
    DELETED,
    /**
     * Item wurde nicht verändert
     */
    NOT_CHANGED;
}
