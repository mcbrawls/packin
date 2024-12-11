package net.mcbrawls.packin.resource.pack

import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.PackResource
import net.minecraft.util.Identifier

/**
 * Collects resources for a pack.
 */
fun interface ResourceCollector {
    /**
     * Collect an existing resource.
     */
    fun collect(resource: PackResource)

    /**
     * Collect raw bytes at a location.
     */
    fun collect(location: Identifier, bytes: ByteArray) {
        collect(PackResource(location, bytes))
    }

    /**
     * Collect an existing resource at a different location.
     */
    fun collect(location: Identifier, resource: PackResource) {
        collect(location, resource.bytes)
    }

    /**
     * Collect an existing resource from its identifier.
     */
    fun tryCollect(location: Identifier) {
        val resource = PackinResourceLoader[location]
        resource?.also(::collect)
    }
}
