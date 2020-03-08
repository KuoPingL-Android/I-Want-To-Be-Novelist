package studio.saladjam.iwanttobenovelist

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class TimeUnitTest {
    @Test
    fun convert1MillToOtherUnits() {
        val oneMills = 1L
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(0L, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert1000MillToOtherUnits() {
        val oneMills = 1L * 1000    // 1 SECOND
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(1L, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert60000MillToOtherUnits() {
        val oneMills = 1L * 1000 * 60   // 1 MINUTE
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert60MinuteMillToOtherUnits() {
        val oneMills = 1L * 1000 * 60 * 60   // 1 HOUR
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L * 60, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(60L, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert24HoursMillToOtherUnits() {
        val oneMills = 1L * 1000 * 60 * 60 * 24   // 1 DAY
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L * 60 * 24, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(60L * 24, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L * 24, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert25HoursMillToOtherUnits() {
        val oneMills = 1L * 1000 * 60 * 60 * 25  // 1 DAY
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L * 60 * 25, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(60L * 25, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L * 25, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert12DaysMillToOtherUnits() {
        val oneMills = 1L * 1000 * 60 * 60 * 24 * 12 // 1 DAY
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L * 60 * 24 * 12, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(60L * 24 * 12, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L * 24 * 12, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(12L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(0L, years)
    }

    @Test
    fun convert1YearMillToOtherUnits() {
        val oneMills = 1L * 1000 * 60 * 60 * 24 * 365 // 1 DAY
        val years = TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS) / 365
        assertEquals(60L * 60 * 24 * 365, TimeUnit.SECONDS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(60L * 24 * 365, TimeUnit.MINUTES.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L * 24 * 365, TimeUnit.HOURS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(365L, TimeUnit.DAYS.convert(oneMills, TimeUnit.MILLISECONDS))
        assertEquals(1L, years)
    }
}