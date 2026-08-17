package actually.portals.ActuallySize.compatibilities.pehkui;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

/**
 * Accesses the API of Pehkui
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public class ASIPehkuiCompatibility {

    /**
     * @param mob The entity of which to check the scale
     *
     * @return The scale of this entity
     *
     * @see virtuoel.pehkui.api.ScaleTypes
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static float GetEntityScale(@NotNull Entity mob) { return GetEntityScale(mob, ScaleTypes.BASE); }

    /**
     * @param mob The entity of which to check the scale
     * @param scaleType The type of scale to check
     *
     * @return The scale of this entity
     *
     * @see virtuoel.pehkui.api.ScaleTypes
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static float GetEntityScale(@NotNull Entity mob, @NotNull ScaleType scaleType) {
        ScaleData scaleData = scaleType.getScaleData(mob);
        return scaleData.getScale();
    }

    /**
     * @param mob The entity to resize
     * @param scale The value to change this to
     *
     * @see virtuoel.pehkui.api.ScaleTypes
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static void SetEntityScaleInstant(@NotNull Entity mob, double scale) {
        SetEntityScaleInstant(mob, ScaleTypes.BASE, scale);

        /*
         * Kira-Brand Adjustments
         */
        if (!ActuallyServerConfig.kiraScalings) { return; }

        // Kira Scalings only apply to beegs
        if (scale <= 1) {
            SetEntityScaleInstant(mob, ScaleTypes.MOTION, 1);
            SetEntityScaleInstant(mob, ScaleTypes.JUMP_HEIGHT, 1);
            SetEntityScaleInstant(mob, ScaleTypes.BLOCK_REACH, 1);
            SetEntityScaleInstant(mob, ScaleTypes.ENTITY_REACH, 1); }

        // Real Kira-Adjustments
        SetEntityScaleInstant(mob, ScaleTypes.MOTION, ASIUtilities.beegBalanceResist(scale, 1, 0.5));

        SetEntityScaleInstant(mob, ScaleTypes.JUMP_HEIGHT, Math.sqrt(ASIUtilities.beegBalanceEnhance(scale, 2, 1)));
        SetEntityScaleInstant(mob, ScaleTypes.BLOCK_REACH, ASIUtilities.beegBalanceResist(scale, 1, 0.7));
        SetEntityScaleInstant(mob, ScaleTypes.ENTITY_REACH, ASIUtilities.beegBalanceResist(scale, 1, 0.6));
    }

    /**
     * @param mob The entity to resize
     * @param scaleType The type of scale to change
     * @param scale The value to change this to
     *
     * @see virtuoel.pehkui.api.ScaleTypes
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static void SetEntityScaleInstant(@NotNull Entity mob, @NotNull ScaleType scaleType, double scale) {
        ScaleData scaleData = scaleType.getScaleData(mob);
        scaleData.setScale((float) scale);
    }
}
