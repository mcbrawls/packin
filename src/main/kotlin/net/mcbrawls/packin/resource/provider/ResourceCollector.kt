package net.mcbrawls.packin.resource.provider

import net.mcbrawls.packin.resource.PackResource
import net.minecraft.util.Identifier

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
}
