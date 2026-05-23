package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.pickup.mixininterfaces.BeegCountable;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements Comparable<KeyMapping>, net.minecraftforge.client.extensions.IForgeKeyMapping, BeegCountable {

    @Shadow boolean isDown;
    @Shadow public abstract void setDown(boolean pValue);

    @Override public void actuallysize$setShowingBeegCount(boolean beeg) { setDown(!beeg); }
    @Override public boolean actuallysize$isShowingBeegCount() { return !isDown; }
}
