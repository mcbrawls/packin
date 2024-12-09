package net.mcbrawls.packin.resource

import net.minecraft.util.Identifier

/**
 * A file in form of location and raw bytes.
 * Can be an input or an output of the pack compilation process.
 */
data class PackResource(
    val path: Identifier,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackResource

        return path == other.path
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun toString(): String {
        val size = bytes.size
        return "$path (${size}B)"
    }
}
