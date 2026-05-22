package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.pickup.mixininterfaces.PlayerBound;
import gunging.ootilities.GungingOotilitiesMod.ootilityception.OotilityNumbers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Container.class)
public interface ContainerMixin {

    /**
     * @author Actually Portals
     *
     * @reason To modify the stack size from a hard-coded 64 to
     *         something that scales with an entity's scale
     */
    @Overwrite
    default int getMaxStackSize() {

        int ret = 64;
        if (this instanceof PlayerBound && ActuallyServerConfig.beegInventoryPower >= 1) {

            Player bound = ((PlayerBound) this).actuallysize$getBoundPlayer();
            if (bound != null) {

                /*
                 * The max slot size scales with a power of the scale, by default
                 * for compatibility beeg building this is set to a power of 3.
                 * Thus, breaking 1 block at scale 8 that is 8x8x8 = 512 blocks
                 * allows you to pick up all of them at the same inventory cost of
                 * breaking a single block at normal scale.
                 */
                int gridSize = OotilityNumbers.ceil(ASIUtilities.getEntityScale(bound));
                if (gridSize > 1) {
                    gridSize = OotilityNumbers.round(Math.pow(gridSize, ActuallyServerConfig.beegInventoryPower));
                    ret *= gridSize; } }
        }

        return ret;
    }
}
