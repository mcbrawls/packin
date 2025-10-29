package net.mcbrawls.packin.resource.pack

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
    fun createJson(version: PackVersion): JsonObject {
        val descriptionJsonResult = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, description)
        val descriptionJson = descriptionJsonResult.result().orElseThrow { IllegalArgumentException("Could not encode description: $description") }

        return JsonObject().apply {
            add("pack", JsonObject().apply {
                add("description", descriptionJson)
            })
        }
    }
}
