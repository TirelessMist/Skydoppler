package ae.skydoppler.player_hiding;

import net.minecraft.entity.player.PlayerEntity;

public class HidePlayerNearNpc {
    public static boolean hidePlayers = true; // the setting - if it is enabled or not

    public static double hideRange = 2; // the range in which players are hidden

    public static boolean isPlayerAnNpc(PlayerEntity player) {

        return player.getUuid().version() != 4;

    }
}
