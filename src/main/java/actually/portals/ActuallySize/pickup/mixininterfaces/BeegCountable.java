package actually.portals.ActuallySize.pickup.mixininterfaces;

/**
 * When something has a separate true to beeg count,
 * this will tell you which the Local client is asking
 * to see
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public interface BeegCountable {

    /**
     * @param beeg If the beeg count should be shown, as opposed to the normal count
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    void actuallysize$setShowingBeegCount(boolean beeg);

    /**
     * @return If the beeg count should be shown, as opposed to the normal count
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    boolean actuallysize$isShowingBeegCount();
}
