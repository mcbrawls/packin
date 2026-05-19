package net.mcbrawls.packin.resource.provider.template

import net.mcbrawls.packin.listener.PackinResourceLoader
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.pack.ResourceCollector
import net.mcbrawls.packin.resource.provider.ResourceProvider
import net.minecraft.util.Identifier

/**
 * A resource provider to provide a file, modified by template keys.
 */
class TemplateTextProvider(
    /**
     * The source file to read.
     */
    val filePath: Identifier,

    /**
     * The output location; defaults to filePath.
     */
    val outputPath: Identifier,

    /**
     * The filter to apply.
     */
    val filter: TemplateFilter.() -> Unit,
) : ResourceProvider {
    constructor(filePath: Identifier, filter: TemplateFilter.() -> Unit) : this(filePath, filePath, filter)

    override fun collectResources(pack: PackinResourcePack, collector: ResourceCollector) {
        val resource = PackinResourceLoader[filePath] ?: return
        val string = resource.bytes.decodeToString()
        val filter = TemplateFilter(string).apply(filter)
        val output = filter.getOutput()
        collector.collect(outputPath, output.encodeToByteArray())
    }
}
