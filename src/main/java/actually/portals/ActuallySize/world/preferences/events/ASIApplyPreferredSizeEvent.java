package actually.portals.ActuallySize.world.preferences.events;

import actually.portals.ActuallySize.netcode.packets.serverbound.ASINSPreferredSize;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when player size preferences are applied
 *
 * @since 1.0.0
 * @author Actually Portals
 */
@Cancelable
public class ASIApplyPreferredSizeEvent extends PlayerEvent {

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public @NotNull ASINSPreferredSize getPrefs() { return prefs; }

    /**
     * The preferences being applied
     *
     * @since 1.0.0
     */
    @NotNull final ASINSPreferredSize prefs;

    /**
     * @return  The player to whom these preferences will be applied to
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    @Override @NotNull public ServerPlayer getEntity() { return (ServerPlayer) super.getEntity(); }

    /**
     * @param player The player to whom these preferences will be applied to
     * @param prefs The preferences being applied
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public ASIApplyPreferredSizeEvent(@NotNull ServerPlayer player, @NotNull ASINSPreferredSize prefs) {
        super(player);
        this.prefs = prefs;
    }
}
