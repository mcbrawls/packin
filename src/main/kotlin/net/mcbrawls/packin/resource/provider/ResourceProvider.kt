package net.mcbrawls.packin.resource.provider

import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector

interface ResourceProvider {
    /**
     * Collects all resources to be added to the pack.
     */
    fun collectResources(pack: PackinResourcePack, collector: ResourceCollector)
}
