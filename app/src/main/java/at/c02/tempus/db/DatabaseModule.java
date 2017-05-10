package at.c02.tempus.db;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import javax.inject.Singleton;

import at.c02.tempus.db.entity.DaoMaster;
import at.c02.tempus.db.entity.DaoSession;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.db.repository.ProjectRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 09.04.2017.
 */
@Module
public class DatabaseModule {
    private static final String TAG = "databaseModule";

    @Provides
    @Singleton
    public DaoSession provideDaoSession(Context context) {
        Log.d(TAG, "initializing DAO Session");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "tempus-db");
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return daoSession;
    }

    @Provides
    @Singleton
    public BookingRepository provideBookingRepository(DaoSession daoSession) {
        return new BookingRepository(daoSession.getBookingEntityDao());
    }

    @Provides
    @Singleton
    public EmployeeRepository provideEmployeeRepository(DaoSession daoSession) {
        return new EmployeeRepository(daoSession.getEmployeeEntityDao());
    }

    @Provides
    @Singleton
    public ProjectRepository provideProjectRepository(DaoSession daoSession) {
        return new ProjectRepository(daoSession.getProjectEntityDao());
    }
}
