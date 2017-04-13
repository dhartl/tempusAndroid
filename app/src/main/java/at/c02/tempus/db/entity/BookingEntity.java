package at.c02.tempus.db.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

import at.c02.tempus.db.converter.EntityStatusConverter;

/**
 * Created by Daniel on 09.04.2017.
 */
@Entity
public class BookingEntity {

    @Id
    private Long id;

    private Long externalId;
    
    private Long bookingId = null;

    private Long projectId;

    @ToOne(joinProperty = "projectId")
    private ProjectEntity project;

    private Long employeeId;

    @ToOne(joinProperty = "employeeId")
    private EmployeeEntity employee;

    private Date beginDate;

    private Date endDate;

    @Convert(columnType = Integer.class, converter = EntityStatusConverter.class)
    private EntityStatus syncStatus;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1776279694)
    private transient BookingEntityDao myDao;

    @Generated(hash = 518469429)
    public BookingEntity(Long id, Long externalId, Long bookingId, Long projectId,
            Long employeeId, Date beginDate, Date endDate, EntityStatus syncStatus) {
        this.id = id;
        this.externalId = externalId;
        this.bookingId = bookingId;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.syncStatus = syncStatus;
    }

    @Generated(hash = 952443125)
    public BookingEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EntityStatus getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(EntityStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    @Generated(hash = 1005767482)
    private transient Long project__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1111416221)
    public ProjectEntity getProject() {
        Long __key = this.projectId;
        if (project__resolvedKey == null || !project__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProjectEntityDao targetDao = daoSession.getProjectEntityDao();
            ProjectEntity projectNew = targetDao.load(__key);
            synchronized (this) {
                project = projectNew;
                project__resolvedKey = __key;
            }
        }
        return project;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 439791433)
    public void setProject(ProjectEntity project) {
        synchronized (this) {
            this.project = project;
            projectId = project == null ? null : project.getId();
            project__resolvedKey = projectId;
        }
    }

    @Generated(hash = 584425655)
    private transient Long employee__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1959867218)
    public EmployeeEntity getEmployee() {
        Long __key = this.employeeId;
        if (employee__resolvedKey == null || !employee__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EmployeeEntityDao targetDao = daoSession.getEmployeeEntityDao();
            EmployeeEntity employeeNew = targetDao.load(__key);
            synchronized (this) {
                employee = employeeNew;
                employee__resolvedKey = __key;
            }
        }
        return employee;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 784683370)
    public void setEmployee(EmployeeEntity employee) {
        synchronized (this) {
            this.employee = employee;
            employeeId = employee == null ? null : employee.getId();
            employee__resolvedKey = employeeId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getExternalId() {
        return this.externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1878371755)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookingEntityDao() : null;
    }
}
