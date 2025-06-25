package ae.skydoppler.chat;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.chat_matcher_config.ChatMatchConfigEntryData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ChatMatchHandler {

    /**
     * Handles chat message matching against configured patterns.
     *
     * @return true if the original message should be hidden, false otherwise.
     */
    public static boolean matchChatMessage(String message) {
        if (SkydopplerClient.CONFIG == null || SkydopplerClient.CONFIG.userChatMatchConfig == null ||
                SkydopplerClient.CONFIG.userChatMatchConfig.functions == null ||
                SkydopplerClient.CONFIG.userChatMatchConfig.functions.length == 0) {
            return false;
        }

        boolean shouldHideOriginalMessage = false;
        MinecraftClient client = MinecraftClient.getInstance();

        // Check each configured chat match function
        for (ChatMatchConfigEntryData function : SkydopplerClient.CONFIG.userChatMatchConfig.functions) {
            if (!function.enabled || function.matches == null || function.matches.isEmpty()) {
                continue;
            }

            // Check if the message matches any of the patterns for this function
            boolean functionMatched = false;
            for (ChatMatchConfigEntryData.ChatMatchEntryData matchEntry : function.matches) {
                if (matchesPattern(message, matchEntry)) {
                    functionMatched = true;
                    break;
                }
            }

            if (functionMatched) {
                // Handle the matched function
                // Play sound if configured
                if (function.playSound && client != null && client.player != null) {
                    client.player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);
                }

                // Display title if configured
                if (function.displayTitle != null && !function.displayTitle.isEmpty() && client != null) {
                    client.inGameHud.setTitle(Text.literal(function.displayTitle));
                }

                // Display chat message if configured
                if (function.displayCustomChatMessage != null && !function.displayCustomChatMessage.isEmpty() && client != null && client.player != null) {
                    client.player.sendMessage(Text.literal(function.displayCustomChatMessage), false);
                }

                // Execute commands if configured
                if (function.executeCommands != null && !function.executeCommands.isEmpty() && client != null && client.player != null) {
                    for (String command : function.executeCommands) {
                        client.player.networkHandler.sendChatCommand(command);
                    }
                }

                // If any function wants to hide the original message, mark it as such
                // The actual hiding will depend on the caller's behavior
                shouldHideOriginalMessage = true;
            }
        }

        return shouldHideOriginalMessage;
    }

    /**
     * Checks if a message matches the given pattern according to the match type and case sensitivity.
     *
     * @param message    The message to check.
     * @param matchEntry The pattern and matching configuration.
     * @return true if the message matches the pattern, false otherwise.
     */
    private static boolean matchesPattern(String message, ChatMatchConfigEntryData.ChatMatchEntryData matchEntry) {
        if (matchEntry.matchString == null || matchEntry.matchString.isEmpty()) {
            return false;
        }

        String messageToCheck = message;
        String patternToMatch = matchEntry.matchString;

        // Handle case sensitivity
        if (matchEntry.matchCaseSensitivityType == ChatMatchCaseSensitivityType.NOT_CASE_SENSITIVE) {
            messageToCheck = messageToCheck.toLowerCase();
            patternToMatch = patternToMatch.toLowerCase();
        }

        // Check based on the match type
        return switch (matchEntry.matchType) {
            case MATCH_EXACTLY -> messageToCheck.equals(patternToMatch);
            case STARTS_WITH -> messageToCheck.startsWith(patternToMatch);
            case CONTAINS -> messageToCheck.contains(patternToMatch);
            default -> false;
        };
    }
}
