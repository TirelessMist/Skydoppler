package ae.skydoppler.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class StyleConverter {

    public static Text ConvertToFormattedText(String formattedString) {

        // Convert a String of text with the formatting character '§' to the Minecraft Text class with siblings that are groups of text with their respective styling properties
        String[] splitString = formattedString.split("§"); // Split the String into an array of Strings with the formatting character '§' as the delimiter
        Text text = Text.empty(); // Create an empty Text object
        Text currentText = Text.empty(); // Create an empty Text object for the current sibling
        Style currentStyle = Style.EMPTY; // Track the current style

        for (String s : splitString) { // For each String in the array
            if (!s.isEmpty()) { // If the String is not empty
                String formattingCode = s.substring(0, 1); // Get the formatting code
                String textString = s.substring(1); // Get the text String

                // Apply the formatting code to the current style
                switch (formattingCode) {
                    case "0" -> currentStyle = currentStyle.withColor(0x000000); // Black
                    case "1" -> currentStyle = currentStyle.withColor(0x0000AA); // Dark blue
                    case "2" -> currentStyle = currentStyle.withColor(0x00AA00); // Dark green
                    case "3" -> currentStyle = currentStyle.withColor(0x00AAAA); // Dark aqua
                    case "4" -> currentStyle = currentStyle.withColor(0xAA0000); // Dark red
                    case "5" -> currentStyle = currentStyle.withColor(0xAA00AA); // Dark purple
                    case "6" -> currentStyle = currentStyle.withColor(0xFFAA00); // Gold
                    case "7" -> currentStyle = currentStyle.withColor(0xAAAAAA); // Gray
                    case "8" -> currentStyle = currentStyle.withColor(0x555555); // Dark gray
                    case "9" -> currentStyle = currentStyle.withColor(0x5555FF); // Blue
                    case "a" -> currentStyle = currentStyle.withColor(0x55FF55); // Green
                    case "b" -> currentStyle = currentStyle.withColor(0x55FFFF); // Aqua
                    case "c" -> currentStyle = currentStyle.withColor(0xFF5555); // Red
                    case "d" -> currentStyle = currentStyle.withColor(0xFF55FF); // Light purple
                    case "e" -> currentStyle = currentStyle.withColor(0xFFFF55); // Yellow
                    case "l" -> currentStyle = currentStyle.withBold(true); // Bold
                    case "o" -> currentStyle = currentStyle.withItalic(true); // Italic
                    case "n" -> currentStyle = currentStyle.withUnderline(true); // Underline
                    case "m" -> currentStyle = currentStyle.withStrikethrough(true); // Strikethrough
                    case "r" -> currentStyle = Style.EMPTY; // Reset
                }

                // Create a Text object for the text String with the current style
                currentText = Text.literal(textString).setStyle(currentStyle);
                text = text.copy().append(currentText); // Append the current text to the main text
            }
        }
        return text;
    }

    public static String ConvertToFormattedString(Text formattedText) {
        StringBuilder builder = new StringBuilder();
        appendText(formattedText, builder); // Process the main text
        // Process siblings
        for (Text sibling : formattedText.getSiblings()) {
            appendText(sibling, builder);
        }
        return builder.toString();
    }

    private static void appendText(Text text, StringBuilder builder) {
        Style style = text.getStyle();
        if (style.getColor() != null) {
            builder.append("§").append(getFormattingCodeFromColor(style.getColor().getRgb()));
        }
        if (style.isBold()) {
            builder.append("§l");
        }
        if (style.isItalic()) {
            builder.append("§o");
        }
        if (style.isUnderlined()) {
            builder.append("§n");
        }
        if (style.isStrikethrough()) {
            builder.append("§m");
        }
        builder.append(text.getString());
    }

    private static String getFormattingCodeFromColor(int color) {
        return switch (color) {
            case 0x000000 -> "0"; // Black
            case 0x0000AA -> "1"; // Dark blue
            case 0x00AA00 -> "2"; // Dark green
            case 0x00AAAA -> "3"; // Dark aqua
            case 0xAA0000 -> "4"; // Dark red
            case 0xAA00AA -> "5"; // Dark purple
            case 0xFFAA00 -> "6"; // Gold
            case 0xAAAAAA -> "7"; // Gray
            case 0x555555 -> "8"; // Dark gray
            case 0x5555FF -> "9"; // Blue
            case 0x55FF55 -> "a"; // Green
            case 0x55FFFF -> "b"; // Aqua
            case 0xFF5555 -> "c"; // Red
            case 0xFF55FF -> "d"; // Light purple
            case 0xFFFF55 -> "e"; // Yellow
            default -> ""; // No matching color
        };
    }
}