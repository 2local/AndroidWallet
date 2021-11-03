package com.android.l2l.twolocal.di.component

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.android.l2l.twolocal.App
import com.android.l2l.twolocal.dataSourse.local.db.DatabaseHelper
import com.android.l2l.twolocal.dataSourse.local.prefs.Session
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.google.gson.Gson
import com.android.l2l.twolocal.di.AppModule
import com.android.l2l.twolocal.di.PersistenceModule
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.di.viewModel.ViewModelFactoryModule
import com.android.l2l.twolocal.di.viewModel.ViewModelModule
import com.android.l2l.twolocal.model.DaoSession
import com.android.l2l.twolocal.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [AppModule::class, PersistenceModule::class, ViewModelModule::class, ViewModelFactoryModule::class]
)
interface AppComponent {
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)

    fun provideRetrofit(): Retrofit
    fun provideOkHttpClient(): OkHttpClient
    fun provideGson(): Gson

    fun provideWritableDaoSession(): DaoSession
    fun provideDbHelper(): DatabaseHelper
    fun provideAppSession(): Session
    fun provideAppPreferences(): UserSessionHelper

    fun bindViewModelFactory(): ViewModelProvider.Factory
    fun applicationContext(): Application
    fun getContext(): Context
//    fun bindLoginViewModel(): LoginViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}