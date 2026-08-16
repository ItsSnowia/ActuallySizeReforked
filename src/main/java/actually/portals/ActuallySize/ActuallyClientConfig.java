package actually.portals.ActuallySize;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The configuration of what the client's preferences are
 *
 * @since 1.0.0
 * @author Actually Portals
 */
@Mod.EventBusSubscriber(modid = ActuallySizeInteractions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ActuallyClientConfig {

    //region Config Parameters

    /**
     * If this player prefers to be beeg
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue PREFERRED_BEEG;

    /**
     * If this player prefers to be smol
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue PREFERRED_TINY;

    /**
     * If this player prefers to be smol
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.DoubleValue PREFERRED_SCALE;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HEAD_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> CHEST_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> LEGS_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> FEET_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> MAINHAND_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> OFFHAND_HOLD_POINT;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> CURSOR_HOLD_POINT;

    /**
     * Honestly holding animals in the same slots you hold players just feels off sometimes (real).
     *
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.BooleanValue ONLY_PLAYERS;

    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR1_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR2_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR3_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR4_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR5_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR6_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR7_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR8_HOLD_POINT;
    /**
     * @since 1.0.0
     */
    @NotNull private static final ForgeConfigSpec.ConfigValue<String> HOTBAR9_HOLD_POINT;

    /**
     * The config builder itself
     *
     * @since 1.0.0
     */
    @NotNull static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        //region Preferences
        CONFIG_BUILDER.push("preferences");

        PREFERRED_BEEG = CONFIG_BUILDER.comment("§eYou will spawn giant")
                .comment("You will spawn beeg by default without having to do anything else. Also affects the size at which you respawn. May be disabled by the server. ")
                .define("preferablyBeeg", false);

        PREFERRED_TINY = CONFIG_BUILDER.comment("§eYou will spawn tiny")
                .comment("You will spawn tiny by default without having to do anything else. Also affects the size at which you respawn. May be disabled by the server. ")
                .define("preferablySmol", false);

        PREFERRED_SCALE = CONFIG_BUILDER.comment("§eYou will spawn this size")
                .comment("For servers that allow you to freely choose your size, what scale do you want to be by default? Also affects the size at which you respawn. Set to '1' to disable this feature. The server has final say and will clamp this to its own maximum allowed size.")
                .defineInRange("preferredScale", 1, 0.05, 1000);

        CONFIG_BUILDER.pop();
        //endregion

        //region Holding
        CONFIG_BUILDER.push("holding");

        HEAD_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your head slot, where does it show up on your player?")
                .define("headHold", "actuallysize:hat");

        CHEST_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your chestplate slot, where does it show up on your player?")
                .define("chestHold", "actuallysize:chest_pocket");

        LEGS_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your leggings slot, where does it show up on your player? ")
                .define("legsHold", "actuallysize:hoodie_pocket");

        FEET_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your boots slot, where does it show up on your player? ")
                .define("bootsHold", "actuallysize:left_boot");

        MAINHAND_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your main hand, where does it show up on your player? ")
                .define("mainhandHold", "actuallysize:right_hand");

        OFFHAND_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When holding an entity in your offhand, where does it show up on your player? ")
                .define("offhandHold", "actuallysize:left_fist");

        CONFIG_BUILDER.push("hotbar");
        HOTBAR1_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the first, leftmost, hotbar slot.")
                .define("hotbarHold1", "actuallysize:nomf");

        HOTBAR2_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the second hotbar slot.")
                .define("hotbarHold2", "actuallysize:right_shoulder");

        HOTBAR3_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the third hotbar slot.")
                .define("hotbarHold3", "actuallysize:left_thigh");

        HOTBAR4_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the fourth hotbar slot.")
                .define("hotbarHold4", "actuallysize:right_pocket");

        HOTBAR5_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the central hotbar slot.")
                .define("hotbarHold5", "actuallysize:right_thigh");

        HOTBAR6_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the sixth hotbar slot.")
                .define("hotbarHold6", "actuallysize:left_shoulder");

        HOTBAR7_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the seventh hotbar slot.")
                .define("hotbarHold7", "actuallysize:left_pocket");

        HOTBAR8_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the eight hotbar slot.")
                .define("hotbarHold8", "actuallysize:right_boot");

        HOTBAR9_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("When their item is in the last, rightmost, hotbar slot.")
                .define("hotbarHold9", "actuallysize:head");
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("advanced");
        CURSOR_HOLD_POINT = CONFIG_BUILDER.comment("§eWhere to put tinies?")
                .comment("While you move around an entity in your inventory, where does it show up on your player? ")
                .define("cursorHold", "actuallysize:pinch");

        ONLY_PLAYERS = CONFIG_BUILDER.comment("§eAnimals and players are held differently")
                .comment("When this is enabled, these custom hold points will only apply when holding other players. All other mobs will be held in the default slots while this is enabled.")
                .define("onlyForPlayers", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.pop();
        //endregion

        SPEC = CONFIG_BUILDER.build();
    }
    //endregion

    //region Config Object
    /**
     * The hold points where this player has specified they want to hold tinies in
     *
     * @since 1.0.0
     */
    @Nullable public static ResourceLocation holdHead, holdChest, holdLegs, holdFeet, holdMainhand, holdOffhand, holdCursor;
    @Nullable public static final ResourceLocation[] holdHotbar = new ResourceLocation[9];

    /**
     * The size preferences of this player
     *
     * @since 1.0.0
     */
    public static boolean isPreferBeeg, isPreferTiny;

    /**
     * The size preference of this player
     *
     * @since 1.0.0
     */
    public static double preferredScale;

    /**
     * If the special hold points are only used when holding players
     *
     * @since 1.0.0
     */
    public static boolean onlySpecialHoldPlayers;

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

        // Read Equipment
        holdHead = tryRead(HEAD_HOLD_POINT.get());
        holdChest = tryRead(CHEST_HOLD_POINT.get());
        holdLegs = tryRead(LEGS_HOLD_POINT.get());
        holdFeet = tryRead(FEET_HOLD_POINT.get());
        holdMainhand = tryRead(MAINHAND_HOLD_POINT.get());
        holdOffhand = tryRead(OFFHAND_HOLD_POINT.get());
        holdCursor = tryRead(CURSOR_HOLD_POINT.get());
        onlySpecialHoldPlayers = ONLY_PLAYERS.get();
        preferredScale = PREFERRED_SCALE.get();
        isPreferBeeg = PREFERRED_BEEG.get();
        isPreferTiny = PREFERRED_TINY.get();

        // Read Hotbar
        holdHotbar[0] = tryRead(HOTBAR1_HOLD_POINT.get());
        holdHotbar[1] = tryRead(HOTBAR2_HOLD_POINT.get());
        holdHotbar[2] = tryRead(HOTBAR3_HOLD_POINT.get());
        holdHotbar[3] = tryRead(HOTBAR4_HOLD_POINT.get());
        holdHotbar[4] = tryRead(HOTBAR5_HOLD_POINT.get());
        holdHotbar[5] = tryRead(HOTBAR6_HOLD_POINT.get());
        holdHotbar[6] = tryRead(HOTBAR7_HOLD_POINT.get());
        holdHotbar[7] = tryRead(HOTBAR8_HOLD_POINT.get());
        holdHotbar[8] = tryRead(HOTBAR9_HOLD_POINT.get());
    }

    /**
     * @param compound A string that encodes for a Hold Point namespace and path
     *
     * @return The namespaced key it encodes for
     *
     * @since 1.0.0
     * @author Actually Portals
     */
    public static @Nullable ResourceLocation tryRead(@NotNull String compound) {
        if (compound.isEmpty()) { return null; }
        if (!ResourceLocation.isValidResourceLocation(compound)) { return null; }
        int col = compound.indexOf(":");

        // Default to ASI Namespace
        if (col < 0) { return ResourceLocation.fromNamespaceAndPath(ActuallySizeInteractions.MODID, compound); }

        // Read from parse
        return ResourceLocation.fromNamespaceAndPath(compound.substring(0, col), compound.substring(col + 1));
    }
    //endregion
}
