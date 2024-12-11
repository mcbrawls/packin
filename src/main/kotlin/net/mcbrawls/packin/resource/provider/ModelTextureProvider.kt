package net.mcbrawls.packin.resource.provider

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

/**
 * A resource provider to provide the textures of models.
 */
class ModelTextureProvider(
    /**
     * The models with textures to be provided.
     */
    vararg val models: Identifier,
) : ResourceProvider {
    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        models.forEach { model ->
            val modelPath = model.withPath { "models/$it.json" }
            val resource = PackinResourceLoader[modelPath] ?: return@forEach

            val modelString = resource.bytes.decodeToString()
            val modelJson = JsonParser.parseString(modelString).asJsonObject

            val texturesJson = modelJson.get("textures")
            if (texturesJson is JsonObject) {
                val textureKeys = texturesJson.keySet()
                textureKeys.forEach { textureKey ->
                    val textureIdStr = texturesJson.getAsJsonPrimitive(textureKey).asString
                    val textureId = Identifier.of(textureIdStr)

                    if (textureId.namespace == Identifier.DEFAULT_NAMESPACE) {
                        return@forEach
                    }

                    collector.tryCollect(textureId.withPath { "textures/$it.png" })
                    collector.tryCollect(textureId.withPath { "textures/$it.png.mcmeta" })
                }
            }
        }
    }
}
