package team.aliens.dms.common.extension

import java.time.LocalDate

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> {
    return object : Iterator<LocalDate> {
        private var next = this@iterator.start
        private val finalElement = this@iterator.endInclusive
        private var hasNext = !next.isAfter(finalElement)

        override fun hasNext(): Boolean = hasNext
        override fun next(): LocalDate {
            if (!hasNext) {
                throw NoSuchElementException()
            }
            val value = next
            if (value == finalElement) {
                hasNext = false
            } else {
                next = next.plusDays(1)
            }
            return value
        }
    }
}
