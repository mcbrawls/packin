package net.mcbrawls.packin.resource.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

class SoundProvider(
    /**
     * The sound ids to register.
     */
    vararg val sounds: Identifier,

    /**
     * A sound to replace sounds that are not found in the source files.
     */
    val unimplementedSound: Identifier? = null,
) : ResourceProvider {
    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        var hasUnimplemented = false

        sounds.groupBy(Identifier::getNamespace)
            .forEach { (namespace, ids) ->
                // register all sound ids
                val invalidSoundPaths = ids.filterNot { id ->
                    val soundPath = createSoundPath(id)
                    val soundResource = PackinResourceLoader[soundPath]
                    if (soundResource != null) {
                        collector.collect(soundResource)
                        true
                    } else {
                        hasUnimplemented = true
                        false
                    }
                }

                // register sounds json for namespace
                val soundsJson = JsonObject().apply {
                    ids.forEach { id ->
                        val usedId = if (invalidSoundPaths.contains(id)) {
                            unimplementedSound
                        } else {
                            id
                        }

                        if (usedId != null) {
                            add(
                                id.path,
                                JsonObject().apply {
                                    add(
                                        "sounds",
                                        JsonArray().apply {
                                            add(usedId.toString())
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                collector.collect(Identifier.of(namespace, "sounds.json"), soundsJson.toString().encodeToByteArray())
            }

        // collect unimplemented sound if applicable
        if (hasUnimplemented) {
            unimplementedSound?.let { id ->
                val path = createSoundPath(id)
                val resource = PackinResourceLoader[path]
                resource?.also(collector::collect)
            }
        }
    }

    /**
     * Creates the file path of the given sound id.
     */
    fun createSoundPath(id: Identifier): Identifier {
        return id.withPath { "sounds/$it.ogg" }
    }
}
