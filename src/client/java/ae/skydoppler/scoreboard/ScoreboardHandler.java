package ae.skydoppler.scoreboard;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.TextRenderer;
import net.minecraft.text.Text;

public class ScoreboardHandler {

    public static void scoreboardTeamUpdate(String line) {

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
