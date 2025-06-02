package ae.skydoppler.command;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.ConfigScreenHandler;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public class SkydopplerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(SkydopplerCommand::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess dedicated) {
        dispatcher.register(ClientCommandManager.literal("skydoppler")
                .then(ClientCommandManager.literal("config")
                        .executes(ctx -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            client.execute(() -> client.setScreen(ConfigScreenHandler.buildConfigScreen(SkydopplerClient.CONFIG, null)));
                            return 1;
                        })
                        .then(ClientCommandManager.literal("HeldItemRenderConfig")
                                .executes(ctx -> {
                                    MinecraftClient client = MinecraftClient.getInstance();
                                    client.execute(() -> client.setScreen(HeldItemConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null)));
                                    return 1;
                                })
                        )
                )
                .then(ClientCommandManager.literal("HeldItemRenderConfig")
                        .executes(ctx -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            client.execute(() -> client.setScreen(HeldItemConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null)));
                            return 1;
                        })
                )
        );
    }
}
