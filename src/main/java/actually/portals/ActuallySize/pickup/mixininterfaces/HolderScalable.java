package actually.portals.ActuallySize.pickup.mixininterfaces;

/**
 * When something may have a scale attribute to it
 * when it is held in someone's inventory
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public interface HolderScalable {

    /**
     * @param scale Scale of this thing
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    void actuallysize$setHolderScale(double scale);

    /**
     * @return Scale of this thing
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    double actuallysize$getHolderScale();
}
