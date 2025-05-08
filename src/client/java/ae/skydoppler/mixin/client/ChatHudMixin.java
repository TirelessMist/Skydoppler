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

    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at = @At("HEAD"), method = "addMessage*")
    private void onAddMessage(Text chatMessage, CallbackInfo ci) {

        chatMessage = StyleConverter.ConvertToString(chatMessage);

        System.out.println("[ChatHudMixin] -> onAddMessage -> chatMessage = \"" + chatMessage + "\"");
        
        String returnValue = ChatMatchHandler.checkForMatches(chatMessage);

        if (returnValue.equalsIgnoreCase("\\hide")) {

            ci.cancel();
            return;

        }

        if (!returnValue.empty()) {

            ci.cancel();
            client.player.sendMessage(Text.literal(returnValue), false);
            return;

        }

    }
}