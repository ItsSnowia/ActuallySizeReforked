package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.pickup.mixininterfaces.BeegCountable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input implements BeegCountable {

    @Shadow @Final private Options options;
    @Unique boolean actuallysize$isBeegCounting;

    @Inject(method = "tick", at = @At("HEAD"))
    void onInputRead(boolean pIsSneaking, float pSneakingSpeedMultiplier, CallbackInfo ci) {
        KeyMapping beegCountKey = this.options.keyPlayerList;
        BeegCountable asCountable = (BeegCountable) beegCountKey;
        actuallysize$isBeegCounting = asCountable.actuallysize$isShowingBeegCount();
    }

    @Override public void actuallysize$setShowingBeegCount(boolean beeg) {
        actuallysize$isBeegCounting = beeg;

    }
    @Override public boolean actuallysize$isShowingBeegCount() {
        return actuallysize$isBeegCounting;
    }
}
