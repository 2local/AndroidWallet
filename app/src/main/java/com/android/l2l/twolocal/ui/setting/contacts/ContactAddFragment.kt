package com.android.l2l.twolocal.ui.setting.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.onMessageToast
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentAddContactBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.scanner.ScanActivity
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactAddFragment : BaseFragment<AddContactViewModel>(R.layout.fragment_add_contact) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: AddContactViewModel by viewModels { viewModelFactory }
    private val binding: FragmentAddContactBinding by viewBinding(FragmentAddContactBinding::bind)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelLiveData()

        var currency: CryptoCurrencyType? = null
        val currencyListType = CryptoCurrencyType.walletArray()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_layout, currencyListType.map {
            it.symbol
        })
        binding.spCurrency.setAdapter(adapter)
        binding.spCurrency.onItemClickListener =
            AdapterView.OnItemClickListener { parent, arg1, position, id ->
                currency = currencyListType[position]
            }


        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val walletNumber = binding.editTextWalletNumber.text.toString().trim()
            val addressBook = AddressBook(name, walletNumber, currency)
            viewModel.addContact(addressBook)
        }

        binding.textPaste.setOnClickListener {
            val address = CommonUtils.pasteFromClipboard(context)
            binding.editTextWalletNumber.setText(address)
        }

        binding.imageScan.setOnClickListener {
            Intent(requireContext(), ScanActivity::class.java).apply {
                someActivityResultLauncher.launch(this)
            }
        }
        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViewModelLiveData(){
        viewModel.formState.observe(viewLifecycleOwner, {
            when (it) {
                is ContactFormState.NameError -> {
                    binding.editTextName.setEditTextError(it.error)
                }
                is ContactFormState.AddressError -> {
                    binding.editTextWalletNumber.setEditTextError(it.error)
                }
                is ContactFormState.CurrencyError -> {
                    binding.spCurrency.setEditTextError(it.error)
                }
                else -> {
                }
            }
        })

        viewModel.contactListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    findNavController().popBackStack()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })
    }

    private var someActivityResultLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null && data.extras != null && data.extras!!.containsKey(ScanActivity.KEY_BUNDLE_RESULT))
                binding.editTextWalletNumber.setText(data.extras!!.getString(ScanActivity.KEY_BUNDLE_RESULT))
        }
    }

}