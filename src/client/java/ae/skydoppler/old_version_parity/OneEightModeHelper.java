package ae.skydoppler.old_version_parity;

import ae.skydoppler.SkydopplerClient;

public class OneEightModeHelper {
    // 1.8 crouch height (eye height and bounding box offset)
    public static final float CROUCH_HEIGHT_1_8 = 1.75f; // vanilla 1.8 crouch eye height
    public static final float CROUCH_HEIGHT_MODERN = 1.54f; // modern crouch eye height

    public static final float CROUCH_HEIGHT_1_8_BBOX = 1.8f; // bounding box height for crouch in 1.8
    public static final float CROUCH_HEIGHT_MODERN_BBOX = 1.5f; // bounding box height for crouch in modern versions

    public static final float CROUCH_MOVEMENT_SPEED_1_8 = 0.3f; // crouch movement speed in 1.8

    public static final float BLOCKING_ROT_X = -86.6f;
    public static final float BLOCKING_ROT_Y = 18.68f;
    public static final float BLOCKING_ROT_Z = 72.17f;

    public static float getCrouchHeight() {
        return SkydopplerClient.CONFIG.oldVersionParityConfig.doOldCrouchHeight ? CROUCH_HEIGHT_1_8 : CROUCH_HEIGHT_MODERN;
    }
}


