package ae.skydoppler.util;

import net.minecraft.client.MinecraftClient;

public class BlockingHelper {

    public static boolean isBlocking = false;

    public static void setBlocking(boolean blocking) {
        isBlocking = blocking;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            if (blocking)
                client.player.setSprinting(false); // Ensure player stops sprinting when blocking
            client.player.setSneaking(blocking); // Set sneaking state to match blocking
        }
    }

}
