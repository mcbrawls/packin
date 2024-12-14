package net.mcbrawls.packin.font.shift

import net.mcbrawls.packin.font.shift.range.ShiftRange
import net.minecraft.util.Identifier
import org.joml.Vector2f
import java.util.SortedSet

/**
 * A font shift handler made only for vertical shifts.
 */
class VerticalFontShiftHandler(
    override val fontId: Identifier,
    ranges: Set<ShiftRange>,
) : FontShiftHandler {
    constructor(fontId: Identifier, vararg ranges: ShiftRange) : this(fontId, ranges.toSet())

    val shifts: SortedSet<Float> = ranges.flatMap(ShiftRange::collectShifts).toSortedSet()

    override val shiftVectors: Set<Vector2f> = shifts.map { Vector2f(0.0f, it) }.toSet()

    operator fun get(shift: Float): Identifier {
        return this[Vector2f(0.0f, shift)]
    }
}
