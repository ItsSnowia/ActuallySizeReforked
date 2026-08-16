package actually.portals.ActuallySize.world;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.ActuallySizeInteractions;
import actually.portals.ActuallySize.compatibilities.create.ASICreateCompatibility;
import actually.portals.ActuallySize.world.preferences.ASIPreferencesManager;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

/**
 * The class that handles systems related to interacting
 * with the world as a beeg or a tiny.
 *
 * @since 1.0.0
 * @author Actually Portals
 */
public class ASIWorldSystemManager {

    //region Compatibility Checking
    /**
     * The result of checking if Create mod is present upon mod init
     *
     * @since 1.0.0
     */
    boolean createPresent = false;

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public boolean isCreatePresent() { return createPresent; }
    //endregion

    /**
     * Load this system onto the mod during mod loading initialization
     *
     * @param context Mod Loading context
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public void OnModLoadInitialize(FMLJavaModLoadingContext context) {

        // It will either generate the error or succeed.
        try {
            createPresent = ASICreateCompatibility.TestIfCreatePresent();
        } catch (Error ignored) {
            createPresent = false;
        }
    }

    /**
     * @since 1.0.0
     * @author Actually Portals
     */
    public void onCommonSetup() {
    }

    //region Fear
    /**
     * @param beeg The beeg
     * @param tiny The tiny
     * @param relative How much bigger is the beeg to the tiny, so 4X bigger is 4.0
     *
     * @return If the specified entity may panic when encountering a beeg
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static boolean CanPanic(@NotNull LivingEntity beeg, @NotNull LivingEntity tiny, double relative) {

        // Needs a higher panic threshold
        double panicThreshold = ActuallyServerConfig.fearThreshold * 1.5;
        if (panicThreshold > relative) { return false; }

        // Zombies do not panic
        boolean isSkeleton = tiny.getType().is(EntityTypeTags.SKELETONS);
        boolean isUndead = tiny.getMobType() == MobType.UNDEAD;
        if (isUndead && !isSkeleton) { return false; }

        // Golems do not panic
        if (tiny instanceof AbstractGolem) { return false; }

        // Creepers do not panic
        if (tiny instanceof Creeper) { return false; }

        // Everything else panics when the beeg is near
        double pos = beeg.position().distanceToSqr(tiny.position());
        double eye = beeg.position().distanceToSqr(tiny.position());
        double least = Math.min(pos, eye);
        return least < (relative * relative * 4);
    }

    /**
     * @param beeg The beeg
     * @param tiny The tiny
     * @param relative How much bigger is the beeg to the tiny, so 4X bigger is 4.0
     *
     * @return If the specified entity may fear when encountering a beeg
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static boolean CanFear(@NotNull LivingEntity beeg, @NotNull LivingEntity tiny, double relative) {

        // Must meet fear threshold
        if (ActuallyServerConfig.fearThreshold > relative) { return false; }

        // Golems do not fear
        if (tiny instanceof AbstractGolem) { return false; }

        // Everything else fears when a beeg is near
        double pos = beeg.position().distanceToSqr(tiny.position());
        double eye = beeg.position().distanceToSqr(tiny.position());
        double least = Math.min(pos, eye);
        return least < (relative * relative * 49);
    }
    //endregion

    //region Damage Taken
    /**
     * @param world The world where damage takes place
     * @param type The type of damage
     *
     * @return If this damage type is adjusted by ASI
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static boolean IsAdjustableDamage(@NotNull Level world, @NotNull DamageSource type) {

        // Fall is not affected by ASI
        if (type.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) { return false; }
        if (type == world.damageSources().fall()) { return false; }

        // Compared by type key, not instance, since ASI's own crushing damage
        // is a freshly built DamageSource sharing the vanilla cramming type
        if (type.is(DamageTypes.CRAMMING)) { return false; }

        if (type == world.damageSources().drown()) { return false; }
        if (type == world.damageSources().starve()) { return false; }
        if (type == world.damageSources().fellOutOfWorld()) { return false; }
        if (type == world.damageSources().wither()) { return false; }
        if (type == world.damageSources().outOfBorder()) { return false; }
        if (type == world.damageSources().genericKill()) { return false; }

        // Everything else is
        return true;
    }

    /**
     * Some types of damage should not be made more powerful
     * against tinies, but it is fine if beegs resist them more.
     * This doesn't make a lot of "sense" sense but if they were
     * boosted it would just make tiny play painful unnecessarily*
     *
     * @param world The world where damage takes place
     * @param type The type of damage
     *
     * @return If this damage type is adjusted by ASI for beegs
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static boolean IsAdjustedForTinies(@NotNull Level world, @NotNull DamageSource type) {

        // Walls make no sense to make more damage to tinies
        if (type == world.damageSources().inWall()) { return false; }
        if (type == world.damageSources().dragonBreath()) { return false; }
        if (type == world.damageSources().dryOut()) { return false; }
        if (type == world.damageSources().cactus()) { return false; }
        if (type == world.damageSources().hotFloor()) { return false; }
        if (type == world.damageSources().flyIntoWall()) { return false; }
        if (type == world.damageSources().sweetBerryBush()) { return false; }

        // Everything else is
        return true;
    }
    /**
     * Some types of damage should not be made more powerful
     * against tinies, but it is fine if beegs resist them more.
     * This doesn't make a lot of "sense" sense but if they were
     * boosted it would just make tiny play painful unnecessarily*
     *
     * @param world The world where damage takes place
     * @param type The type of damage
     *
     * @return If this damage type is adjusted by ASI for beegs
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static boolean IsBypassedByTinies(@NotNull Level world, @NotNull DamageSource type) {

        // Walls make no sense to make more damage to tinies
        if (type == world.damageSources().cactus()) { return true; }
        if (type == world.damageSources().sweetBerryBush()) { return true; }

        // Everything else is
        return false;
    }

    /**
     * Based on the options in the config, this method will adjust the damage
     * dealt between one or two entities of different sizes. In general, beegs
     * take less damage and tinies take more damage.
     *
     * @param originalDamage The amount of damage
     * @param victim The receiver of damage
     * @param attack The attack information
     *
     * @return Damage, adjusted to account for matters of size.
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static double ASICombatAdjust(double originalDamage, @NotNull LivingEntity victim, @NotNull DamageSource attack) {
        if (!IsAdjustableDamage(victim.level(), attack)) { return originalDamage; }
        if (!ActuallyServerConfig.strongBeegs && !ActuallyServerConfig.tankyBeegs && !ActuallyServerConfig.delicateTinies) { return originalDamage; }

        /*
         * Damage calculations use scale, not effective size. This
         * is because using effective size would make larger entities
         * that already deal more damage deal even more damage lol.
         */
        double mySize = ASIUtilities.getEntityScale(victim);
        double buffingLimit = ActuallyServerConfig.strongestBeeg;
        //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 [" + victim.getScoreboardName() + "] Adjusting hurt &3 " + originalDamage + " &r up to &b " + buffingLimit + " &f at &e x" + mySize);

