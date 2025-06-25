package ae.skydoppler.config.chat_matcher_config;

import ae.skydoppler.chat.ChatMatchCaseSensitivityType;
import ae.skydoppler.chat.ChatMatchType;

import java.util.List;

/**
 * Represents a single match function.
 */
public class ChatMatchConfigEntryData {

    public String name;

    public boolean enabled;

    public boolean playSound;
    public String displayTitle;

    public String displayCustomChatMessage;
    public boolean hideOriginalChatMessage;

    /**
     * A list of commands to execute when the match is found.
     * Enter without '/' at the start, as it will be added automatically.
     * For example, to execute the command "/say Hello", you would enter "say Hello
     */
    public List<String> executeCommands;
    public List<ChatMatchEntryData> matches;

    /**
     * A single match possibility for a chat match function.
     */
    public static class ChatMatchEntryData {
        public String matchString;
        public ChatMatchType matchType;
        public ChatMatchCaseSensitivityType matchCaseSensitivityType;
    }
}
