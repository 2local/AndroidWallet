package com.android.l2l.twolocal.ui.market

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentMarketListBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.market.di.DaggerMarketComponent
import com.android.l2l.twolocal.ui.wallet.detail.rename.BottomSheetRenameWallet
import com.android.l2l.twolocal.utils.CommonUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FragmentMarketList : BaseFragment<MarketListViewModel>(R.layout.fragment_market_list), OnMapReadyCallback, OnMarkerClickListener {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: MarketListViewModel by viewModels { viewModelFactory }
    private val binding: FragmentMarketListBinding by viewBinding(FragmentMarketListBinding::bind)

    private val marketPlaceList: MutableList<MarketPlace> = arrayListOf()
    private var mMap: GoogleMap? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerMarketComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.marketPlaceListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    handleONSuccess(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    if (it.error.message.isNullOrBlank())
                        onErrorDialog(getString(R.string.failed_to_get_market_place_list))
                    else
                        onErrorDialog(it.error.message)
                }
            }
        })

        viewModel.getMarketPlaces()

    }

    private fun handleONSuccess(response: List<MarketPlace>) {
        marketPlaceList.addAll(response)
        if (marketPlaceList.isEmpty())
            onErrorDialog(getString(R.string.no_market_place_found))
        else {
            addMarkers(marketPlaceList)
            moveCameraToCenter(marketPlaceList)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.setOnMarkerClickListener(this)

        mMap = googleMap

        addMarkers(marketPlaceList)
        moveCameraToCenter(marketPlaceList)
    }

    private fun addMarkers(vehicles: List<MarketPlace>) {
        mMap?.let {

            it.clear()
            vehicles.forEach { v ->
                if(v.latitude !=null && v.longitude!=null) {
                    val center = LatLng(v.latitude!!.toDouble(), v.longitude!!.toDouble())
                    val bitmap = CommonUtils.getBitmapFromVectorDrawable(context, R.drawable.ic_map_marker_blak)
                    it.addMarker(
                        MarkerOptions()
                            .position(center)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                    )
                }
            }
        }
    }

    private fun moveCameraToCenter(marketList: List<MarketPlace>) {
        if (marketList.isNotEmpty()) {
            val market = marketList[getRandomNumber(0, marketList.size - 1)]
            if(market.latitude !=null && market.longitude!=null) {
                val center = LatLng(market.latitude!!.toDouble(), market.longitude!!.toDouble())
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14f))
            }
        }
    }

    private fun getRandomNumber(min: Int, max: Int): Int {
        return (Math.random() * (max - min + 1) + min).toInt()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (marketPlaceList.isNotEmpty()) {
            val bottomSheetRenameWallet: BottomSheetMarketPlaceDetail = BottomSheetMarketPlaceDetail.newInstance(marketPlaceList[0])
            bottomSheetRenameWallet.show(requireActivity().supportFragmentManager, bottomSheetRenameWallet.tag)
        }
        return true
    }
}