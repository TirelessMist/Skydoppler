package ae.skydoppler.chat;

import net.minecraft.text.Text;

public class StyleConverter {

    public static Text ConvertToText(String formattedString) {

        return Text.empty(); // convert a String of text with the formatting character '§' to the Minecraft Text class with siblings that are groups of text with their respective styling properties

    }

    public static String ConvertToString(Text formattedText) {

        return String.empty(); // convert a Minecraft Text object to a String with the formatting character '§' by taking each Text sibling and assembling the String sibling-by-sibling, attatching the formatting codes to the portions of the String.

    }

}