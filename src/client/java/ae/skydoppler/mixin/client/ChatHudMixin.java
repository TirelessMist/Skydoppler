package ae.skydoppler.mixin.client;

import ae.skydoppler.chat.ChatMatchHandler;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.particle.FishingParticle;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "addMessage*")
    private void onAddMessage(Text chatMessage, CallbackInfo ci) {

        System.out.println("[ChatHudMixin] -> onAddMessage -> chatMessage = \"" + chatMessage + "\"");
        ChatMatchHandler.checkForMatches(chatMessage);

    }
}