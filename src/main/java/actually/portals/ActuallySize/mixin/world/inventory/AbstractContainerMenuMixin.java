package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.pickup.mixininterfaces.HolderScalable;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Unique @Nullable Player actuallysize$latestClicker;

    @WrapMethod(method = "doClick")
    public void whenCarriedClicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer, Operation<Void> original) {

        actuallysize$latestClicker = pPlayer;
        original.call(pSlotId, pButton, pClickType, pPlayer);
        actuallysize$latestClicker = null;
    }

    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack whenCarriedAccessed(AbstractContainerMenu instance, Operation<ItemStack> original) {

        ItemStack ret = original.call(instance);

        /*
         * If this item is valid, and we have a player in the middle of clicking,
         * sync up the inventory scale of that player as to pickup ad infinitude
         */
        if (actuallysize$latestClicker != null && ret != null && ret != ItemStack.EMPTY) {
            HolderScalable boundItem = (HolderScalable) (Object) ret;
            boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(actuallysize$latestClicker)); }

        return ret;
    }

    @WrapMethod(method = "moveItemStackTo")
    public boolean whenQuickMoving(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection, Operation<Boolean> original) {

        /*
         * If this item is valid, and we have a player in the middle of clicking,
         * sync up the inventory scale of that player as to pickup ad infinitude
         */
        if (actuallysize$latestClicker != null && pStack != null && pStack != ItemStack.EMPTY) {
            HolderScalable boundItem = (HolderScalable) (Object) pStack;
            boundItem.actuallysize$setHolderScale(ASIUtilities.getEntityScale(actuallysize$latestClicker)); }
        
        // Proceed as normal
        return original.call(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }
}
