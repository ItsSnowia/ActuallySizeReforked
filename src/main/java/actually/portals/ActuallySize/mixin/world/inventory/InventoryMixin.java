package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.pickup.mixininterfaces.PlayerBound;
import actually.portals.ActuallySize.pickup.mixininterfaces.HolderScalable;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Inventory.class)
public abstract class InventoryMixin implements Container,Nameable,PlayerBound {

    @Shadow @Final public Player player;

    @Override public void actuallysize$setBoundPlayer(@Nullable Player player) { }
    @Override public @Nullable Player actuallysize$getBoundPlayer() { return player; }

    @WrapMethod(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I")
    public int whenPickupItem(int pSlot, ItemStack pStack, Operation<Integer> original) {

        /*
         * For the sake of picking up something, pick it up
         * at my scale (real) instead of the default
         */
        if (pStack != null && pStack != ItemStack.EMPTY) {
            HolderScalable boundItem = (HolderScalable) (Object) pStack;
            boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(player)); }

        return original.call(pSlot, pStack);
    }

    @WrapMethod(method = "addResource(Lnet/minecraft/world/item/ItemStack;)I")
    public int whenPickupItem(ItemStack pStack, Operation<Integer> original) {

        /*
         * For the sake of picking up something, pick it up
         * at my scale (real) instead of the default
         */
        if (pStack != null && pStack != ItemStack.EMPTY) {
            HolderScalable boundItem = (HolderScalable) (Object) pStack;
            boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(player)); }

        return original.call(pStack);
    }
}
