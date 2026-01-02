package ae.skydoppler;

import ae.skydoppler.command.SkydopplerCommand;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.dungeon.DungeonClientHandler;
import ae.skydoppler.dungeon.map.DungeonTileMapConstructor;
import ae.skydoppler.dungeon.map.MapParser;
import ae.skydoppler.dungeon.map.MapTile;
import ae.skydoppler.skyblock.skyblock_locations.SkyblockLocationEnum;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import ae.skydoppler.timers.SkydopplerTimerEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class SkydopplerClient implements ClientModInitializer {

    public static final Path CONFIG_PATH = Paths.get(MinecraftClient.getInstance() // or use a proper run directory reference
            .runDirectory.getAbsolutePath(), "config", "skydoppler.json");
    public static final Boolean debugModeEnabled = false;
    public static SkydopplerConfig CONFIG;
    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct = new SkyblockPlayerDataStruct();

    public static DungeonClientHandler dungeonClientHandler = new DungeonClientHandler();

    public static SkyblockLocationEnum currentIsland = SkyblockLocationEnum.NONE;
    public static Enum<?> currentZone = SkyblockLocationEnum.NONE.getZonesForIsland()[0];
    public static Enum<?> currentRegion = null;

    public static boolean isPlayingSkyblock = false;

    public static int lockSlotKey; // Key to lock the hotbar slot. This is not a traditional keybinding, because it is used in inventories and not in the main game loop.

    @Override
    public void onInitializeClient() {

        // TODO: Revamp chat notifications to use translation keys and be more organized with a more structured system.

        SkydopplerCommand.registerCommands();

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("Skydoppler (Client) is initializing!");

        CONFIG = SkydopplerConfig.load(CONFIG_PATH);

        if (SkydopplerClient.debugModeEnabled)
            debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));
        else
            debugKey = null;

        lockSlotKey = GLFW.GLFW_KEY_L;

        ClientTickEvents.END_CLIENT_TICK.register(SkydopplerTimerEventHandler::timerTick);
    }
}
