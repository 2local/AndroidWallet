package com.android.l2l.twolocal.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.android.l2l.twolocal.databinding.ChartItemViewBinding


class ChartItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    private var _binding: ChartItemViewBinding? = null
    private val binding
        get() = _binding!!

    init {
        _binding = ChartItemViewBinding.inflate(LayoutInflater.from(context), this)
    }


    fun setIncomeChart(maxValue: Double, element: Double) {
        val maxLength = 120
        val minValue = 4

        var inc: Double = element
        val layoutParams = binding.viewIncomeFeb.layoutParams

        if (inc != 0.0 && maxValue!=0.0) {
            inc = maxLength * (inc / maxValue)

            if (inc == 0.0 || inc < minValue) inc = minValue.toDouble()
            layoutParams.height = inc.toInt()
            binding.viewIncomeFeb.visibility = View.VISIBLE
        }else {
            layoutParams.height = 0
            binding.viewIncomeFeb.visibility = View.GONE
        }

        binding.viewIncomeFeb.layoutParams = layoutParams

    }

    fun setExpenseFebChart(maxValue: Double, element: Double) {
        val maxLength = 120
        val minValue = 4

        var inc: Double = element
        val layoutParams = binding.viewExpenseFeb.layoutParams

        if (inc != 0.0 && maxValue!=0.0) {
            inc = maxLength * (inc / maxValue)

            if (inc == 0.0 || inc < minValue) inc = minValue.toDouble()
            layoutParams?.height = inc.toInt()
            binding.viewExpenseFeb.visibility = View.VISIBLE
        }else {
            layoutParams.height = 0
            binding.viewExpenseFeb.visibility = View.GONE
        }

        binding.viewExpenseFeb.layoutParams = layoutParams

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        _binding = null
    }
}