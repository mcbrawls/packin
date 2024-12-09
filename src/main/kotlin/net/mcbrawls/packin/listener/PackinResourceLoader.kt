package net.mcbrawls.packin.listener

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.mcbrawls.packin.PackinMod
import net.mcbrawls.packin.resource.PackResource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object PackinResourceLoader : SimpleResourceReloadListener<List<SourcePackResource>> {
    val LISTENER_ID: Identifier = Identifier.of(PackinMod.MOD_ID, "resources")
    val LOGGER: Logger = LoggerFactory.getLogger("PackinResourceLoader")

    const val ROOT_PATH: String = "packin_resources"

    private val sourceResources: MutableList<SourcePackResource> = mutableListOf()
    private val resources: MutableList<PackResource> = mutableListOf()
    private val resourcesByPack: MutableMap<String, List<PackResource>> = mutableMapOf()

    override fun load(
        manager: ResourceManager,
        executor: Executor,
    ): CompletableFuture<List<SourcePackResource>> {
        return CompletableFuture.supplyAsync {
            val resources = manager.findAllResources(ROOT_PATH) { true }
            resources.flatMap { (rawId, resources) ->
                resources.map { resource ->
                    val bytes = resource.inputStream.readBytes()
                    val id = rawId.withPath { it.removePrefix("$ROOT_PATH/") }
                    SourcePackResource(resource.packId, PackResource(id, bytes))
                }
            }
        }
    }

    override fun apply(
        loadedResources: List<SourcePackResource>,
        manager: ResourceManager,
        executor: Executor,
    ): CompletableFuture<Void> {
        sourceResources.clear()
        sourceResources.addAll(loadedResources)

        resources.clear()
        resources.addAll(loadedResources.map(SourcePackResource::resource))

        resourcesByPack.clear()
        resourcesByPack.putAll(
            sourceResources
                .groupBy(SourcePackResource::pack)
                .mapValues { (_, resources) -> resources.map(SourcePackResource::resource) }
        )

        val resourceCount = loadedResources.size
        LOGGER.info("Loaded $resourceCount Packin resources")

        return CompletableFuture.completedFuture(null)
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

    override fun getFabricId(): Identifier {
        return LISTENER_ID
    }
}
