package ae.skydoppler.command;

// Import the static methods for command building

import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkydopplerCommandHandler {

    // Register the command during client initialization
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("skydoppler")
                        .then(literal("config")
                                .executes(context -> {
                                    MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreen(new HeldItemConfigScreen(null)));
                                    context.getSource().sendFeedback(Text.literal("Opening Skydoppler Held Item Renderer Config..."));
                                    return 1;
                                })
                        ));
    }


}
