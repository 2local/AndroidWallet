package com.android.l2l.twolocal.di

import android.content.Context
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.db.DatabaseHelper
import com.android.l2l.twolocal.dataSourse.local.db.DbOpenHelper
import com.android.l2l.twolocal.dataSourse.local.prefs.Session
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.model.DaoMaster
import com.android.l2l.twolocal.model.DaoSession
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideWritableDaoSession(openHelper: DbOpenHelper): DaoSession {
        return DaoMaster(openHelper.writableDb).newSession()
    }

    @Provides
    @Singleton
    fun provideDbHelper(daoSession: DaoSession): DatabaseHelper {
        return AppDatabase(daoSession)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(session: Session): UserSessionHelper {
        return UserSession(session)
    }

    @Provides
    @Singleton
    fun provideAppSession(context: Context): Session {
        return Session(context)
    }
}