package com.android.l2l.twolocal.dataSourse.repository.contact

import com.android.l2l.twolocal.dataSourse.local.db.AppDatabase
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactRepository @Inject constructor(
    private val database: AppDatabase,
) :ContactRepositoryHelper {


    override fun getContactList(): Single<List<AddressBook>> {
        return database.getContactsFromDb()
    }

    override fun getContactList(walletType: CryptoCurrencyType): Single<List<AddressBook>> {
        return database.getContactsFromDb(walletType)
    }

    override fun addContact(addressBook: AddressBook): Single<Long> {
       return database.addContactToDb(addressBook)
    }

    override fun deleteSingleContact(addressBook: AddressBook): Single<Boolean> {
        return database.deleteSingleContact(addressBook)
    }


}