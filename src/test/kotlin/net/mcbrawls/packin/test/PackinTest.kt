package net.mcbrawls.packin.test

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.mcbrawls.packin.font.FontMetrics
import net.mcbrawls.packin.font.FontMetrics.Companion.minecraftHeight
import net.mcbrawls.packin.font.FontMetrics.Companion.minecraftWidth
import net.mcbrawls.packin.resource.pack.PackMetadata
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.provider.FontProvider
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.io.path.Path
import kotlin.io.path.writeBytes
import kotlin.math.round

object PackinTest : ModInitializer {
    const val MOD_ID = "packin-test"

    override fun onInitialize() {
        val loader = FabricLoader.getInstance()
        val container = loader.getModContainer(MOD_ID).orElseThrow()

        ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "brawls"), container, ResourcePackActivationType.DEFAULT_ENABLED)
        ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "mcc"), container, ResourcePackActivationType.DEFAULT_ENABLED)
        ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, "twemoji"), container, ResourcePackActivationType.DEFAULT_ENABLED)

        CommandRegistrationCallback.EVENT.register { dispatcher, access, env ->
            dispatcher.register(
                CommandManager.literal("out")
                    .executes { context ->
                        val packBytes = PackinResourcePack.create(PackMetadata("Test", Text.literal("Test"))) {
                            addProvider(FontProvider(Identifier.of("brawls", "pinch"), 7.0f, 4.0f))
                            addProvider(FontProvider(Identifier.of("brawls", "love_bug"), 9.0f, 8.0f))
                            addProvider(FontProvider(Identifier.of("brawls", "chocolate"), 11.0f, 8.0f))
                        }.createZip()
                        val path = Path("out.zip")
                        path.writeBytes(packBytes)
                        1
                    }
            )

            dispatcher.register(
                CommandManager.literal("textwidth")
                    .then(
                        CommandManager.argument("font", IdentifierArgumentType.identifier())
                            .then(
                                CommandManager.argument("size", FloatArgumentType.floatArg(0.0f))
                                    .then(
                                        CommandManager.argument("string", StringArgumentType.greedyString())
                                            .executes { context ->
                                                val fontId = IdentifierArgumentType.getIdentifier(context, "font")
                                                val size = FloatArgumentType.getFloat(context, "size")
                                                val string = StringArgumentType.getString(context, "string")

                                                val metrics = FontMetrics(fontId, size)
                                                val bounds = metrics.getBounds(string)
                                                val width = bounds.minecraftWidth
                                                val height = bounds.minecraftHeight

                                                context.source.sendFeedback({
                                                    Text.empty()
                                                        .append(Text.literal("$fontId@$size: $width, $height"))
                                                        .append("\n")
                                                        .append(Text.literal(string).styled { it.withFont(fontId) })
                                                        .append("\n")
                                                        .append(Text.literal(".".repeat(round(width / 2.0).toInt())))
                                                }, false)
                                                1
                                            }
                                    )
                            )
                    )
            )
        }
    }
}