        /*
         * Is there an aggressive entity? Then we must bother
         * with their damage being amplified if they are bigger.
         */
        double aggressorScale = 0;
        double aggressorAmplificationFactor = 1;
        boolean aggressorIsCrouching;
        if (attack.getDirectEntity() != null) {
            aggressorScale = ASIUtilities.getEntityScale(attack.getDirectEntity());
            aggressorIsCrouching = (attack.getDirectEntity() instanceof Player) && attack.getDirectEntity().isCrouching();
            //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 [" + attack.getDirectEntity().getScoreboardName() + "] Aggressor (" + aggressorIsCrouching + ") at &e x" + aggressorScale);

            if (aggressorScale > 0) {

                // Adjust damage based on the relative scale from aggressor to me
                double tankyFloor = 1 - ActuallyServerConfig.maxTankyDamageReduction;
                aggressorAmplificationFactor = ASIUtilities.beegBalanceResist(mySize / aggressorScale, buffingLimit, tankyFloor);

                // If the beeg is crouching, and bigger than us, there is no amplification. Tinies remain reduced tho.
                if (aggressorIsCrouching && aggressorScale > mySize) { aggressorAmplificationFactor = 1; }
            }
            //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 Aggressor Factor: &6 " + aggressorAmplificationFactor + " &r for a total &e " + (originalDamage * aggressorAmplificationFactor));

            // Combat takes precedence to the other stuff below
            return originalDamage * aggressorAmplificationFactor;
        }

