package net.mcbrawls.packin

import com.google.gson.JsonObject
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.mcbrawls.packin.listener.PackinResourceLoader
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PackinMod : ModInitializer {
    const val MOD_ID = "packin"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
        LOGGER.info("Initializing Packin")

		val dataHelper = ResourceLoader.get(ResourceType.SERVER_DATA)
		dataHelper.registerReloader(Identifier.of(MOD_ID, "reloader"), PackinResourceLoader)
	}

	fun JsonObject.addProperty(property: String, value: Identifier) {
		addProperty(property, value.toString())
	}
}
