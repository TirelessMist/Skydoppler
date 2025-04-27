package ae.skydoppler.scoreboard;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.*;

import java.util.Collection;

public class ScoreboardHandler {

    public static void scoreboardUpdate(String line) {

        line = line.toLowerCase();

        System.out.println("===SCOREBOARD UPDATE===");
        System.out.println("Line Update: " + line);

        if (line.contains("bits")) {
            line = line.substring(0, line.indexOf(":"));
            line = line.trim();
            line = line.replaceAll(",", "");

            SkydopplerClient.playerDataStruct.setBits(long.class.cast(line));
        }
        else if (line.contains("purse") || line.contains("piggy")) {
            line = line.substring(0, line.indexOf(":"));
            line = line.trim();
            line = line.replaceAll(",", "");

            SkydopplerClient.playerDataStruct.setPurse(long.class.cast(line));
        }

    }

}
