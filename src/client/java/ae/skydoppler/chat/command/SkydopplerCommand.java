package ae.skydoppler.chat.command;

import ae.skydoppler.config.SkydopplerConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SkydopplerCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("skydoppler")
                            .then(ClientCommandManager.literal("config")
                                    .executes(context -> {
                                        MinecraftClient client = MinecraftClient.getInstance();
                                        // Use client.execute to ensure thread safety when setting the screen.
                                        client.execute(() -> {
                                            // First, close any currently open screen (chat, etc.)
                                            client.setScreen(null);
                                            // Then open your config screen with no parent (null)
                                            client.setScreen(new SkydopplerConfigScreen(client.currentScreen));
                                        });
                                        // Provide immediate feedback
                                        context.getSource().sendFeedback(Text.literal("Opening Skydoppler config..."));
                                        return 1;
                                    })
                            )
            );
        });
    }
}
