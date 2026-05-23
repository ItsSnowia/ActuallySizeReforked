package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.pickup.mixininterfaces.BeegCountable;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer implements BeegCountable {

    @Shadow public Input input;

    public LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) { super(pClientLevel, pGameProfile); }

    @Override public void actuallysize$setShowingBeegCount(boolean beeg) {
        Input myInput = this.input;
        if (!(myInput instanceof BeegCountable)) { return; }

        BeegCountable asCountable = (BeegCountable) input;
        asCountable.actuallysize$setShowingBeegCount(beeg);

    }
    @Override public boolean actuallysize$isShowingBeegCount() {
        Input myInput = this.input;
        if (!(myInput instanceof BeegCountable)) { return false; }

        BeegCountable asCountable = (BeegCountable) input;
        return asCountable.actuallysize$isShowingBeegCount();
    }
}
