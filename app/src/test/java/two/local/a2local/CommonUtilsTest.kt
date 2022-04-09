package two.local.a2local;

import android.text.TextUtils
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.math.BigDecimal


@RunWith(PowerMockRunner::class)
@PrepareForTest(TextUtils::class)
class CommonUtilsTest {

    @Before
    fun setup() {
        PowerMockito.mockStatic(TextUtils::class.java)
        PowerMockito.`when`(TextUtils.isEmpty(any(CharSequence::class.java))).thenAnswer { invocation ->
            val a = invocation.arguments[0] as? CharSequence
            a?.isEmpty() ?: true
        }

    }

    @Test
    fun `format Date As Day month name`() {
        val result = CommonUtils.formatDateAsDayMonthName("2020-01-26")
        Truth.assertThat(result).isEqualTo("26 Jan")
    }

    @Test
    fun `get month name`() {
        val result1 = CommonUtils.getMonthName(0)
        Truth.assertThat(result1).isEqualTo("")
        val result2 = CommonUtils.getMonthName(12)
        Truth.assertThat(result2).isEqualTo("Dec")
        val result3 = CommonUtils.getMonthName(162)
        Truth.assertThat(result3).isEqualTo("")
    }

    @Test
    fun `remove characters if exists`() {
        val result1 = PriceFormatUtils.removeCharactersPriceIfExists("255,654.23")
        Truth.assertThat(result1).isEqualTo("255654.23")
        val result2 = PriceFormatUtils.removeCharactersPriceIfExists("654.23")
        Truth.assertThat(result2).isEqualTo("654.23")
        val result22 = PriceFormatUtils.removeCharactersPriceIfExists("654.53")
        Truth.assertThat(result22).isEqualTo("654.53")
        val result222 = PriceFormatUtils.removeCharactersPriceIfExists("654.666")
        Truth.assertThat(result222).isEqualTo("654.666")
        val result4 = PriceFormatUtils.removeCharactersPriceIfExists("")
        Truth.assertThat(result4).isEqualTo("0.0")
        val result5 = PriceFormatUtils.removeCharactersPriceIfExists(null)
        Truth.assertThat(result5).isEqualTo("0.0")
        val result6 = PriceFormatUtils.removeCharactersPriceIfExists("۶۵۴.۶۷۸")
        Truth.assertThat(result6).isEqualTo("654.678")
    }

    @Test
    fun `string to decimal`() {
        val result1 = PriceFormatUtils.stringToBigDecimal("255,654.23")
        Truth.assertThat(result1).isEqualTo(BigDecimal("255654.23"))
        val result2 = PriceFormatUtils.stringToBigDecimal("654.23")
        Truth.assertThat(result2).isEqualTo(BigDecimal("654.23"))
        val result22 = PriceFormatUtils.stringToBigDecimal("654.53")
        Truth.assertThat(result22).isEqualTo(BigDecimal("654.53"))
        val result222 = PriceFormatUtils.stringToBigDecimal("654.666")
        Truth.assertThat(result222).isEqualTo(BigDecimal("654.666"))
        val result3 = PriceFormatUtils.stringToBigDecimal("0.23")
        Truth.assertThat(result3).isEqualTo(BigDecimal("0.23"))
        val result4 = PriceFormatUtils.stringToBigDecimal(null)
        Truth.assertThat(result4).isEqualTo(BigDecimal("0.0"))
    }

    @Test
    fun `farsi character to english`() {
        val result1 = CommonUtils.toEnglish("۱۲۳۴۵۶۷۸۹۰")
        Truth.assertThat(result1).isEqualTo("1234567890")
    }

    @Test
    fun `format to decimal price two digits`() {
        val result1 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("456654.20"))
        Truth.assertThat(result1).isEqualTo("456,654.20")
        val result2 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("456654"))
        Truth.assertThat(result2).isEqualTo("456,654.00")
        val result3 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("456654.23680"))
        Truth.assertThat(result3).isEqualTo("456,654.23")
        val result4 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("456654.73680"))
        Truth.assertThat(result4).isEqualTo("456,654.73")
        val result5 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("787345456654"))
        Truth.assertThat(result5).isEqualTo("787,345,456,654.00")
        val result6 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("0.235"))
        Truth.assertThat(result6).isEqualTo("0.23")
        val result7 = PriceFormatUtils.formatToDecimalPriceTwoDigits(BigDecimal("0.۲۳۵"))
        Truth.assertThat(result7).isEqualTo("0.23")
    }

    @Test
    fun `format to decimal price six digits`() {
        val result1 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("456654.20"))
        Truth.assertThat(result1).isEqualTo("456,654.20")
        val result2 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("456654"))
        Truth.assertThat(result2).isEqualTo("456,654.00")
        val result3 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("456654.23680"))
        Truth.assertThat(result3).isEqualTo("456,654.2368")
        val result4 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("456654.236809"))
        Truth.assertThat(result4).isEqualTo("456,654.236809")
        val result5 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("787345456654.236809"))
        Truth.assertThat(result5).isEqualTo("787,345,456,654.236809")
        val result6 = PriceFormatUtils.formatToDecimalPriceSixDigits(BigDecimal("787345456654"))
        Truth.assertThat(result6).isEqualTo("787,345,456,654.00")
    }

}