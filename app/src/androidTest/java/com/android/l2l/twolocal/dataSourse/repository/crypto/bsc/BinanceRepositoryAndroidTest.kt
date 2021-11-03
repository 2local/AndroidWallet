package com.android.l2l.twolocal.dataSourse.repository.crypto.bsc;

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.remote.currency.CryptoCurrencyRemoteDataSourceHelper
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.web3j.protocol.Web3j
import java.lang.reflect.Field


@RunWith(MockitoJUnitRunner::class)
class BinanceRepositoryAndroidTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
//@Rule
//var rxSchedulerRule: RxSchedulerRule = RxSchedulerRule()

    lateinit var context: Context

    //    private lateinit var etherApiInterface: CryptoCurrencyApiInterface
    private lateinit var database: AppDatabase
    lateinit var repository: BinanceRepository
    lateinit var web3j: Web3j

    @Before
    fun start() {
        context =  mockk<Context>()
        database = mockk<AppDatabase>()
        web3j = mockk<Web3j>()
        val userSession = mockk<UserSession>()
        val etherApiInterface = mockk<CryptoCurrencyRemoteDataSourceHelper>()
        repository = BinanceRepository(context, web3j, etherApiInterface, database, userSession, CryptoCurrencyType.TwoLC)

//        RxAndroidPlugins.reset()
//        RxJavaPlugins.reset()
//        RxJavaPlugins.setIoSchedulerHandler(Function<Scheduler, Scheduler> {
//            it
//        })
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> {
//            it.
//        })
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler(object : Function<Callable<Scheduler>, Scheduler>() {
//            @Throws(Exception::class)
//            fun apply(@NonNull schedulerCallable: Callable<Scheduler?>?): Scheduler? {
//                return Schedulers.trampoline()
//            }
//        })

    }

//    @Test
//    fun restoreWalletFromMnemonicTest() {
//        //Given
////        val ageField: Field = BinanceRepository::class.java
////            .getDeclaredField("context")
////        ageField.setAccessible(true)
//
////        ageField.set(repository, context)
//
////        coEvery {
////            context.getFilesDir()
////        } returns File(context.getFilesDir(), "")
//
//        val testSingle = TestObserver<Boolean>()
//        repository.restoreWalletFromMnemonic("later front attract congress advice helmet moon potato brown expect region fish").subscribe(
//            testSingle
//        )
////        testSingle.await().assertValue {
////            it == false
////        }
//        testSingle.await().assertError {
//            it == Throwable()
//        }
////        testSingle.assertComplete()
////        testSingle.assertError {
////            it == Throwable()
////        }
////        testSingle.assertValue(true);
//    }


}