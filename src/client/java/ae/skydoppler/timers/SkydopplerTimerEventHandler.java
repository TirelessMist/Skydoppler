package ae.skydoppler.timers;

import net.minecraft.client.MinecraftClient;

public class SkydopplerTimerEventHandler {

    public static void timerTick(MinecraftClient client) {
        if (PlayerTransferCooldown.isCooldownActive())
            PlayerTransferCooldown.tick(client);
    }


}
