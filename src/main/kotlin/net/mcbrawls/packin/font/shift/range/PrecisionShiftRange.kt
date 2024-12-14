package net.mcbrawls.packin.font.shift.range

import java.math.BigDecimal

class PrecisionShiftRange(val range: IntRange, val precision: Float = 1.0f) : ShiftRange {
    /**
     * The precision value as a big decimal.
     */
    private val precisionDecimal = precision.toBigDecimal()

    /**
     * All precisions between 0.0 and 1.0 for this provider.
     */
    private val offsetPrecisions: Set<Float> = buildSet {
        if (precision != 1.0f) {
            // use dumb big decimal math because accuracy
            var decimal = 0.0.toBigDecimal()
            while (decimal < BigDecimal.ONE) {
                add(decimal.toFloat())
                decimal += precisionDecimal
            }
        } else {
            add(0.0f)
        }
    }

    override fun collectShifts(): Set<Float> {
        return buildSet {
            val end = range.endInclusive

            range.forEach { base ->
                if (base == end) {
                    return@forEach
                }

                offsetPrecisions.forEach { precision ->
                    // use dumb big decimal math because accuracy
                    var decimal = base.toBigDecimal()
                    decimal += precision.toBigDecimal()
                    add(decimal.toFloat())
                }
            }

            add(end.toFloat())
        }
    }
}
