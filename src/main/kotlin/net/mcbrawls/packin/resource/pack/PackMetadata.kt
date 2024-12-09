package net.mcbrawls.packin.resource.pack

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs

/**
 * The displayed resource pack information.
 */
data class PackMetadata(
    val title: String,
    val description: Text,
) {
    fun createJson(packFormat: Int): JsonObject {
        val descriptionJsonResult = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, description)
        val descriptionJson = descriptionJsonResult.result().orElseThrow { IllegalArgumentException("Could not encode description: $description") }

        return JsonObject().apply {
            add("pack", JsonObject().apply {
                addProperty("pack_format", packFormat)
                add("description", descriptionJson)
            })
        }
    }
}
