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
import android.view.inputmethod.EditorInfo

import android.widget.TextView
import com.android.l2l.twolocal.common.hideKeyboard


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
                    handleOnSuccess(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    if (it.error.message.isNullOrBlank())
                        onErrorDialog(getString(R.string.fragment_market_place_failed_to_get_market_place_list))
                    else
                        onErrorDialog(it.error.message)
                }
            }
        })

        binding.imgSearch.setOnClickListener {
            performSearch()
        }

        binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun performSearch() {
        val searchQuery = binding.edtSearch.text
        val searchResultList = viewModel.filterMarketPlace(marketPlaceList, searchQuery)

        if (searchQuery.isBlank()) {
            addMarkers(marketPlaceList)
            moveCameraToCenter(marketPlaceList)
        } else {
            if(searchResultList.isEmpty())
                onMessageToast(R.string.fragment_market_place_item_not_found)
            addMarkers(searchResultList)
            moveCameraToCenter(searchResultList)
        }
        hideKeyboard()
    }

    private fun handleOnSuccess(response: List<MarketPlace>) {
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
        viewModel.getMarketPlaces()
    }

    private fun addMarkers(marketList: List<MarketPlace>) {
        mMap?.let {

            it.clear()
            marketList.forEach { v ->
                if (v.latitude != null && v.longitude != null) {
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
            val center = computeCenter(marketList)
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 4f))
        }
    }

    private fun computeCenter(marketList: List<MarketPlace>): LatLng {
        var latitude = 0.0
        var longitude = 0.0
        var n = 0
        for (point in marketList) {
            if (point.latitude != null && point.longitude != null && point.longitude?.isNotBlank() == true) {
                latitude += point.latitude!!.toDouble()
                longitude += point.longitude!!.toDouble()
                n++
            }
        }
        if (n == 0)
            n = 1
        return LatLng(latitude / n, longitude / n)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        try {
            if (marketPlaceList.isNotEmpty()) {
                val market = marketPlaceList.findLast {
                    val tt: Double = it.latitude?.toDouble() ?: 0.0
                    tt == p0.position.latitude
                }
                market?.let {
                    val bottomSheetRenameWallet: BottomSheetMarketPlaceDetail = BottomSheetMarketPlaceDetail.newInstance(it)
                    bottomSheetRenameWallet.show(requireActivity().supportFragmentManager, bottomSheetRenameWallet.tag)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}