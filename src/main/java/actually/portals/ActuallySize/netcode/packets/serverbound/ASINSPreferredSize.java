package actually.portals.ActuallySize.netcode.packets.serverbound;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.ActuallySizeInteractions;
import actually.portals.ActuallySize.world.mixininterfaces.PreferentialOptionable;
import actually.portals.ActuallySize.world.preferences.events.ASIApplyPreferredSizeEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * An example packet where the clients tell the server what
 * size they prefer to respawn or log-in as. I believe a similar
 * result can be achieved with configuration files directly, but
 * I wanted a quick and easy server-bound Network Packet example
 * to test and work with.
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public class ASINSPreferredSize {

    /**
     * The scale the local player prefers to be
     *
     * @since 1.0.0
     */
    private final boolean specialHoldPlayers;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public boolean isSpecialHoldPlayers() { return specialHoldPlayers; }

    /**
     * The scale the local player prefers to be
     *
     * @since 1.0.0
     */
    private final double preferredSize;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public double getPreferredSize() { return preferredSize; }

    /**
     * If this player prefers to be beeg
     *
     * @since 1.0.0
     */
    private final boolean preferredBeeg;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public boolean isPreferredBeeg() { return preferredBeeg; }

    /**
     * If this player prefers to be smol
     *
     * @since 1.0.0
     */
    private final boolean preferredSmol;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public boolean isPreferredSmol() { return preferredSmol; }

    /**
     * @param preferredSize The scale the local player prefers to be
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public ASINSPreferredSize(double preferredSize, boolean beeg, boolean smol, boolean holdOnlyPlayers) {
        this.preferredSize = preferredSize;
        this.preferredBeeg = beeg;
        this.preferredSmol = smol;
        this.specialHoldPlayers = holdOnlyPlayers;
    }

    /**
     * @param buff A buffer with a single double value, representing
     *             the scale the local player prefers to be.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public ASINSPreferredSize(@NotNull FriendlyByteBuf buff) {
        this(buff.readDouble(), buff.readBoolean(), buff.readBoolean(), buff.readBoolean());
    }

    /**
     * @param buff A buffer in which to write a single double value,
     *             the scale the local player prefers to be.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void encode(@NotNull FriendlyByteBuf buff) {
        buff.writeDouble(preferredSize);
        buff.writeBoolean(preferredBeeg);
        buff.writeBoolean(preferredSmol);
        buff.writeBoolean(specialHoldPlayers);
    }

    /**
     * This method is usually called when respawning a player, and also
     * when they first log in to the server in this session. This means
     * that your preferences are only re-applied upon death and upon
     * server reboot. You may send updated preferences in between, and
     * they will be recorded, but they won't apply until you die.
     *
     * @param someone The person to apply these preferences to
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void applyTo(@NotNull ServerPlayer someone) {

        ASIApplyPreferredSizeEvent playerEvent = new ASIApplyPreferredSizeEvent(someone, this);
        boolean canceled = MinecraftForge.EVENT_BUS.post(playerEvent);
        if (canceled) { return; }

        double pref = toDouble();

        // Formalities~
        PreferentialOptionable optionable = (PreferentialOptionable) someone;
        ASIUtilities.setEntityScale(someone, pref);
        optionable.actuallysize$setPreferredOptionsApplied(pref);
    }

    /**
     * @return The actual scale these Preferred Size options
     *         will apply, considering server configuration.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public double toDouble() {
        double ret = 1;

        // Interpret settings
        double serverBeeg = ActuallyServerConfig.beegSize;
        boolean serverBeegEnabled = serverBeeg != 1;

        double serverTiny = ActuallyServerConfig.tinySize;
        boolean serverTinyEnabled = serverTiny != 1;

        boolean serverFree = ActuallyServerConfig.enableFreeSize;
        boolean preferredFree = getPreferredSize() != 1;

        // Highest priority to free choice
        if (serverFree && preferredFree) {

            // Clamp between an okay minimum and maximum
            double adjusted = getPreferredSize();
            if (adjusted > 50) { adjusted = 50; }
            if (adjusted < 0.02) { adjusted = 0.02; }

            // Use preferred size
            ret = adjusted;

        // Alternatively, do we want to play beeg?
        } else if (serverBeegEnabled && isPreferredBeeg()) {

            // Use beeg size
            ret = serverBeeg;
        } else if (serverTinyEnabled && isPreferredSmol()) {

            // Use tiny size
            ret = serverTiny;
        }

        // Normalize size, no preferences
        return ret;
    }

    /**
     * The server will take a note of the current preferred scale
     * of this player, updating the static table.
     *
     * @param contextSupplier The context by which this packet is handled
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void handle(@NotNull Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {

            // Find the player who is sending this packet
            ServerPlayer player = contextSupplier.get().getSender();
            if (player == null) { return; }
            PreferentialOptionable optionable = (PreferentialOptionable) player;

            // First time? Apply these
            if (!optionable.actuallysize$isPreferredOptionsApplied(toDouble())) { applyTo(player); }

            // Apply those settings
            ActuallySizeInteractions.getInstance().getPreferencesSystem().SetPreferredSize(player, this);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
