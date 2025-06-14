package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.command.SkydopplerCommand;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.dungeon.DungeonClientHandler;
import ae.skydoppler.dungeon.map.DungeonTileMapConstructor;
import ae.skydoppler.dungeon.map.MapParser;
import ae.skydoppler.dungeon.map.MapTile;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class SkydopplerClient implements ClientModInitializer {

    public static final Path CONFIG_PATH = Paths.get(MinecraftClient.getInstance() // or use a proper run directory reference
            .runDirectory.getAbsolutePath(), "config", "skydoppler.json");
    public static final Boolean debugModeEnabled = true;
    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct = new SkyblockPlayerDataStruct();

    public static DungeonClientHandler dungeonClientHandler = new DungeonClientHandler();
    public static SkyblockLocationEnum currentIsland = SkyblockLocationEnum.NONE;
    public static Enum<?> currentZone = SkyblockLocationEnum.NONE.getZonesForIsland()[0];
    public static Enum<?> currentRegion = null;

    public static boolean isRodCast;

    public static boolean isPlayingSkyblock = false;

    public static SkydopplerConfig CONFIG;

    private static int islandWarpTimerTicks = 0;
    private static boolean islandWarpTimerActive = false;

    public static void startIslandWarpTimer() {
        islandWarpTimerTicks = 60; // 3 seconds at 20 ticks per second
        islandWarpTimerActive = true;
    }

    @Override
    public void onInitializeClient() {

        SkydopplerCommand.registerCommands();

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("Skydoppler (Client) is initializing!");

        CONFIG = SkydopplerConfig.load(CONFIG_PATH);

        ChatMatchHandler.loadJsonData();

        isRodCast = false;

        if (SkydopplerClient.debugModeEnabled)
            debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));
        else
            debugKey = null;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            while (debugKey.wasPressed()) {
                //client.setScreen(HeldItemConfigScreen.buildConfigScreen(CONFIG, null));
                drawDungeonMap();
            }

            if (islandWarpTimerActive && client.player != null) {
                if (islandWarpTimerTicks > 0) {
                    islandWarpTimerTicks--;
                } else {
                    client.player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.MASTER, 1.0f, 1.0f);
                    client.player.sendMessage(Text.literal(" §7(§aPLAYER_TRANSFER_COOLDOWN_EXPIRED§7)"), false);
                    islandWarpTimerActive = false;
                }
            }
        });
    }

    private void drawDungeonMap() {
        MinecraftClient client = MinecraftClient.getInstance();

        ItemStack itemStack = client.player.getInventory().getStack(8); // 9th Hotbar Slot

        if (!(itemStack.getItem() instanceof FilledMapItem)) return; // Ensure the item is a filled map

        //MapParser.saveMapToDesktop(FilledMapItem.getMapState(itemStack, client.world));

        byte[][] mapPixels = MapParser.parseMap(FilledMapItem.getMapState(itemStack, client.world));

        // Print map pixels
        /*System.out.println("Map Pixels:");
        for (int y = 0; y < mapPixels.length; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < mapPixels[y].length; x++) {
                row.append(mapPixels[y][x]).append(" ");
            }
            System.out.println(row.toString());
        }*/

        //byte[][] testMapPixels = DungeonTileMapConstructorTest.readMapPixelsFromFile("src/client/java/ae/skydoppler/dungeon/map/testmaps/floor7.txt");

        MapTile[][] dungeonMap = DungeonTileMapConstructor.constructMap(mapPixels);

        for (int y = 0; y < dungeonMap.length; y++) {
            for (int x = 0; x < dungeonMap[0].length; x++) {
                MapTile tile = dungeonMap[y][x];
                Random r = new Random();
                tile.setUuid(r.nextInt());
            }
        }
/*
        System.out.println("---=== DUNGEON MAP ===---");

        for (int y = 0; y < dungeonMap.length; y++) {
            for (int x = 0; x < dungeonMap[0].length; x++) {
                MapTile mapTile = dungeonMap[y][x];
                System.out.println("---START TILE---");
                System.out.println("Position: (" + x + ", " + y + ")");
                System.out.println("Room Type: " + mapTile.getRoomType());
                System.out.println("CheckMark Type: " + mapTile.getRoomMarkType());
                System.out.println("UUID: " + mapTile.getUuid());
                System.out.println("Top Door Type: " + mapTile.getTopDoorType());
                System.out.println("Right Door Type: " + mapTile.getRightDoorType());
                System.out.println("Bottom Door Type: " + mapTile.getBottomDoorType());
                System.out.println("Left Door Type: " + mapTile.getLeftDoorType());
                System.out.println("---END TILE---");
            }
        }

        System.out.println("---=== END DUNGEON MAP ===---");

        System.out.println("Modular tile grid size: " + dungeonMap.length + " x " + dungeonMap[0].length);
        //DungeonTileMapConstructorTest.printTileGridSummary(dungeonMap);*/
        HudRenderingEntrypoint.dungeonMapTiles = dungeonMap;

    }
}
