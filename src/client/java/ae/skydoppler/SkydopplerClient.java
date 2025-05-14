package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.chat.command.SkydopplerCommand;
import ae.skydoppler.dungeon.room_detection.MapParser;
import ae.skydoppler.fishing.FishingHideState;
import ae.skydoppler.skyblock_locations.SkyblockIslandEnum;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

import static ae.skydoppler.dungeon.solver.waterboard_solver.solveMaze;

public class SkydopplerClient implements ClientModInitializer {

    public static MinecraftClient client = MinecraftClient.getInstance();

    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct;
    public static SkyblockIslandEnum currentIsland = SkyblockIslandEnum.NONE;
    public static Enum<?> currentZone = SkyblockIslandEnum.NONE.getZonesForIsland()[0]; // Sets currentZone to the first enum for the island of type "NONE", which is also "NONE" (the only value for the island of type "NONE").
    public static Enum<?> currentRegion = null;
    public static boolean isRodCast;
    public static boolean isPlayingSkyblock = false;
    public static boolean hideExplosionParticle = true;
    private TextRenderer textRenderer;

    @Override
    public void onInitializeClient() {

        System.out.println("Skydoppler (Client) is initializing!");
        textRenderer = new TextRenderer(client);
        textRenderer.initialize();
        ChatMatchHandler.loadJsonData();
        isRodCast = false;

        Map<BlockPos, Block> waterPuzzleStructure = Map.of(
                new BlockPos(0, 0, 0), Blocks.GRAY_STAINED_GLASS,
                new BlockPos(1, 0, 0), Blocks.GRAY_STAINED_GLASS,
                new BlockPos(2, 0, 0), Blocks.GRAY_STAINED_GLASS,
                new BlockPos(3, 0, 0), Blocks.GRAY_STAINED_GLASS,
                new BlockPos(4, 0, 0), Blocks.GRAY_STAINED_GLASS,
                new BlockPos(5, 0, 0), Blocks.GRAY_STAINED_GLASS
        );

        SkydopplerCommand.register();

        playerDataStruct = new SkyblockPlayerDataStruct();

        debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            // Define the maximum distance for the raycast (e.g., 20 blocks)
            double maxDistance = 20.0;

            // Get the player's eye position and look vector
            Vec3d eyePos = client.player.getCameraPosVec(1.0F);
            Vec3d lookVec = client.player.getRotationVec(1.0F);

            // Calculate the end position based on the look vector and the desired distance
            Vec3d endPos = eyePos.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);

            // Set up the raycast context
            RaycastContext context = new RaycastContext(
                    eyePos,
                    endPos,
                    RaycastContext.ShapeType.OUTLINE,          // Determines how to traverse block shapes
                    RaycastContext.FluidHandling.NONE,         // Decide whether fluids should be considered
                    client.player               // The player performing the raycast
            );

            // Perform the raycast using the custom context
            HitResult hitResult = client.world.raycast(context);

            // Check if the hit result is a block hit
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                if (client.world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.GRAY_STAINED_GLASS) {
                    //System.out.println("Extended raycast hit Gray Stained Glass at: " + blockHitResult.getBlockPos());

                    /*if (isStructureAtPosition(blockHitResult.getBlockPos()))
                        System.out.println("Extended raycast hit Water Puzzle at: " + blockHitResult.getBlockPos());*/

                    // You can now query the block further or perform additional logic here.
                }
            }


            while (debugKey.wasPressed()) {
                // add debug key stuff here if you need


                MapParser.parseMapFromSlot();

            }

            // Check every tick if the local player's fishing bobber is present within hideRange
            if (client.world == null || client.player == null) {
                FishingHideState.rodCastActive = false;
                return;
            }
            // Create a bounding box centered around the player
            double r = FishingHideState.hideRange;
            Box box = new Box(
                    client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
                    client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
            );

            // Check if any FishingBobberEntity in the world (within the box) belongs to the local player
            boolean found = client.world.getEntitiesByClass(FishingBobberEntity.class, box,
                    bobber -> bobber.getOwner() != null && bobber.getOwner().equals(client.player)
            ).size() > 0;

            FishingHideState.rodCastActive = found;
        });

    }

    // Function to check if a block structure exists at a given position
    public boolean isStructureAtPosition(BlockPos startPos, Map<BlockPos, Block> structure) {
        if (client.world == null) return false;

        for (Map.Entry<BlockPos, Block> entry : structure.entrySet()) {
            BlockPos relativePos = startPos.add(entry.getKey());
            Block expectedBlock = entry.getValue();
            Block actualBlock = client.world.getBlockState(relativePos).getBlock();

            if (!actualBlock.equals(expectedBlock)) {
                return false; // Structure does not match
            }
        }

        return true; // Structure matches
    }


}