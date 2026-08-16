package actually.portals.ActuallySize.controlling.input;

import actually.portals.ActuallySize.ASIUtilities;
import actually.portals.ActuallySize.ActuallyServerConfig;
import actually.portals.ActuallySize.ActuallySizeInteractions;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers the {@code /asize} command, letting players change their
 * own scale on the fly in-game, and letting operators change (or reset)
 * the scale of any other entity, without needing to wait for a respawn
 * like the "Preferred Size" system does.
 *
 * @author evanbones
 * @since 1.0.0
 */
@Mod.EventBusSubscriber(modid = ActuallySizeInteractions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ASISizeCommand {

    /**
     * The smallest scale obtainable through this command, matching
     * the clamp used by the "Preferred Size" free-size system.
     *
     * @since 1.0.0
     */
    private static final double MIN_SCALE = 0.02;

    /**
     * The largest scale obtainable through this command, matching
     * the clamp used by the "Preferred Size" free-size system.
     *
     * @since 1.0.0
     */
    private static final double MAX_SCALE = 50;

    @SubscribeEvent
    public static void OnRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("asize")

                // /asize <scale>
                .then(Commands.argument("scale", DoubleArgumentType.doubleArg(MIN_SCALE, MAX_SCALE))
                        .requires(source -> source.hasPermission(2) || ActuallyServerConfig.enableFreeSize)
                        .executes(ctx -> setSelf(ctx.getSource().getPlayerOrException(), DoubleArgumentType.getDouble(ctx, "scale"))))

                // /asize reset
                .then(Commands.literal("reset")
                        .requires(source -> source.hasPermission(2) || ActuallyServerConfig.enableFreeSize)
                        .executes(ctx -> setSelf(ctx.getSource().getPlayerOrException(), 1)))

                // /asize get
                .then(Commands.literal("get")
                        .executes(ctx -> get(ctx.getSource().getPlayerOrException())))

                // /asize player <target> <scale|reset>
                .then(Commands.literal("player")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("scale", DoubleArgumentType.doubleArg(MIN_SCALE, MAX_SCALE))
                                        .executes(ctx -> setOther(ctx.getSource().getPlayerOrException(), EntityArgument.getPlayer(ctx, "target"), DoubleArgumentType.getDouble(ctx, "scale"))))
                                .then(Commands.literal("reset")
                                        .executes(ctx -> setOther(ctx.getSource().getPlayerOrException(), EntityArgument.getPlayer(ctx, "target"), 1)))
                                .then(Commands.literal("get")
                                        .executes(ctx -> get(EntityArgument.getPlayer(ctx, "target"))))))
        );
    }

    /**
     * @param who   The player resizing themselves
     * @param scale The scale to become
     * @author evanbones
     * @since 1.0.0
     */
    private static int setSelf(ServerPlayer who, double scale) throws CommandSyntaxException {
        ASIUtilities.setEntityScale(who, scale);
        who.sendSystemMessage(Component.literal("You are now " + format(scale) + "x your normal size."));
        return (int) Math.ceil(scale);
    }

    /**
     * @param source The operator issuing the resize
     * @param target The player being resized
     * @param scale  The scale the target will become
     * @author evanbones
     * @since 1.0.0
     */
    private static int setOther(ServerPlayer source, ServerPlayer target, double scale) {
        ASIUtilities.setEntityScale(target, scale);

        String message = target == source
                ? "You are now " + format(scale) + "x your normal size."
                : target.getGameProfile().getName() + " is now " + format(scale) + "x their normal size.";

        target.sendSystemMessage(Component.literal(target == source ? message : "You have been resized to " + format(scale) + "x by " + source.getGameProfile().getName() + "."));
        if (target != source) {
            source.sendSystemMessage(Component.literal(message));
        }
        return (int) Math.ceil(scale);
    }

    /**
     * @param who The player to report the current scale of
     * @author evanbones
     * @since 1.0.0
     */
    private static int get(ServerPlayer who) {
        double scale = ASIUtilities.getEntityScale(who, false);
        who.sendSystemMessage(Component.literal(who.getGameProfile().getName() + " is currently " + format(scale) + "x their normal size."));
        return (int) Math.ceil(scale);
    }

    /**
     * @param scale The scale to format
     * @return A tidy, human-readable representation of this scale
     * @author evanbones
     * @since 1.0.0
     */
    private static String format(double scale) {
        return Math.round(scale * 100) / 100.0 + "";
    }
}
