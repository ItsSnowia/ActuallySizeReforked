package actually.portals.ActuallySize.mixin.world.building;

import actually.portals.ActuallySize.ASIUtilities;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gunging.ootilities.GungingOotilitiesMod.ootilityception.OotilityNumbers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public abstract class BlockMixin {

    @WrapOperation(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    public void OnMiningFoodExhaust(Player instance, float pExhaustion, Operation<Void> original) {

        // Giants get reduced block mining hunger, they are mining a volume of blocks after all
        double size = ASIUtilities.getEffectiveSize(instance);
        if (size > 1) {
            size = 1D / OotilityNumbers.ceil(size);
            pExhaustion *= (float) (size * size * size);
            original.call(instance, pExhaustion);
            return;
        }

        // Otherwise, normal breaking
        original.call(instance, pExhaustion);
    }
}
