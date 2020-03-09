package studio.saladjam.iwanttobenovelist

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import studio.saladjam.iwanttobenovelist.binding.getLastUpdatedTime

@RunWith(AndroidJUnit4::class)
class TimeUnitInstrumentedTest {
    @Test
    fun convert1MillToOtherUnits() {
        val diffTime = 1L
        val (value, unit) = getLastUpdatedTime(diffTime)
        assertEquals(0, value)
        assertEquals(IWBNApplication.instance.getString(R.string.unit_second), unit)
    }
}