package net.mcbrawls.packin.resource.provider.template

import org.joml.Vector3f

/**
 * Modifies a string based on replacement filters.
 */
data class TemplateFilter(private var string: String) {
    fun getOutput(): String {
        return string
    }

    /**
     * Replaces a key, used in the format `{key}`, with the given value.
     */
    fun replace(key: String, value: String) {
        string = string.replace("\"{$key}\"", value)
    }

    /**
     * Replaces a key with any stringified object.
     */
    fun replace(key: String, value: Any?) {
        replace(key, value.toString())
    }

    /**
     * Replaces any `r|g|b_key` with the given 3-float vector.
     */
    fun replace(key: String, value: Vector3f) {
        replace("r_$key", value.x)
        replace("g_$key", value.y)
        replace("b_$key", value.z)
    }
}
