package ae.skydoppler.timers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class PlayerTransferCooldown {
    public static final int COOLDOWN_TICKS = 20 * 3; // 3 seconds cooldown

    private static int cooldownTicks = 0;
    private static boolean isCooldownActive = false;

    public static boolean isCooldownActive() {
        return isCooldownActive;
    }

    public static void startCooldown() {
        cooldownTicks = COOLDOWN_TICKS;
        isCooldownActive = true;
    }

    public static void tick(MinecraftClient client) {
        if (isCooldownActive) {
            if (cooldownTicks > 0) {
                cooldownTicks--;
            } else {
                endCooldown(client);
            }
        }
    }

    public static void endCooldown(MinecraftClient client) {
        isCooldownActive = false;
        cooldownTicks = 0;
        if (client.player != null) {
            client.player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.8f, 0.9f);
            client.player.sendMessage(Text.literal(Text.translatable("skydoppler.chat.skydoppler_prefix").getString() + Text.translatable("skydoppler.player_transfer_cooldown").getString()), false);
        }
    }
}
