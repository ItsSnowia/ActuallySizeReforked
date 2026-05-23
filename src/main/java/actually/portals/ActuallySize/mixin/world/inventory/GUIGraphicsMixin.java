package actually.portals.ActuallySize.mixin.world.inventory;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.ActuallySizeInteractions;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import gunging.ootilities.GungingOotilitiesMod.ootilityception.OotilityNumbers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiGraphics.class)
public class GUIGraphicsMixin {

    @WrapMethod(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    public void OnItemCountRendering(Font pFont, ItemStack pStack, int pX, int pY, String pText, Operation<Void> original) {
        if (pStack.isEmpty()) { original.call(pFont, pStack, pX, pY, pText); return; }
        if (pText != null) { original.call(pFont, pStack, pX, pY, pText); return; }

        LocalPlayer local = Minecraft.getInstance().player;
        if (local == null) { original.call(pFont, pStack, pX, pY, null); return; }
        if (local.isShiftKeyDown()) { original.call(pFont, pStack, pX, pY, null); return;}

        if (!ActuallyServerConfig.beegBuilding) { original.call(pFont, pStack, pX, pY, null); return; }
        if (ActuallyServerConfig.beegInventoryPower <= 1) { original.call(pFont, pStack, pX, pY, null); return; }
        if (ActuallyServerConfig.reducedBeegBuildingDrops) { original.call(pFont, pStack, pX, pY, null); return; }
        if (!ActuallySizeInteractions.WORLD_SYSTEM.canBeBeegBlock(pStack)) { original.call(pFont, pStack, pX, pY, null); return; }

        /*
         * Beeg Inventory system makes building blocks kinda scuffed...
         * Especially when holding 32767 of the same thing.
         *
         * Anyhow, it is nicer if they render their count in a
         * better way, because this will not do. Divide the total
         * count of beeg building blocks back to their equivalent
         * in normal blocks.
         */

        // So first calculate how many items this would be displayed as
        int count = pStack.getCount();
        int gridSize = OotilityNumbers.ceil(ASIUtilities.getEntityScale(local));
        if (gridSize <= 1) { original.call(pFont, pStack, pX, pY, null); return; }
        int inventoryScale = OotilityNumbers.round(Math.pow(gridSize, ActuallyServerConfig.beegInventoryPower));
        double reducedScale = (double) count / (double) inventoryScale;

        // It only makes to reduce the scale when the count exceeds 1, or 999 that overflows the display
        String countDisplay = null;
        if (reducedScale > 1 || count > 999) {

            // Indicate the reduced count
            int reducedCount = OotilityNumbers.floor(reducedScale);
            countDisplay = ChatFormatting.AQUA.toString() + reducedCount;

            // Indicate that an eight block can be built
            double residual = reducedScale - reducedCount;
            if (residual >= 0.125) { countDisplay = countDisplay + "+"; }
        }

        original.call(pFont, pStack, pX, pY, countDisplay);
    }
}
