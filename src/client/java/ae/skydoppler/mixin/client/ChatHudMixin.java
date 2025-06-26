package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.chat.StyleConverter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(at = @At("HEAD"), method = "addMessage*", cancellable = true)
    private void onAddMessage(Text chatMessage, CallbackInfo ci) {

        String messageString = StyleConverter.ConvertToFormattedString(chatMessage);

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("[ChatHudMixin] -> onAddMessage -> chatMessage = \"" + chatMessage + "\"");

        boolean returnValue = ChatMatchHandler.matchChatMessage(messageString);

        if (returnValue) ci.cancel(); // Cancel the message from being added to the chat

    }

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            index = 3,
            argsOnly = true
    )
    private MessageIndicator forceSinglePlayerIndicator(MessageIndicator original) {
        if (SkydopplerClient.CONFIG.vanillaHudConfig.hideChatIndicators) {
            return null;
        }
        return original;
    }
}