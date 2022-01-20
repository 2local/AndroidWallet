package com.android.l2l.twolocal.ui.wallet.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.setHeightPercent
import com.android.l2l.twolocal.databinding.DialogWalletInstructionBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DialogWalletInstruction : DialogFragment(R.layout.dialog_wallet_instruction) {


    private val binding: DialogWalletInstructionBinding by viewBinding(DialogWalletInstructionBinding::bind)

    companion object {

        const val MESSAGE = "MESSAGE"

        fun newInstance(message: String) = DialogWalletInstruction().apply {
            arguments = bundleOf(
                MESSAGE to message
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        binding.webView.settings.javaScriptEnabled = true

        arguments?.let {
            val message = it.getString(MESSAGE)
            onWalletInfoLoaded(message)
        }
    }

    private fun onWalletInfoLoaded(message: String?) {
        if (message != null) {
            var content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<html><head>" +
                    "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                    "</head><body>" + "$message</body></html>"

            binding.webView.loadData(content, "text/html; charset=UTF-8", null)
        }
    }

    override fun onStart() {
        super.onStart()
        setHeightPercent(75)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    private fun setupClickListeners() {

        binding.imageCross.setOnClickListener {
            dismiss()
        }
    }

}