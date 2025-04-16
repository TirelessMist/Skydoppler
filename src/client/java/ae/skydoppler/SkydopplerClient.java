package ae.skydoppler;

import net.fabricmc.api.ClientModInitializer;

public class SkydopplerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		for (module m in ChatHudMixin.getAllActiveModules()) {

		}
	}
}