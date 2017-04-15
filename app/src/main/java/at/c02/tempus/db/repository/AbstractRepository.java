package at.c02.tempus.db.repository;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

import at.c02.tempus.db.entity.BookingEntity;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class AbstractRepository<E, K, T extends AbstractDao<E, K>> {

    protected T dao;

    public AbstractRepository(T dao) {
        this.dao = dao;
    }

    public E createOrUpdate(E entity) {
        dao.save(entity);
        return entity;
    }

    public void delete(E entity) {
        dao.delete(entity);
    }

    public List<E> findAll() {
        return dao.loadAll();
    }
}
