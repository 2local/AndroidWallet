package com.android.l2l.twolocal.ui.market

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.dataSourse.remote.api.ApiConstants.GOOGLE_QR_CODE
import com.android.l2l.twolocal.databinding.MarketplaceBottomsheetDetailBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.utils.MessageUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class BottomSheetMarketPlaceDetail : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val binding: MarketplaceBottomsheetDetailBinding by viewBinding()
    private var marketPlace: MarketPlace? = null

    companion object {
        const val MARKET_PLACE_KEY = "MARKET_PLACE_KEY"

        fun newInstance(marketPlace: MarketPlace) = BottomSheetMarketPlaceDetail().apply {
            arguments = bundleOf(
                MARKET_PLACE_KEY to marketPlace
            )
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            marketPlace = it.get(MARKET_PLACE_KEY) as MarketPlace
        }

        marketPlace?.let {
            binding.marketPlaceUrl.text = it.address
            binding.companyName.text = it.companyName
            binding.txtRepresentative.text = it.representative
        }


        binding.buttonCall.setOnClickListener {
            try {
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_place_phone))).apply {
                    startActivity(this)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.imgClose.setOnClickListener {
            this.dismiss()
        }

        binding.marketPlaceUrl.setOnClickListener {
            try {
                marketPlace?.let {
                    Intent(Intent.ACTION_VIEW, Uri.parse(it.address)).apply {
                        startActivity(this)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                MessageUtils.showErrorDialog(requireContext(), getString(R.string.fragment_market_place_can_not_launch_the_web_url))
            }
        }

        binding.buttonDirection.setOnClickListener {
            try {
                marketPlace?.let {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_QR_CODE + it.latitude + "," + it.longitude)
                    ).apply {
                        startActivity(this)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}