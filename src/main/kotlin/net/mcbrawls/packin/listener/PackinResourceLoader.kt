package net.mcbrawls.packin.listener

import net.fabricmc.fabric.api.resource.v1.reloader.SimpleResourceReloader
import net.mcbrawls.packin.PackinMod
import net.mcbrawls.packin.resource.PackResource
import net.minecraft.resource.ResourceReloader
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Loads Packin source files from Minecraft resources.
 */
object PackinResourceLoader : SimpleResourceReloader<List<SourcePackResource>>() {
    val LOGGER: Logger = LoggerFactory.getLogger("PackinResourceLoader")

    /**
     * The unique identfiier of this reload listener.
     */
    val LISTENER_ID: Identifier = Identifier.of(PackinMod.MOD_ID, "resources")

    const val ROOT_PATH: String = "packin_resources"

    private val sourceResources: MutableList<SourcePackResource> = mutableListOf()
    private val resources: MutableList<PackResource> = mutableListOf()
    private val resourcesByPack: MutableMap<String, List<PackResource>> = mutableMapOf()

    override fun prepare(store: ResourceReloader.Store): List<SourcePackResource> {
        val manager = store.resourceManager
        val resources = manager.findAllResources(ROOT_PATH) { true }
        return resources.flatMap { (rawId, resources) ->
            resources.map { resource ->
                val bytes = resource.inputStream.readBytes()
                val id = rawId.withPath { it.removePrefix("$ROOT_PATH/") }
                SourcePackResource(resource.packId, PackResource(id, bytes))
            }
        }
    }

    override fun apply(prepared: List<SourcePackResource>, store: ResourceReloader.Store) {
        sourceResources.clear()
        sourceResources.addAll(prepared)

        resources.clear()
        resources.addAll(prepared.map(SourcePackResource::resource))

        resourcesByPack.clear()
        resourcesByPack.putAll(
            sourceResources
                .groupBy(SourcePackResource::pack)
                .mapValues { (_, resources) -> resources.map(SourcePackResource::resource) }
        )

        val resourceCount = prepared.size
        LOGGER.info("Loaded $resourceCount Packin resources")
    }

    /**
     * Gets the highest loaded resource for the given id.
     */
    operator fun get(id: Identifier): PackResource? {
        return resources.lastOrNull { resource -> resource.path == id }
    }

    /**
     * Gets the loaded resource for the given id from the given pack.
     */
    operator fun get(pack: String, id: Identifier): PackResource? {
        val resources = resourcesByPack[pack] ?: return null
        return resources.firstOrNull { resource -> resource.path == id }
    }

    /**
     * Gets all loaded resources for the given pack id and filter.
     */
    fun getAll(pack: String?, filter: (Identifier) -> Boolean = { true }): Set<PackResource> {
        val resources = if (pack == null) {
            resources.toSet()
        } else {
            resourcesByPack[pack] ?: return emptySet()
        }

        return resources.filter { filter(it.path) }.toSet()
    }
}
