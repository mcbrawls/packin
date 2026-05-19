package net.mcbrawls.packin.resource.pack

import net.mcbrawls.packin.resource.PackResource
import net.mcbrawls.packin.resource.provider.ResourceProvider

data class PackOverlay(
    val name: String,
    val formatMin: Int,
    val formatMax: Int,
    val providers: MutableSet<ResourceProvider> = mutableSetOf(),
) {
    fun addProvider(provider: ResourceProvider) = providers.add(provider)

    fun collectResources(pack: PackinResourcePack): Set<PackResource> {
        return buildSet {
            providers.forEach { it.collectResources(pack, ::add) }
        }
    }
}
