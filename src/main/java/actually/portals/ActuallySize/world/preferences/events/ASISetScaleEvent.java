package actually.portals.ActuallySize.world.preferences.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when scale of an entity is changed... at least by ASI
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public class ASISetScaleEvent extends EntityEvent {

    /**
     * The scale to which this entity is going to be grown to
     *
     * @since 1.0.0
     */
    double scale;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public double getScale() { return scale; }

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public void setScale(double scale) { this.scale = scale; }

    /**
     * @param entity The entity that will experience growth
     * @param scale The scale to which it will grow
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public ASISetScaleEvent(@NotNull Entity entity, double scale) {
        super(entity);
        this.scale = scale;
    }
}
