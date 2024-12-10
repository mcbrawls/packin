package net.mcbrawls.packin.resource.provider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.mcbrawls.packin.PackinMod.addProperty
import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.PackResource
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

class FontProvider(
    fontId: Identifier,
    val size: Float,
    val oversample: Float,
) : ResourceProvider {
    /**
     * The file path of the font JSON file.
     */
    val fontJsonPath: Identifier = fontId.withPath { "font/$it.json" }

    /**
     * The font file path to be placed inside the font JSON file.
     */
    val fontFilePathInJson: Identifier = fontId.withPath { "$it.ttf" }

    /**
     * The actual file path of the font.
     */
    val fontFilePath: Identifier = fontFilePathInJson.withPath { "font/$it" }

    /**
     * Returns the loaded resource of the font.
     */
    val fontResource: PackResource? get() = PackinResourceLoader[fontFilePath]

    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        fontResource?.also { fontResource ->
            collector.collect(fontResource)
        }

        val json = createJson()
        collector.collect(fontJsonPath, json.toString().encodeToByteArray())
    }

    /**
     * Creates the font JSON file as an object.
     */
    fun createJson(): JsonObject {
        return JsonObject().apply {
            add("providers", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("type", "ttf")
                    addProperty("file", fontFilePathInJson)
                    addProperty("size", size)
                    addProperty("oversample", oversample)
                })
            })
        }
    }
}
