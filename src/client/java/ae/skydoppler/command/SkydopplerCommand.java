package ae.skydoppler.command;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.ConfigScreenHandler;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SkydopplerCommand {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("skydoppler")
                            .then(ClientCommandManager.literal("config")
                                    .executes(context -> {
                                        SkydopplerClient.openMainConfigScreen();
                                        context.getSource().sendFeedback(Text.literal("Opened Skydoppler Config Screen."));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .then(ClientCommandManager.literal("HeldItemRendererConfig")
                                    .executes(context -> {
                                        SkydopplerClient.openHeldItemConfigScreen();
                                        context.getSource().sendFeedback(Text.literal("Opened Held Item Renderer Config Screen."));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
            );
        });
    }
}
