package actually.portals.ActuallySize.world.preferences;

import actually.portals.ActuallySize.netcode.packets.serverbound.ASINSPreferredSize;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * A manager class for everything related to player preferences
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public class ASIPreferencesManager {

    /**
     * @param who The player to set their preferred size.
     * @param scale The scale they are said to prefer
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void SetPreferredSize(@NotNull ServerPlayer who, @NotNull ASINSPreferredSize scale) { SetPreferredSize(GetEffectiveUUID(who), scale); }

    /**
     * @param who The player who you seek their preferred size.
     *
     * @return The known preferred size of this player.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    @Nullable public ASINSPreferredSize GetPreferredSize(@NotNull ServerPlayer who) { return GetPreferredSize(GetEffectiveUUID(who)); }

    /**
     * @param who The player whose UUID you seek
     *
     * @return The UUID that is used, for this player, by the Preferred Size system.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    @NotNull public UUID GetEffectiveUUID(@NotNull ServerPlayer who) {

        // If there are valid sessions, those are good enough for this purpose
        RemoteChatSession session = who.getChatSession();
        if (session != null) { return session.sessionId(); }

        // If not, we must get creative
        return who.getUUID();
    }

    /**
     * @param who The player to set their preferred size.
     * @param scale The scale they are said to prefer
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void SetPreferredSize(@NotNull UUID who, @NotNull ASINSPreferredSize scale) {
        preferredSizes.put(who, scale);
    }

    /**
     * @param who The UUID of who you seek their preferred size.
     *
     * @return The known preferred size of this player.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    @Nullable public ASINSPreferredSize GetPreferredSize(@NotNull UUID who) {
        return preferredSizes.get(who);
    }

    /**
     * Not so much as a network packet, but as the system that
     * keeps track of the Preferred Sizes of player, the collection
     * to store everyone's preferred size
     *
     * @since 1.0.0
     */
    @NotNull public final HashMap<UUID, ASINSPreferredSize> preferredSizes = new HashMap<>();
}
