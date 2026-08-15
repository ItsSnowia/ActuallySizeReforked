package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.pickup.item.ASIPSHeldEntityItem;
import actually.portals.ActuallySize.pickup.mixininterfaces.HolderScalable;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import gunging.ootilities.GungingOotilitiesMod.ootilityception.OotilityNumbers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<ItemStack> implements net.minecraftforge.common.extensions.IForgeItemStack, HolderScalable {
    @Shadow public abstract Component getDisplayName();

    @Shadow private int count;

    protected ItemStackMixin(Class<ItemStack> baseClass) { super(baseClass); }

    @Unique double actuallysize$inPlayerInventory = 1;
    @Override public void actuallysize$setHolderScale(double scale) { actuallysize$inPlayerInventory = scale; }
    @Override public double actuallysize$getHolderScale() { return actuallysize$inPlayerInventory; }

    @WrapMethod(method = "getMaxStackSize")
    public int whenMaxStackSize(Operation<Integer> original) {

        // Obtain the max stack size of this item
        int ret = original.call();
        if (((Object) this) == ItemStack.EMPTY) { return ret; }

        // If inventory is increased for beegs
        if (ActuallyServerConfig.beegInventoryPower >= 1) {

            // Must be bound to a player
            if (actuallysize$inPlayerInventory > 1) {

                int gridSize = OotilityNumbers.ceil(actuallysize$inPlayerInventory);
                if (gridSize > 1 && ret > 1) { ret *= gridSize; }
            }
        }

        return ret;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    public void whenLoadedFromNBT(CompoundTag pCompoundTag, CallbackInfo ci) {

        // If advanced count saving is needed
        if (pCompoundTag.contains(ASIPSHeldEntityItem.TAG_INT_COUNT)) {
            this.count = pCompoundTag.getInt(ASIPSHeldEntityItem.TAG_INT_COUNT); }
    }

    @Inject(method = "save", at = @At("RETURN"))
    public void whenSavedToNBT(CompoundTag pCompoundTag, CallbackInfoReturnable<CompoundTag> cir) {

        // If advanced count saving is needed
        if (this.count > 127) { pCompoundTag.putInt(ASIPSHeldEntityItem.TAG_INT_COUNT, this.count); }
    }

    @WrapMethod(method = "copy")
    public ItemStack whenCloned(Operation<ItemStack> original) {
        ItemStack ret = original.call();

        /*
         *  When picking items up, a copy of the item is made to
         *  select which slot to put this item into. This copy
         *  will not be scaled (and thus default to normal Max Stack)
         *  if the scale is not copied.
         *
         *  This fixes being able to pickup increased stacks when beeg
         */
        if (ret != null && ret != ItemStack.EMPTY) {
            HolderScalable boundItem = (HolderScalable) (Object) ret;
            boundItem.actuallysize$setHolderScale(actuallysize$inPlayerInventory); }

        return ret;
    }
}
