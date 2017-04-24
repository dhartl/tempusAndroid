package at.c02.tempus.db.repository;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import at.c02.tempus.api.model.Employee;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.BookingEntityDao;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.entity.ProjectEntity;

/**
 * Klasse für Queries auf Booking-Tabelle
 * Created by Daniel on 09.04.2017.
 */
public class BookingRepository extends AbstractRepository<BookingEntity, Long, BookingEntityDao> {

    public BookingRepository(BookingEntityDao dao) {
        super(dao);
    }

    public List<BookingEntity> loadAllDeep() {
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
        QueryBuilder<BookingEntity> queryBuilder = dao.queryBuilder();
        queryBuilder.join(BookingEntityDao.Properties.ProjectId, ProjectEntity.class);
        queryBuilder.join(BookingEntityDao.Properties.EmployeeId, EmployeeEntity.class);
        return queryBuilder
                .where(BookingEntityDao.Properties.SyncStatus.in(
                        EntityStatus.DELETED.getId(),
                        EntityStatus.MODIFIED.getId(),
                        EntityStatus.NEW.getId()
                )).list();
    }

    public List<BookingEntity> findServerBookings() {
        return dao.queryBuilder()
                .where(BookingEntityDao.Properties.ExternalId.isNotNull())
                .list();
    }
}
