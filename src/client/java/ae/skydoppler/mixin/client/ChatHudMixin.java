package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.chat.StyleConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at = @At("HEAD"), method = "addMessage*", cancellable = true)
    private void onAddMessage(Text chatMessage, CallbackInfo ci) {

        String messageString = StyleConverter.ConvertToFormattedString(chatMessage);

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("[ChatHudMixin] -> onAddMessage -> chatMessage = \"" + chatMessage + "\"");

        String returnValue = ChatMatchHandler.checkForMatches(messageString);

        if (returnValue.startsWith("\\hide")) {

            if (returnValue.length() > 5) {

                returnValue = returnValue.substring(5);

            } else {

                returnValue = "";

            }

            ci.cancel();

        }

        if (!returnValue.isEmpty()) {

            if (client.player != null) {

                client.player.sendMessage(Text.literal(returnValue), false);
                return;

            }

        }

    }
}