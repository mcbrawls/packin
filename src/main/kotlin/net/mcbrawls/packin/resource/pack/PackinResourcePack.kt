package net.mcbrawls.packin.resource.pack

import net.mcbrawls.packin.resource.PackResource
import net.mcbrawls.packin.resource.provider.ResourceProvider
import net.minecraft.MinecraftVersion
import net.minecraft.resource.ResourceType
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.Path

/**
 * A configured resource pack.
 */
class PackinResourcePack(
    val metadata: PackMetadata,
) {
    val providers: MutableSet<ResourceProvider> = mutableSetOf()

    /**
     * Adds a provider to this pack for compilation.
     * @return whether the provider was added successfully
     */
    fun addProvider(provider: ResourceProvider): Boolean {
        return providers.add(provider)
    }

    fun collectResources(): Set<PackResource> {
        val pack = this
        return buildSet {
            providers.forEach { provider ->
                provider.collectResources(pack, ::add)
            }
        }
    }

    fun createZip(): ByteArray {
        val packFormat = MinecraftVersion.create().getResourceVersion(ResourceType.CLIENT_RESOURCES)

        val resources = collectResources()
        val mcmetaJson = metadata.createJson(packFormat)
        val packIcon = readServerIcon()

        val pack = buildMap {
            putAll(
                resources.associate { resource ->
                    val fullPath = resource.path
                    val namespace = fullPath.namespace
                    val path = fullPath.path

                    "assets/$namespace/$path" to resource.bytes
                }
            )

            this["pack.mcmeta"] = mcmetaJson.toString().encodeToByteArray()
            packIcon?.also { this["pack.png"] = it }
        }

        return createZip(pack)
    }

    companion object {
        inline fun create(metadata: PackMetadata, builder: PackinResourcePack.() -> Unit): PackinResourcePack {
            return PackinResourcePack(metadata).apply(builder)
        }

        /**
         * Creates a zip file from a map of paths to their file byte arrays.
         * @return a zip byte array
         */
        fun createZip(files: Map<String, ByteArray>): ByteArray {
            val outputStream = ByteArrayOutputStream()
            val zipOutputStream = ZipOutputStream(outputStream)

            zipOutputStream.use { stream ->
                for ((fileName, fileContent) in files) {
                    val entry = ZipEntry(fileName)
                    entry.time = 0
                    stream.putNextEntry(entry)
                    stream.write(fileContent)
                    stream.closeEntry()
                }
            }

            return outputStream.toByteArray()
        }

        /**
         * Reads the server icon from its file as a byte array.
         * @return a png byte array
         */
        fun readServerIcon(): ByteArray? {
            val file = Path("server-icon.png").toFile()

            if (!file.exists()) {
                return null
            }

            return file.readBytes()
        }
    }
}
