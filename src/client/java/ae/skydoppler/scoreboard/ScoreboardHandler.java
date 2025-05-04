package ae.skydoppler.scoreboard;

import ae.skydoppler.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreboardHandler {

    public static void scoreboardTeamUpdate(String line) {


        line = line.trim();
        line = line.toLowerCase();

        System.out.println("===SCOREBOARD UPDATE===");
        System.out.println("Line: " + line);

        Pattern pattern = Pattern.compile("\u2020");

        Matcher matcher = pattern.matcher(line);

        System.out.println("Running if statement to check if the line contains \"\u008F\".");
        if (matcher.find() || line.contains("\u2020")) {
            /*line = line.substring(line.indexOf("£") + 1);
            line = line.trim();*/
            System.out.println("Displaying title: " + line);
            TextRenderer.DisplayTitle(Text.literal("Location: " + line), Text.empty(), 0, 90, 0);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.playSoundToPlayer(SoundEvents.BLOCK_BELL_USE, SoundCategory.MASTER, 1.0f, 1.0f);
        }






















        /*line = line.trim();
        line = line.toLowerCase();

        System.out.println("===SCOREBOARD UPDATE===");
        System.out.println("Line Update: " + line);
        if (line.contains(":")) {
            System.out.println("The line contains \":\"");

            *//*if (line.contains("bits")) {
                System.out.println("The line contains \"bits\"");
                line = line.replaceAll(".*?(\\d{1,3}(?:,\\d{3})*\\.\\d+).*", "$1");
                System.out.println("Removed everything but the number.");
                line = line.replaceAll(",", "");
                System.out.println("Removed commas.");
                line = line.trim();
                System.out.println("Trimmed whitespace.");
                System.out.println("New line: " + line);

                double numberDouble = Double.parseDouble(line);
                System.out.println("Converted to a Double.");
                long numberLong = (long) Math.ceil(numberDouble);
                System.out.println("Converted to a Long.");

                System.out.println("Setting the purse var to " + numberLong + ".");
                SkydopplerClient.playerDataStruct.setBits(numberLong);
                System.out.println("Successfully set the purse var to " + numberLong + ".");

                TextRenderer.DisplayTitle(Text.literal("Bits: " + line), Text.empty(), 0, 50, 0);

            } else *//*if ((line.contains("purse") || line.contains("piggy"))) {
                System.out.println("The line contains \"purse\" or \"piggy\"");
                line = line.replaceAll(".*?(\\d{1,3}(?:,\\d{3})*\\.\\d+).*", "$1");
                System.out.println("Removed everything but the number.");
                line = line.replaceAll(",", "");
                System.out.println("Removed commas.");
                line = line.trim();
                System.out.println("Trimmed whitespace.");
                System.out.println("New line: " + line);

                double numberDouble = Double.parseDouble(line);
                System.out.println("Converted to a Double.");
                long numberLong = (long) Math.ceil(numberDouble);
                System.out.println("Converted to a Long.");

                System.out.println("Setting the purse var to " + numberLong + ".");
                SkydopplerClient.playerDataStruct.setPurse(numberLong);
                System.out.println("Successfully set the purse var to " + numberLong + ".");

                TextRenderer.DisplayTitle(Text.literal("Purse: " + line), Text.empty(), 0, 50, 0);

            }

        }*/

    }

}
