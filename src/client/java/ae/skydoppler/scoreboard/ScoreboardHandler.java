package ae.skydoppler.scoreboard;

import ae.skydoppler.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import java.util.regex.*;

public class ScoreboardHandler {

    public static void scoreboardTeamUpdate(String line) {


        line = line.trim();
        line = line.toLowerCase();

        /*System.out.println("===SCOREBOARD UPDATE===");
        String converted = convertIconsToUnicode(line);
        System.out.println("Converted scoreboard: " + converted);*/

        if (line.contains("⏣")) { // if it contains the location icon
            line = line.substring(line.indexOf("⏣") + 1);
            line = line.trim();
            line = line.substring(0, 1).toUpperCase() + line.substring(1);
            TextRenderer.DisplayTitle(Text.literal("Location: " + line), Text.empty(), 0, 30, 0);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.playSoundToPlayer(SoundEvents.BLOCK_BELL_USE, SoundCategory.MASTER, 0.5f, 1.4f);

            return;
        }

        // Regex pattern to match "purse" or "piggy" followed by a number
        Pattern pattern = Pattern.compile("(purse|piggy): ([\\d,]+\\.?\\d*)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String label = matcher.group(1); // "purse" or "piggy"
            String numberStr = matcher.group(2); // Extract the number part

            // Remove commas and decimal points
            String cleanedNumber = numberStr.replace(",", "").split("\\.")[0];

            // Convert to an integer and round up
            int roundedNumber = (int) Math.ceil(Double.parseDouble(cleanedNumber));

            // Print the formatted result
            TextRenderer.DisplayTitle(Text.literal(label + ": " + roundedNumber), Text.empty(), 0, 50, 0);

            return;
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

    public static String convertIconsToUnicode(String line) {
        StringBuilder sb = new StringBuilder();

        // Process the string code-point by code-point to properly handle surrogate pairs.
        for (int i = 0; i < line.length(); ) {
            int codePoint = line.codePointAt(i);
            int charCount = Character.charCount(codePoint);

            // Here we define “icon/emoji” (or special symbol) as any non-ASCII character
            // that is not a letter, digit, or whitespace.
            // (You can adjust this condition if you want to preserve accented letters, etc.)
            if (codePoint > 127 && !(Character.isLetterOrDigit(codePoint) || Character.isWhitespace(codePoint))) {
                sb.append(String.format("\\u%04X", codePoint));
            } else {
                sb.appendCodePoint(codePoint);
            }
            i += charCount;
        }
        return sb.toString();
    }

}
