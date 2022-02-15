package com.android.l2l.twolocal.ui.wallet.transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.gone
import com.android.l2l.twolocal.common.onMessageToast
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.ActivityTransactionHistoryBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.wallet.detail.TransactionsRecyclerAdapter
import com.android.l2l.twolocal.ui.wallet.detail.transactionDetail.BottomSheetTransactionDetail
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TransactionHistoryActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val viewModel: TransactionHistoryViewModel by viewModels { viewModelFactory }
    private val binding: ActivityTransactionHistoryBinding by viewBinding(ActivityTransactionHistoryBinding::inflate)
    private val transactionHistoryList: MutableList<WalletTransactionHistory> = arrayListOf()
    private lateinit var adapter: TransactionsRecyclerAdapter
        companion object {
        fun start(context: Context) {
            Intent(context, TransactionHistoryActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerWalletComponent.factory().create(findAppComponent(), CryptoCurrencyType.TwoLC).inject(this)
        super.onCreate(savedInstanceState)

       setUpAdapter()

        viewModel.transactionHistoryLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    updateList(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
            }
        })

        viewModel.inComeLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onChartDataLoaded(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
            }
        })


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    viewModel.getAllTransactionHistory()
                } else if (tab.position == 1) {
                    viewModel.getSendTransactionHistory()
                } else if (tab.position == 2) {
                    viewModel.getReceiveTransactionHistory()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.tabsLastWeek.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    viewModel.getAllIncome()
                } else if (tab.position == 1) {
                    viewModel.getAllExpense()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.toolbar.getBackBtn().setOnClickListener {
            finish()
        }

        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.activity_transaction_detail_expenses_all)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.activity_transaction_detail_tab_send)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.activity_transaction_detail_tab_receive)))

        binding.tabsLastWeek.addTab(binding.tabsLastWeek.newTab().setText(getString(R.string.activity_transaction_detail_income)))
        binding.tabsLastWeek.addTab(binding.tabsLastWeek.newTab().setText(getString(R.string.activity_transaction_detail_expenses)))

//        viewModel.getAllTransactionHistory()
    }

    private fun setUpAdapter(){
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        adapter = TransactionsRecyclerAdapter(this, transactionHistoryList)
        binding.recyclerHistory.adapter = adapter
        adapter.setItemClickListener { _, position ->
            val bottomSheetTransactionDetail: BottomSheetTransactionDetail =
                BottomSheetTransactionDetail.newInstance(transactionHistoryList[position].type, transactionHistoryList[position].hash)
            bottomSheetTransactionDetail.show(this.supportFragmentManager, bottomSheetTransactionDetail.tag)
        }
    }

    private fun updateList(historyList: List<WalletTransactionHistory> ){
        transactionHistoryList.clear()
        transactionHistoryList.addAll(historyList)
        if (transactionHistoryList.isEmpty()) binding.lntNoData.visible() else binding.lntNoData.gone()
        adapter.notifyDataSetChanged()
    }
    val quarters = arrayOf(
        "Sun", "Mon", "Tue", "Wed", "Thu",
        "Fri", "Sat"
    )

    private fun onChartDataLoaded(transactions: List<String>) {
        val valsComp1: MutableList<Entry> = ArrayList()

        val image = ContextCompat.getDrawable(this, R.drawable.ic_dot_blue)
        for (i in transactions.indices) {
            val entry = Entry()
            entry.icon = image
            valsComp1.add(Entry(i.toFloat(), transactions[i].replace(",".toRegex(), "").toFloat(), image))
        }
        val setComp1 = LineDataSet(valsComp1, "")
        setComp1.setCircleColor(ContextCompat.getColor(this, R.color.color_blue))
        setComp1.lineWidth = 2.5.toFloat()
        setComp1.color = ContextCompat.getColor(this, R.color.light_blue)
        setComp1.valueTextColor = ContextCompat.getColor(this, R.color.grey_text)
        setComp1.valueTextSize = 12f
        setComp1.setDrawCircleHole(false)
        setComp1.axisDependency = YAxis.AxisDependency.LEFT
        setComp1.setDrawVerticalHighlightIndicator(false)
        setComp1.setDrawValues(true)
        setComp1.mode = LineDataSet.Mode.LINEAR
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                axis.textSize = 12f
                return quarters.get(value.toInt())
            }
        }
        val dataSets: MutableList<ILineDataSet> = ArrayList()
        dataSets.add(setComp1)
        val xAxis: XAxis = binding.chart.xAxis
        xAxis.valueFormatter = formatter
        xAxis.textColor = ContextCompat.getColor(this, R.color.text_color_disabled)
        val data = LineData(dataSets)
        binding.chart.isLogEnabled = false
        val description = Description()
        description.text = ""
        binding.chart.description = description
        binding.chart.setDrawGridBackground(false)
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.xAxis.setDrawAxisLine(false)
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.xAxis.setDrawLimitLinesBehindData(false)
        binding.chart.xAxis.setDrawGridLinesBehindData(false)
        binding.chart.axisLeft.setDrawGridLines(false)
        binding.chart.axisRight.setDrawGridLines(false)
        binding.chart.axisRight.setDrawAxisLine(false)
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.legend.setEnabled(false)
        binding.chart.axisLeft.setDrawLabels(false)
        binding.chart.axisRight.setDrawLabels(false)
        binding.chart.animateX(0)
        binding.chart.data = data
        binding.chart.invalidate() // refresh
    }
}