package net.mcbrawls.packin.resource.pack

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.resource.PackVersion
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs

/**
 * The displayed resource pack information.
 */
data class PackMetadata(
    val title: String,
    val description: Text,
) {
    fun createJson(version: PackVersion, overlays: List<PackOverlay> = emptyList()): JsonObject {
        val descriptionJsonResult = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, description)
        val descriptionJson = descriptionJsonResult.result().orElseThrow { IllegalArgumentException("Could not encode description: $description") }

        val maxFormat = maxOf(version.major, overlays.maxOfOrNull { it.formatMax } ?: 0)

        return JsonObject().apply {
            add("pack", JsonObject().apply {
                addProperty("pack_format", version.major)
                add("supported_formats", JsonArray().apply {
                    add(0)
                    add(maxFormat)
                })
                add("description", descriptionJson)
            })
            if (overlays.isNotEmpty()) {
                add("overlays", JsonObject().apply {
                    add("entries", JsonArray().apply {
                        overlays.forEach { overlay ->
                            add(JsonObject().apply {
                                add("formats", JsonArray().apply {
                                    add(overlay.formatMin)
                                    add(overlay.formatMax)
                                })
                                addProperty("directory", overlay.name)
                            })
                        }
                    })
                })
            }
        }
    }
}
