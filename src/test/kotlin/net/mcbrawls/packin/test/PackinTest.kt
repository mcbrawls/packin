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
import net.mcbrawls.packin.font.shift.FontShiftHandler
import net.mcbrawls.packin.font.shift.range.PrecisionShiftRange
import net.mcbrawls.packin.lang.LanguageList
import net.mcbrawls.packin.resource.pack.PackMetadata
import net.mcbrawls.packin.resource.pack.PackinResourcePack
import net.mcbrawls.packin.resource.provider.DirectResourceProvider
import net.mcbrawls.packin.resource.provider.FontProvider
import net.mcbrawls.packin.resource.provider.LanguageProvider
import net.mcbrawls.packin.resource.provider.ModelTextureProvider
import net.mcbrawls.packin.resource.provider.SoundProvider
import net.mcbrawls.packin.resource.provider.VanillaSoundRemovalProvider
import net.mcbrawls.packin.resource.provider.template.TemplateTextProvider
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.io.path.Path
import kotlin.io.path.writeBytes
import kotlin.math.floor
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
                        runCatching {
                            val packBytes = PackinResourcePack.create(PackMetadata("Test", Text.literal("Test"))) {
                                val pinchFontId = Identifier.of("brawls", "pinch")
                                val handler = FontShiftHandler.createVertical(pinchFontId, PrecisionShiftRange(0..2, 0.5f), PrecisionShiftRange(4..6, 0.25f))
                                addProvider(FontProvider(pinchFontId, 7.0f, 4.0f, handler))

                                setOf(0.0f, 0.2f, 0.5f, 0.8f, 1.0f, 3.0f, 3.75f, 2.0f, 10.0f).forEach {
                                    println("$it - ${handler[it]}")
                                }

                                addProvider(FontProvider(Identifier.of("brawls", "love_bug"), 9.0f, 8.0f))
                                addProvider(FontProvider(Identifier.of("brawls", "chocolate"), 11.0f, 8.0f))

                                addProvider(
                                    SoundProvider(
                                        Identifier.of("brawls", "global/action_success"),
                                        Identifier.of("brawls", "global/doesnt_exist"),
                                        Identifier.of("brawls", "unimplemented"),
                                        unimplementedSound = Identifier.of("brawls", "unimplemented")
                                    )
                                )

                                addProvider(
                                    VanillaSoundRemovalProvider(
                                        "block.lava.pop"
                                    )
                                )

                                LanguageList.forEachLanguage { code ->
                                    addProvider(
                                        LanguageProvider(Identifier.ofVanilla(code)) {
                                            LanguageList.forEachContainer { key ->
                                                this[key] = ""
                                            }

                                            this["effect.minecraft.hunger"] = "MC Brawls"
                                        }
                                    )
                                }

                                addProvider(
                                    LanguageProvider(Identifier.of("brawls", "en_us")) {
                                        this["item.brawls.rocket_launcher"] = "Rocket Launcher"
                                    }
                                )

                                addProvider(DirectResourceProvider(Identifier.DEFAULT_NAMESPACE, "textures/", packId = "packin-test:brawls"))
                                addProvider(DirectResourceProvider(Identifier.ofVanilla("models/item/template_skull.json")))
                                addProvider(DirectResourceProvider(packId = "packin-test:twemoji"))
                                // addProvider(DirectResourceProvider(packId = "packin-test:mcc"))

                                addProvider(ModelTextureProvider(Identifier.of("brawls", "item/arcade_machine")))
                                addProvider(DirectResourceProvider("brawls", "models/"))

                                addProvider(TemplateTextProvider(Identifier.ofVanilla("shaders/core/rendertype_text.vsh")) {
                                    replace("r_xp", 0.0f)
                                })
                            }.createZip()
                            val path = Path("out.zip")
                            path.writeBytes(packBytes)
                            context.source.sendFeedback({ Text.literal("Output pack: ~${floor(packBytes.size / 1024.0).toInt()}KB") }, true)
                        }.exceptionOrNull()?.printStackTrace()
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
