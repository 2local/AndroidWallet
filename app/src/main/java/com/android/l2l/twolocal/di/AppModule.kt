package com.android.l2l.twolocal.di

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import com.android.l2l.twolocal.BuildConfig
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.BASE_URL
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.REQUEST_TIMEOUT_DURATION
import com.android.l2l.twolocal.dataSourse.remote.api.ApiHeaders
import com.android.l2l.twolocal.dataSourse.remote.auth.AuthenticationApiInterface
import com.android.l2l.twolocal.dataSourse.utils.NetInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@ExperimentalCoroutinesApi
class AppModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(apiHeaders: ApiHeaders): OkHttpClient {

        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(NetInterceptor(apiHeaders))
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(NetInterceptor(apiHeaders))
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        }

    }

    @Provides
    fun getContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create()
    }


    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient, gson: Gson): Retrofit {

        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
//            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}