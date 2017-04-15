package at.c02.tempus.db.repository;

import java.util.List;

import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.BookingEntityDao;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;

/**
 * Klasse für Queries auf Booking-Tabelle
 * Created by Daniel on 09.04.2017.
 */
public class BookingRepository {

    private BookingEntityDao dao;

    public BookingRepository(BookingEntityDao dao) {
        this.dao = dao;
    }

    public BookingEntity createOrUpdate(BookingEntity entity) {
        dao.save(entity);
        return entity;
    }

    public void delete(BookingEntity entity) {
        dao.delete(entity);
    }

    public List<BookingEntity> loadBookings() {
        return dao.queryDeep("");
    }

    public BookingEntity findLastBooking() {
        BookingEntity bookingEntity = dao.queryBuilder()
                .orderDesc(BookingEntityDao.Properties.BeginDate)
                .limit(1)
                .unique();
        if (bookingEntity != null) {
            bookingEntity.getProject();
        }
        return bookingEntity;
    }

    public BookingEntity findBookingById(Long id) {
        return dao.loadDeep(id);
    }

    public List<BookingEntity> findModifiedEntries() {
        return dao.queryBuilder()
                .where(BookingEntityDao.Properties.SyncStatus.in(
                        EntityStatus.DELETED,
                        EntityStatus.MODIFIED,
                        EntityStatus.NEW
                )).list();
    }

    public List<BookingEntity> findServerBookings() {
        return dao.queryBuilder()
                .where(BookingEntityDao.Properties.ExternalId.isNotNull())
                .list();
    }
}
