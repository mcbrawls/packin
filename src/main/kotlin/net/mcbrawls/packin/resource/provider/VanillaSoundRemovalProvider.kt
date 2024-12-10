package net.mcbrawls.packin.resource.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

/**
 * A resource provider to remove vanilla sounds.
 */
class VanillaSoundRemovalProvider(
    /**
     * The sound ids to remove.
     */
    vararg val sounds: String,
) : ResourceProvider {
    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        // register sounds json for namespace
        val soundsJson = JsonObject().apply {
            sounds.forEach { path ->
                add(
                    path,
                    JsonObject().apply {
                        addProperty("replace", true)
                        add("sounds", JsonArray())
                    }
                )
            }
        }

        collector.collect(Identifier.ofVanilla("sounds.json"), soundsJson.toString().encodeToByteArray())
    }
}
