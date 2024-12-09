package net.mcbrawls.packin.resource.provider

import net.mcbrawls.packin.resource.pack.PackinResourcePack

interface ResourceProvider {
    fun collectResources(pack: PackinResourcePack, collector: ResourceCollector)
}
