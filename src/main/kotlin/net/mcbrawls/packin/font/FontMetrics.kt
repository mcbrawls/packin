package net.mcbrawls.packin.font

import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.PackResource
import net.minecraft.util.Identifier
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import kotlin.math.round

/**
 * Provides font metrics for the given font id.
 *
 * It is not recommended to store this object for reuse, as [fontResource] may change on resource reload.
 */
class FontMetrics(
    fontId: Identifier,
    fontSize: Float,
    fontFormat: Int = Font.TRUETYPE_FONT,
) {
    val fontResource: PackResource = PackinResourceLoader[fontId.withPath { "font/$it.ttf" }] ?: throw IllegalArgumentException("Font not loaded: $fontId")

    val fontBytes: ByteArray = fontResource.bytes

    val font: Font = Font.createFont(fontFormat, fontBytes.inputStream()).deriveFont(fontSize)

    /**
     * Returns the bounds of the provided text for this font.
     */
    fun getBounds(string: String): Rectangle2D {
        return font.getStringBounds(string, FONT_RENDER_CONTEXT)
    }

    companion object {
        private val FONT_RENDER_CONTEXT = FontRenderContext(AffineTransform(), false, false)

        /**
         * The rounded width of this rectangle.
         */
        val Rectangle2D.minecraftWidth: Int get() = round(width).toInt()

        /**
         * The rounded height of this rectangle.
         */
        val Rectangle2D.minecraftHeight: Int get() = round(height).toInt()
    }
}
