package actually.portals.ActuallySize;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Configuration of the allowed interactions. Mostly server-sided.
 *
 * @since 1.0.0
 * @author Actually Portals
 */
@Mod.EventBusSubscriber(modid = ActuallySizeInteractions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ActuallyServerConfig {

    //region Config Parameters

    /**
     * Whether the "Pickup Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue USE_PRACTICAL_SIZE;

    /**
     * Whether the "Pickup Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue ENABLE_ENTITY_PICKUP;

    /**
     * Whether the "Holding Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue ENABLE_ENTITY_HOLDING;

    /**
     * The default size for beegs, clients that enabled it in their config will join like this.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue BEEG_SIZE;

    /**
     * The default size for tinies, clients that enabled it in their config will join like this.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue TINY_SIZE;

    /**
     * The default size for tinies, clients that enabled it in their config will join like this.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue FREE_SIZE;

    /**
     * If beegs receive less damage and knockback from all sources
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue BEEGS_ARE_TANKY;

    /**
     * If beegs deal more melee damage, unless they crouch to avoid hitting too hard.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue BEEGS_ARE_STRONG;

    /**
     * If tinies receive more damage from all sources (does not affect knockback)
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue TINIES_ARE_DELICATE;

    /**
     * If beegs deal more melee damage, unless they crouch to avoid hitting too hard.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue SIZE_DAMAGE_LIMIT;

    /**
     * The relative scale between a player and an entity so that the player can ride the entity
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue SCALE_LIMIT_RIDE;

    /**
     * Whether beegs deal crushing damage to tinies they walk over
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue ENABLE_CRUSHING_DAMAGE;

    /**
     * The relative scale needed between a beeg and a tiny for crushing damage to occur
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue CRUSHING_THRESHOLD;

    /**
     * The damage dealt per multiple of relative scale above the crushing threshold
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue CRUSHING_DAMAGE_MULTIPLIER;

    /**
     * The maximum amount of crushing damage that may be dealt in a single hit, as a multiple of the victim's max health
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue CRUSHING_DAMAGE_LIMIT;

    /**
     * Whether sneaking prevents a beeg from crushing tinies underfoot
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue CRUSHING_SNEAK_PREVENTS;

    /**
     * Whether food is nerfed for giants and buffed for tinies. Also affects eat animation time.
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue BEEGS_ARE_HUNGRY;

    /**
     * Extends the duration of effects granted upon consumption of held entities
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue BEEGS_ARE_HUNGRY_DURATION;

    /**
     * Increases the frequency of effects granted upon consumption of held entities
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue BEEGS_ARE_HUNGRY_FREQUENCY;

    /**
     * At what scale does your size cause panic on monsters
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue FEAR_THRESHOLD;

    /**
     * If beegs receive less damage and knockback from all sources
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.IntValue BEEG_INVENTORY_POWER;

    /**
     * Scales various attributes for a "smoother" gameplay when beeg
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue KIRA_SCALINGS;

    /**
     * Whether the "Pickup Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue TINIEST_CRANKER;

    /**
     * Whether the "Pickup Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue LARGEST_CRANKER;

    /**
     * Whether the "Pickup Entity" system is enabled at all
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue WATER_WHEEL_CRANKER;

    /**
     * The config builder itself
     *
     * @since 1.0.0
     */
    @NotNull static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        //region Gameplay
        CONFIG_BUILDER.push("general");

        CONFIG_BUILDER.push("pickup");
        ENABLE_ENTITY_PICKUP = CONFIG_BUILDER.comment("§eAllows picking up entities as items")
                .comment("Enables the ability to pick up entities as items, if you are big enough to hold them in your mainhand or offhand. To pick up players, the Allow Hold must also be enabled.")
                .define("allowPickup", true);

        ENABLE_ENTITY_HOLDING = CONFIG_BUILDER.comment("§eAllows holding live entities")
                .comment("When picking up an entity as an item, holding this item in your hand will also spawn the entity in your hand in the world. Required to pickup players.")
                .define("allowHold", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("sizes");
        BEEG_SIZE = CONFIG_BUILDER.comment("§eThe size of beegs")
                .comment("Players may indicate they prefer to be beeg, this is the size they will have by default when joining and respawning. Set to '1' to disable this feature.")
                .defineInRange("beegSize", 8, 0.05, 25);

        TINY_SIZE = CONFIG_BUILDER.comment("§eThe size of tinies")
                .comment("Players may indicate they prefer to be tiny, this is the size they will have by default when joining and respawning. Set to '1' to disable this feature.")
                .defineInRange("tinySize", 0.13, 0.05, 25);

        FREE_SIZE = CONFIG_BUILDER.comment("§ePlayers may choose a size")
                .comment("Gives players the option to freely choose whatever scale they want between 0.05x and 25x, to have as their default scale when joining.")
                .define("allowFreeSize", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.pop();
        //endregion

        //region Gameplay
        CONFIG_BUILDER.push("gameplay");

        CONFIG_BUILDER.push("building");
        BEEG_INVENTORY_POWER = CONFIG_BUILDER.comment("§eIncreases max stack size for giants")
                .comment("The max stack size of all items in the inventory of giants is increased by their scale. The max stack of building blocks is scaled to this power of their scale. Set to 0 to disable this system.")
                .defineInRange("beegInventoryPower", 3,0, 3);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("combat");
        BEEGS_ARE_TANKY = CONFIG_BUILDER.comment("§eReduces all damage taken by beegs")
                .comment("Beegs will receive less damage from most sources the bigger they are. However, in combat, larger attackers suffer less damage reduction. Knockback is also reduced just for the fact of being beeg. ")
                .define("beegsAreTanky", true);

        BEEGS_ARE_STRONG = CONFIG_BUILDER.comment("§eIncreases damage dealt by beegs")
                .comment("Beegs will deal more damage in melee attacks to entities smaller than them.  You may crouch while punching unarmed to disable this for that one punch, so it will be a normal vanilla punch. Also affects knockback between different-sized combatants. ")
                .define("beegsAreStrong", true);

        TINIES_ARE_DELICATE = CONFIG_BUILDER.comment("§eIncreases the damage taken by tinies")
                .comment("Tinies will receive more damage from most sources the smaller they are. However, in combat, smaller attackers get less damage amplification. When punched by a player, if that player is crouching, their damage is not amplified at all. The [Beegs Are Strong] option takes precedence in combat (so that only [Beegs Are Strong] is applied). ")
                .define("tiniesAreDelicate", true);

        SIZE_DAMAGE_LIMIT = CONFIG_BUILDER.comment("§eCombat Damage amplification for giants")
                .comment("If you would take more damage because you are small, the maximum multiplier. If you would deal bonus damage due to being beeg, the maximum multiplier. ")
                .defineInRange("sizeDamageAmplifier", 25D, 1D, Double.MAX_VALUE);

        FEAR_THRESHOLD = CONFIG_BUILDER.comment("§eMonsters will fear giants")
                .comment("This is the scale where monsters begin to fear you if you are a beeg, some may even panic. For example, 4.0 means monsters fear giants 4x bigger than them")
                .defineInRange("fearThreshold", 4D,1D, Double.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("food");
        BEEGS_ARE_HUNGRY = CONFIG_BUILDER.comment("§eFood gives less nutrition to giants")
                .comment("This will make food a lot less effective the bigger you are, and feed and saturate more if you are small. Also results in beegs eating faster and tinies taking longer to eat, depending on the nutritional value of food (tinies should prefer berries, very nourishing to them hehe)")
                .define("beegsAreHungry", true);

        BEEGS_ARE_HUNGRY_DURATION = CONFIG_BUILDER.comment("§eMultiplies foodie effect duration")
                .comment("This is a multiplier to increase or decrease effects granted when beegs eat animals (such as strength from horses) or other mobs. Disables this feature when ZERO")
                .defineInRange("effectDurationMultiplier", 1D,0D, Double.MAX_VALUE);

        BEEGS_ARE_HUNGRY_FREQUENCY = CONFIG_BUILDER.comment("§eMultiplies foodie effect chance")
                .comment("This is a multiplier to how often effects are granted by eating live animals (such as night vision from glow squids). Does not affect boss entities.")
                .defineInRange("effectChanceMultiplier", 1D,0D, Double.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("create");
        TINIEST_CRANKER = CONFIG_BUILDER.comment("§eTinies cannot use hand cranks")
                .comment("The smallest size you can be and still be able to use the hand crank to produce power. The power output and speed will be reduced the tinier you are. ")
                .defineInRange("tiniestCranker", 0.4, 0, 1);

        LARGEST_CRANKER = CONFIG_BUILDER.comment("§eBeegs break hand cranks")
                .comment("The biggest size at which the hand crank can be used, after which, you may accidentally break it because you are too strong to rotate the tiny thing. The power output will still be increased the bigger you are. To disable this, just set it to an absurdly large scale (such as 1000x)")
                .defineInRange("largestCranker", 6, 1, Double.MAX_VALUE);

        WATER_WHEEL_CRANKER = CONFIG_BUILDER.comment("§eBeegs hand-crank L. Waterwheels")
                .comment("At what size is a large waterwheel a normal hand crank to you? From then, it will use the minimum and maximum crankshaft limits to decide how it gets buffed or nerfed based on your size.")
                .defineInRange("waterwheelCranker", 5, 1, Double.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.pop();
        //endregion

        //region Advanced
        CONFIG_BUILDER.push("advanced");

        USE_PRACTICAL_SIZE = CONFIG_BUILDER.comment("§eSize math is influenced by hitbox size")
                .comment("Some entities are already small, and some are pretty big. For example, a ravager VS a chicken. For size calculations, it feels better to use bigger numbers for massive entities, I call this their 'practical size.'")
                .define("usePracticalSize", true);

        KIRA_SCALINGS = CONFIG_BUILDER.comment("§eScales various attributes for smoother giants")
                .comment("Giants may feel too much, this option reduces various attributes for smoother gameplay for casual players. Personally I, Portals, a Terraria player, prefer it when giants are absolutely cracked with super speed and super reach tho LOL.")
                .define("kiraScalings", true);

        SCALE_LIMIT_RIDE = CONFIG_BUILDER.comment("§eMaximum rider size compared to mount")
                .comment("The relative scale (ex. 2x) upper limit to riding another entity. If you are this much bigger, you won't be able to ride the other. Prevents giants from riding normal horses and such, but there will be no problem riding giant horses. Size matters!")
                .defineInRange("ridingScaleLimit", 2D, 0D, Double.MAX_VALUE);

        CONFIG_BUILDER.pop();
        //endregion

        //region Crushing
        CONFIG_BUILDER.push("crushing");

        ENABLE_CRUSHING_DAMAGE = CONFIG_BUILDER.comment("§eBeegs crush tinies underfoot")
                .comment("If a player or entity is enough bigger than another, simply standing over/on top of the smaller one will deal crushing damage, scaled by how much bigger they are.")
                .define("enableCrushingDamage", true);

        CRUSHING_THRESHOLD = CONFIG_BUILDER.comment("§eMinimum size difference to crush")
                .comment("How many times bigger a beeg must be than a tiny before they start dealing crushing damage by standing on them.")
                .defineInRange("crushingThreshold", 4D, 1D, Double.MAX_VALUE);

        CRUSHING_DAMAGE_MULTIPLIER = CONFIG_BUILDER.comment("§eCrushing damage strength")
                .comment("Crushing damage dealt per multiple of relative scale above the crushing threshold. For example, at the default of 1, being crushed by someone 10x bigger than the crushing threshold deals roughly 10 damage.")
                .defineInRange("crushingDamageMultiplier", 1D, 0D, Double.MAX_VALUE);

        CRUSHING_DAMAGE_LIMIT = CONFIG_BUILDER.comment("§eCrushing damage cap")
                .comment("The maximum crushing damage that can be dealt in a single hit, as a multiple of the victim's max health. Set higher to allow crushing to be instantly fatal.")
                .defineInRange("crushingDamageLimit", 2D, 0D, Double.MAX_VALUE);

        CRUSHING_SNEAK_PREVENTS = CONFIG_BUILDER.comment("§eSneaking avoids crushing tinies")
                .comment("If the beeg is crouching, they will not deal crushing damage to tinies underfoot, similarly to how crouching avoids other size-related interactions.")
                .define("crushingSneakPrevents", true);

        CONFIG_BUILDER.pop();
        //endregion

        SPEC = CONFIG_BUILDER.build();
    }
    //endregion

    //region Config Object
    public static boolean enableEntityPickup;
    public static boolean enableEntityHolding;
    public static boolean enableFreeSize;

    public static boolean kiraScalings;
    public static boolean usePracticalSize;
    public static double scaleLimitRider;
    public static boolean tankyBeegs;
    public static int beegInventoryPower;
    public static boolean strongBeegs;
    public static boolean hungryBeegs;
    public static boolean delicateTinies;
    public static double strongestBeeg;
    public static double beegSize, tinySize;
    public static double foodDuration, foodFrequency;
    public static double fearThreshold;
    public static double largestCranker, tiniestCranker, waterwheelCranker;

    public static boolean enableCrushingDamage;
    public static double crushingThreshold;
    public static double crushingDamageMultiplier;
    public static double crushingDamageLimit;
    public static boolean crushingSneakPrevents;

    /**
     * Reads the values specified in the config and loads them
     * to their static variables to be accessed from anywhere
     * in the mod.
     *
     * @param event The mod config loading event
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) { return; }

        enableEntityPickup = ENABLE_ENTITY_PICKUP.get();
        enableEntityHolding = ENABLE_ENTITY_HOLDING.get();

        usePracticalSize = USE_PRACTICAL_SIZE.get();
        scaleLimitRider = SCALE_LIMIT_RIDE.get();
        tankyBeegs = BEEGS_ARE_TANKY.get();
        strongBeegs = BEEGS_ARE_STRONG.get();
        delicateTinies = TINIES_ARE_DELICATE.get();
        strongestBeeg = SIZE_DAMAGE_LIMIT.get();
        hungryBeegs = BEEGS_ARE_HUNGRY.get();
        fearThreshold = FEAR_THRESHOLD.get();
        beegInventoryPower = BEEG_INVENTORY_POWER.get();

        foodDuration = BEEGS_ARE_HUNGRY_DURATION.get();
        foodFrequency = BEEGS_ARE_HUNGRY_FREQUENCY.get();

        enableFreeSize = FREE_SIZE.get();
        beegSize = BEEG_SIZE.get();
        tinySize = TINY_SIZE.get();

        largestCranker = LARGEST_CRANKER.get();
        tiniestCranker = TINIEST_CRANKER.get();
        waterwheelCranker = WATER_WHEEL_CRANKER.get();
        kiraScalings = KIRA_SCALINGS.get();

        enableCrushingDamage = ENABLE_CRUSHING_DAMAGE.get();
        crushingThreshold = CRUSHING_THRESHOLD.get();
        crushingDamageMultiplier = CRUSHING_DAMAGE_MULTIPLIER.get();
        crushingDamageLimit = CRUSHING_DAMAGE_LIMIT.get();
        crushingSneakPrevents = CRUSHING_SNEAK_PREVENTS.get();
    }
    //endregion
}
