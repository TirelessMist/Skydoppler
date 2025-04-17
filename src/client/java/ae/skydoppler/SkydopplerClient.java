package ae.skydoppler;

import net.fabricmc.api.ClientModInitializer;

public class SkydopplerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChatNotificationHandler.loadJsonData();
	}
}