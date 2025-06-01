package ae.skydoppler.command;

import ae.skydoppler.config.ConfigScreenHandler;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import ae.skydoppler.SkydopplerClient;

public class SkydopplerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("skydoppler")
            .then(ClientCommandManager.literal("config")
                .executes(ctx -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.execute(() -> client.setScreen(ConfigScreenHandler.buildConfigScreen(SkydopplerClient.CONFIG)));
                    return 1;
                })
                .then(ClientCommandManager.literal("HeldItemRenderConfig")
                    .executes(ctx -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.execute(() -> client.setScreen(new HeldItemConfigScreen(MinecraftClient.getInstance().currentScreen)));
                        return 1;
                    })
                )
            )
            .then(ClientCommandManager.literal("HeldItemRenderConfig")
                .executes(ctx -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.execute(() -> client.setScreen(new HeldItemConfigScreen(MinecraftClient.getInstance().currentScreen)));
                    return 1;
                })
            )
        );
    }
}
