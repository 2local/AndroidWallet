package com.android.l2l.twolocal.ui.setting.contacts;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.InputValidationRegex.isValidateWalletNumber
import com.android.l2l.twolocal.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class AddContactViewModel
@Inject constructor(
    private val contactRepository: ContactRepositoryHelper,
) : BaseViewModel() {


    private val _formState = SingleLiveEvent<ContactFormState>()
    val formState: LiveData<ContactFormState>
        get() = _formState


    private val _contactListLiveData = MutableLiveData<ViewState<Boolean>>()
    val contactListLiveData: LiveData<ViewState<Boolean>>
        get() = _contactListLiveData

    fun addContact(profileInfo: AddressBook) {
        val loginEnable = addContactDataChanged(profileInfo)
        if (loginEnable is ContactFormState.IsDataValid) {
            if (loginEnable.isValid)
                doAddContact(profileInfo)
        }
    }

    private fun doAddContact(addressBook: AddressBook) {
        contactRepository.addContact(addressBook).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _contactListLiveData.value = ViewState.Loading
            }
            .doOnError { _contactListLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({
                _contactListLiveData.value = ViewState.Success(it>0)
            }, { })

    }


    private fun addContactDataChanged(profileInfo: AddressBook): ContactFormState {
        var formState: ContactFormState = ContactFormState.IsDataValid(true)
        if (profileInfo.name.isBlank()) {
            formState = ContactFormState.NameError(R.string.error_empty_input)
            _formState.value = formState
        }

        if (profileInfo.type == null) {
            formState = ContactFormState.CurrencyError(R.string.error_empty_input)
            _formState.value = formState
        }


        if (profileInfo.wallet_number.isBlank()) {
            formState = ContactFormState.AddressError(R.string.error_empty_input)
            _formState.value = formState
        } else if (!isValidateWalletNumber(profileInfo.wallet_number)) {
            formState = ContactFormState.AddressError(R.string.invalid_wallet_number)
            _formState.value = formState
        }

        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}