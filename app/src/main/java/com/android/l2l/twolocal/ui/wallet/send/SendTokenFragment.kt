package com.android.l2l.twolocal.ui.wallet.send

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.*
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentSendTokenBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.SendCryptoCurrency
import com.android.l2l.twolocal.model.enums.InputType
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.scanner.ScanActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.send.confirm.SendTokenConfirmFragment
import com.android.l2l.twolocal.ui.wallet.send.viewmodel.SendFormState
import com.android.l2l.twolocal.ui.wallet.send.viewmodel.SendViewModel
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.InputTextWatcher
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class SendTokenFragment : BaseFragment<SendViewModel>(R.layout.fragment_send_token) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: SendViewModel by viewModels { viewModelFactory }
    private val binding: FragmentSendTokenBinding by viewBinding(FragmentSendTokenBinding::bind)

    private var inputType = InputType.CURRENCY

    private lateinit var wallet: Wallet
    private var sentCryptoCurrency: SendCryptoCurrency? = null
    var contactList = mutableListOf<AddressBook>()
    lateinit var addressBookSpinnerAdapter: AddressBookSpinnerAdapter

    companion object {
        const val SEND_CRYPTO_KEY = "SEND_CRYPTO_KEY"

    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handelBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), sentCryptoCurrency?.type).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handelBundle(arguments)

        binding.buttonSend.setOnClickListener {
            saveForm()
            viewModel.sendEther(sentCryptoCurrency!!)
        }

        binding.imageScan.setOnClickListener {
            val intent = Intent(context, ScanActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.btnSelectMax.setOnClickListener {
            binding.edtFiatAmount.setText("")
            binding.edtCurrencyAmount.setText(wallet.amount)
        }

        binding.imageMenu.setOnClickListener {
            showMenu(binding.imageMenu)
        }
        binding.toolbar.getBackBtn().setOnClickListener {
            requireActivity().finish()
        }

        binding.imgClearAddress.setOnClickListener {
            binding.edtWalletNumber.setText("")
        }


        binding.imgSwitch.setOnClickListener {
            inputType = when (inputType) {
                InputType.CURRENCY -> {
                    setFiatInputActive()
                    InputType.FIAT
                }
                InputType.FIAT -> {
                    setCurrencyInputActive()
                    InputType.CURRENCY
                }
            }
        }

        binding.edtWalletNumber.onTextChanged {
            if (it.isNotBlank()) {
                val padding = requireContext().resources.getDimension(R.dimen._33dp)
                binding.imgClearAddress.visible()
                binding.imageMenu.gone()
                binding.imageScan.gone()
                binding.edtWalletNumber.setPadding(
                    binding.edtWalletNumber.paddingLeft,
                    binding.edtWalletNumber.paddingTop,
                    padding.toInt(),
                    binding.edtWalletNumber.paddingBottom
                )
            } else {
                binding.imgClearAddress.gone()
                binding.imageMenu.visible()
                binding.imageScan.visible()
            }
        }

        setUpEditTextOnKeyListener()
        setupSpinnerView()
        setUpLiveData()
        setInfo()
        sentCryptoCurrency?.let {
            viewModel.getWalletDetail(it.type)
        }
    }

    private fun setUpEditTextOnKeyListener(){
        binding.edtCurrencyAmount.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.edtFiatAmount.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.edtWalletNumber.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setupSpinnerView(){
        var fistTime = 0
        addressBookSpinnerAdapter = AddressBookSpinnerAdapter(requireContext(), contactList)
        binding.spinnerContacts.adapter = addressBookSpinnerAdapter
        binding.spinnerContacts.setSelection(0, false)
        binding.spinnerContacts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (++fistTime > 0) {
                    binding.edtWalletNumber.setText(contactList[position].wallet_number)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handelBundle(arguments: Bundle?) {
        arguments?.let {
            if (sentCryptoCurrency == null)
                sentCryptoCurrency = it.get(SEND_CRYPTO_KEY) as SendCryptoCurrency
        }
    }

    private fun saveForm() {
        sentCryptoCurrency?.address = binding.edtWalletNumber.text.toString().trim()
        sentCryptoCurrency?.amount = CommonUtils.removeCharactersPriceIfExists(sentCryptoCurrency?.amount)
    }

    private fun setUpLiveData() {
        viewModel.formState.observe(viewLifecycleOwner, {
            when (it) {
                is SendFormState.AmountError -> {
                    binding.edtCurrencyAmount.setEditTextError(it.error)
                }
                is SendFormState.AddressError -> {
                    binding.edtWalletNumber.setEditTextError(it.error)
                }
                else -> {
                }
            }
        })

        viewModel.walletDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    wallet = it.response
                    viewModel.getWalletBalance(wallet.type)

                    sentCryptoCurrency?.copyCoinInfo(wallet)
                    onWalletInfoLoaded(wallet)
                }
                is ViewState.Error -> {
                    onMessageToast(it.error.message)
                }
                else -> {
                }
            }
        })

        viewModel.balanceLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    binding.progressBalance.visible()
                }
                is ViewState.Success -> {
                    binding.progressBalance.gone()
                    wallet.amount = it.response.amount
                    onWalletBalanceLoaded(it.response)
                }
                is ViewState.Error -> {
                    binding.progressBalance.gone()
                    onMessageToast(it.error?.message)
                }
            }
        })

        viewModel.contactListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    if (it.response.isNotEmpty()) {
                        if (contactList.isEmpty()) {
                            contactList.addAll(it.response)
                            addressBookSpinnerAdapter.notifyDataSetChanged()
                        }
                        setUpContactListSpinner()
                    } else
                        onMessageToast(R.string.send_token_where_is_no_contact)
                }
                is ViewState.Error -> {
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        viewModel.sendTokenLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    findNavController().navigate(
                        R.id.action_sendFragment_to_sendTokenConfirmFragment,
                        bundleOf(
                            SendTokenConfirmFragment.SEND_CRYPTO_KEY to sentCryptoCurrency,
                        )
                    )
                }
                else -> {
                }
            }
        })
    }

    private fun setUpContactListSpinner() {
        binding.spinnerContacts.performClick()
    }

    private fun setInfo() {
        sentCryptoCurrency?.let {
            binding.edtWalletNumber.setText(it.address)
        }

        if (inputType == InputType.CURRENCY)
            setCurrencyInputActive()
        else if (inputType == InputType.FIAT) {
            setFiatInputActive()
        }
    }

    private fun setFiatInputActive() {
        binding.edtCurrencyAmount.isEnabled = false
        binding.edtCurrencyAmount.removeTextChangedListener(l2lWatcher)
        binding.edtFiatAmount.isEnabled = true
        binding.edtFiatAmount.addTextChangedListener(usdWatcher)
    }

    private fun setCurrencyInputActive() {
        binding.edtCurrencyAmount.isEnabled = true
        binding.edtCurrencyAmount.addTextChangedListener(l2lWatcher)
        binding.edtFiatAmount.isEnabled = false
        binding.edtFiatAmount.removeTextChangedListener(usdWatcher)
    }

    private fun onWalletInfoLoaded(wallet: Wallet) {
        binding.txtTokenSymbol.text = wallet.type.symbol
        binding.txtCurrency.text = wallet.currency.myName
        onWalletBalanceLoaded(wallet)
    }

    private fun onWalletBalanceLoaded(wallet: Wallet) {
        binding.txtAvailableAmount.text = getString(R.string.available_amount, wallet.amountPriceFormat, wallet.type.symbol)
    }

    fun onPriceFormatted(sentCryptoCurrency: SendCryptoCurrency?) {
        sentCryptoCurrency?.let {
            binding.edtCurrencyAmount.hint = ""
            binding.edtFiatAmount.hint = ""
            when (inputType) {
                InputType.FIAT -> {
                    binding.edtCurrencyAmount.setText("")
                    binding.edtCurrencyAmount.hint = it.amountPriceFormat
                }
                InputType.CURRENCY -> {
                    binding.edtFiatAmount.setText("")
                    binding.edtFiatAmount.hint = it.fiatPriceFormat
                }
            }
        }
    }

    private val usdWatcher: TextWatcher = object : InputTextWatcher() {
        override fun textChanged(amount: String) {
            sentCryptoCurrency?.fiatPrice = amount
            onPriceFormatted(sentCryptoCurrency)
        }
    }
    private val l2lWatcher: TextWatcher = object : InputTextWatcher() {
        override fun textChanged(amount: String) {
            sentCryptoCurrency?.amount = amount
            onPriceFormatted(sentCryptoCurrency)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            binding.edtWalletNumber.setText(data!!.extras!!.getString(ScanActivity.KEY_BUNDLE_RESULT))
        }
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.send_token_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.menu_paste) {
                binding.edtWalletNumber.setText(CommonUtils.pasteFromClipboard(context))
            }
            if (menuItem.itemId == R.id.menu_address_book) {
                viewModel.getContactList(wallet.type)
            }
            return@setOnMenuItemClickListener true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }
}