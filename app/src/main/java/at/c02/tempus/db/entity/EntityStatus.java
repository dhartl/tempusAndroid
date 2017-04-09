package at.c02.tempus.db.entity;

/**
 * Created by Daniel on 09.04.2017.
 */

public enum EntityStatus {
    /**
     * Entity wurde neu erzeugt; keine Repräsentation am Server
     */
    NEW(0),
    /**
     * Entity befindet sich am Server und wurde verändert
     */
    MODIFIED(1),
    /**
     * Entity befindet sich am Server und wurde gelöscht
     */
    DELETED(2),
    /**
     * Entity ist synchron mit Server
     */
    SYNCED(3),
    UNKNOWN(4);

    private final int id;

    private EntityStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
