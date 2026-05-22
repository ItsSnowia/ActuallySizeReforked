package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.pickup.mixininterfaces.PlayerBound;
import actually.portals.ActuallySize.pickup.mixininterfaces.HolderScalable;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow @Final public Container container;

    @WrapMethod(method = "set")
    public void whenItemSet(ItemStack pStack, Operation<Void> original) {
        original.call(pStack);
        if (pStack == ItemStack.EMPTY || pStack == null) { return; }

        // If this container is bound to a player
        if ((this.container instanceof PlayerBound)) {
            Player bound = ((PlayerBound) this.container).actuallysize$getBoundPlayer();
            if (bound != null) {

                // Make sure the item stack within is synced to this same player
                HolderScalable boundItem = (HolderScalable) (Object) pStack;
                boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(bound));
            }
        }
    }

    @WrapMethod(method = "getItem")
    public ItemStack whenItemGet(Operation<ItemStack> original) {
        ItemStack ret = original.call();
        if (ret == ItemStack.EMPTY || ret == null) { return ret; }

        // If this container is bound to a player
        if ((this.container instanceof PlayerBound)) {
            Player bound = ((PlayerBound) this.container).actuallysize$getBoundPlayer();
            if (bound != null) {

                // Make sure the item stack within is synced to this same player
                HolderScalable boundItem = (HolderScalable) (Object) ret;
                boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(bound));
            }
        }

        return ret;
    }
}
