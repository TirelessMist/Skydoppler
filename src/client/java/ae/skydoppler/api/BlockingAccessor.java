package ae.skydoppler.api;

/**
 * Interface to access blocking state from the ClientPlayerEntity Mixin.
 */
public interface BlockingAccessor {
    /**
     * Checks if the player is currently blocking
     * @return true if the player is blocking, false otherwise
     */
    boolean skydoppler$isBlocking();

    /**
     * Sets the blocking state for the player
     * @param blocking the new blocking state
     */
    void skydoppler$setBlocking(boolean blocking);
}
