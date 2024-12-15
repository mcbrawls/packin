package net.mcbrawls.packin.font.shift

import net.mcbrawls.packin.font.shift.range.ShiftRange
import net.mcbrawls.packin.resource.provider.FontProvider
import net.minecraft.util.Identifier
import org.joml.Vector2f

/**
 * Handles generation and collection of font shifts dynamically.
 */
interface FontShiftHandler {
    val fontId: Identifier
    val shiftVectors: Set<Vector2f>

    operator fun get(shift: Vector2f = Vector2f()): Identifier {
        val closest = getClosest(shift)
        return FontProvider.createShiftedFontId(fontId, closest)
    }

    fun getClosest(shift: Vector2f): Vector2f {
        return shiftVectors.minByOrNull { it.distanceSquared(shift) } ?: Vector2f()
    }

    companion object {
        fun createVertical(fontId: Identifier, vararg ranges: ShiftRange): VerticalFontShiftHandler {
            return VerticalFontShiftHandler(fontId, *ranges)
        }
    }
}
