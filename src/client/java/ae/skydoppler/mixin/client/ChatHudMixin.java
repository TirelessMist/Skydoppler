package ae.skydoppler.mixin.client;

import ae.skydoppler.ChatNotificationHandler;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
	@Inject(at = @At("HEAD"), method = "addMessage")
	private void onAddMessage(Text chatMessage, CallbackInfo ci) {
		ChatNotificationHandler.checkForMatches(chatMessage);
	}
}