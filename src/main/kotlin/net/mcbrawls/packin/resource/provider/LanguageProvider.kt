package net.mcbrawls.packin.resource.provider

import com.google.gson.JsonObject
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

/**
 * A resource provider to add a language override.
 */
class LanguageProvider(
    /**
     * The language code of this provider.
     * For example, "minecraft:en_us".
     */
    val language: Identifier,

    /**
     * All translations for this provider.
     */
    val translations: Map<String, String>,
) : ResourceProvider {
    constructor(language: Identifier, builder: MutableMap<String, String>.() -> Unit) : this(language, mutableMapOf<String, String>().apply(builder))

    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        val languageJson = JsonObject().apply {
            translations.forEach(::addProperty)
        }

        val languagePath = language.withPath { "lang/$it.json" }
        collector.collect(languagePath, languageJson.toString().encodeToByteArray())
    }
}