        /*
         * Size matters
         */
        double sizeAmplificationFactor = 1;

        // When beeg
        if (mySize > 1 && ActuallyServerConfig.tankyBeegs) {
            //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 Tanky beeg");

            double tankyFloor = 1 - ActuallyServerConfig.maxTankyDamageReduction;
            sizeAmplificationFactor = ASIUtilities.beegBalanceResist(mySize, buffingLimit, tankyFloor);

        // When smol
        } else if (mySize < 1 && ActuallyServerConfig.delicateTinies) {
            //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 Delicate smol");
            if (mySize < 0.25 && IsBypassedByTinies(victim.level(), attack)) { return 0; }
            if (!IsAdjustedForTinies(victim.level(), attack)) { return originalDamage; }

            // Increase damage from all sources
            sizeAmplificationFactor = ASIUtilities.beegBalanceResist(mySize, buffingLimit, 0);
        }
        //ATT//ActuallySizeInteractions.Log("ASI &2 WSM &7 Size Factor: &6 " + sizeAmplificationFactor + " &r for a total &e " + (originalDamage * sizeAmplificationFactor));

        // Adjust effect
        return originalDamage * sizeAmplificationFactor;
    }
    //endregion

    //region Crushing
    /**
     * @param beeg The potentially larger entity, standing over the tiny
     * @param tiny The potentially smaller entity, being stood over
     *
     * @return If the beeg is large enough, and otherwise eligible, to crush the tiny underfoot
     *
     * @since 1.0.0
     * @author evanbones
     */
    public static boolean CanCrush(@NotNull LivingEntity beeg, @NotNull LivingEntity tiny) {

        if (!ActuallyServerConfig.enableCrushingDamage) { return false; }
        if (beeg == tiny) { return false; }
        if (beeg.isDeadOrDying() || tiny.isDeadOrDying()) { return false; }
        if (ActuallyServerConfig.crushingSneakPrevents && beeg.isCrouching()) { return false; }

        // riders/mounts, and entities sharing a vehicle, don't crush each other
        if (beeg.hasPassenger(tiny) || tiny.hasPassenger(beeg)) { return false; }
        if (beeg.getRootVehicle() == tiny.getRootVehicle() && beeg.getRootVehicle() != beeg) { return false; }

        double relative = ASIUtilities.getRelativeScale(tiny, beeg);
        return relative >= ActuallyServerConfig.crushingThreshold;
    }

    /**
     * Deals crushing damage to the tiny, scaled by how much bigger the beeg
     * standing over it is, capped by the configured limit.
     *
     * @param beeg The larger entity crushing the tiny underfoot
     * @param tiny The smaller entity being crushed
     *
     * @since 1.0.0
     * @author evanbones
     */
    public static void ApplyCrushing(@NotNull LivingEntity beeg, @NotNull LivingEntity tiny) {
        double relative = ASIUtilities.getRelativeScale(tiny, beeg);
        double damage = (relative - ActuallyServerConfig.crushingThreshold) * ActuallyServerConfig.crushingDamageMultiplier;
        if (damage <= 0) { return; }

        double limit = tiny.getMaxHealth() * ActuallyServerConfig.crushingDamageLimit;
        if (limit > 0 && damage > limit) { damage = limit; }

        DamageSource source = new DamageSource(tiny.damageSources().cramming().typeHolder(), beeg);
        tiny.hurt(source, (float) damage);
    }
    //endregion
}
