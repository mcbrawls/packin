package net.mcbrawls.packin.resource.provider

import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.minecraft.util.Identifier

/**
 * Provides the contents of a Packin pack.
 */
class DirectResourceProvider(
    /**
     * A predicate to test the path of a resource to include.
     */
    val pathPredicate: (Identifier) -> Boolean = { true },

    /**
     * The id of the Packin pack to provide.
     */
    val packId: String? = null,
) : ResourceProvider {
    constructor(paths: Set<Identifier>, packId: String? = null) : this(paths::contains, packId)
    constructor(vararg paths: Identifier, packId: String? = null) : this(paths.toSet(), packId)

    /**
     * Creates a provider which checks within a namespace against a collection of starting path predicates.
     */
    constructor(namespace: String, vararg startingPaths: String, packId: String? = null) : this(
        createStartingPathPredicate(namespace, *startingPaths), packId
    )

    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        val allResources = PackinResourceLoader.getAll(packId, pathPredicate)
        allResources.forEach(collector::collect)
    }

    companion object {
        fun createStartingPathPredicate(namespace: String, vararg startingPaths: String): (Identifier) -> Boolean {
            return { id -> id.namespace == namespace && startingPaths.any(id.path::startsWith) }
        }
    }
}
