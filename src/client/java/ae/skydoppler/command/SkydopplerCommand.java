package ae.skydoppler.command;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class SkydopplerCommand {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("skydoppler")
                            .then(ClientCommandManager.literal("config")
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Text.literal("Opened Skydoppler Config Screen."));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .then(ClientCommandManager.literal("HeldItemRendererConfig")
                                    .executes(context -> {
                                        context.getSource().sendFeedback(Text.literal("Opened Held Item Renderer Config Screen."));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
            );
        });
    }
}
