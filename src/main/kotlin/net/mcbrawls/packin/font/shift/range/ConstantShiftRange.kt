package net.mcbrawls.packin.font.shift.range

class ConstantShiftRange(val value: Float) : ShiftRange {
    override fun collectShifts(): Set<Float> {
        return setOf(value)
    }
}
