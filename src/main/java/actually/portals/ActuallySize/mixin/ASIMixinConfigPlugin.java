package actually.portals.ActuallySize.mixin;

import actually.portals.ActuallySize.compatibilities.create.ASICreateCompatibility;
import actually.portals.ActuallySize.compatibilities.figura.ASIFiguraCompatibility;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import virtuoel.pehkui.util.VersionUtils;

import java.util.List;
import java.util.Set;

/**
 * Some mixins are applied only in some versions of minecraft.
 * This configuration class tells Mixin when to NOT apply the
 * wrong ones basically.
 *
 * @author Actually Portals
 * @since 1.0.0
 */
public class ASIMixinConfigPlugin implements IMixinConfigPlugin {

    /**
     * Checks that the mixin package being evaluated is the expected one
     *
     * @since 1.0.0
     */
    private static final String MIXIN_PACKAGE = "actually.portals.ActuallySize.mixin";
    /**
     * I don't want to be string splitting multiple times, once is enough
     */
    static int[] FORGE_VERSION = null;

    /**
     * Checks the package name of this mixin to see if it is a third party compat type
     *
     * @author Actually Portals
     * @since 1.0.0
     */
    public static boolean isThirdPartyCompatibilityMixin(@NotNull String mixinClassName) {
        return mixinClassName.contains(".third.");
    }

    /**
     * Checks the package name of this mixin to see if applying it
     *
     * @author Actually Portals
     * @since 1.0.0
     */
    public static boolean shouldApplyThirdParty(@NotNull String mixinClassName) {
        boolean wasCompatibilityFound = false;

        // Check for CREATE
        boolean isCreate = mixinClassName.contains(".create.");
        if (isCreate) {

            try {
                wasCompatibilityFound = ASICreateCompatibility.TestIfCreatePresent();
                wasCompatibilityFound = true;
            } catch (Error ignored) {
            }
        }

        boolean isFigura = mixinClassName.contains(".figura.");
        if (isFigura) {

            try {
                wasCompatibilityFound = ASIFiguraCompatibility.TestIfFiguraPresent();
                wasCompatibilityFound = true;
            } catch (Error ignored) {
            }
        }

        return wasCompatibilityFound;
    }

    /**
     * Checks the package name of this mixin to decide if it is good to be applied
     *
     * @author Actually Portals
     * @since 1.0.0
     */
    public static boolean shouldApplyCompatibilityMixin(@NotNull String mixinClassName) {

        /*
         * For now, it is fine to piggyback off Pehkui as it is the only
         * supported size-manager mod, but I suppose I will have to write
         * my own if another size-managing mod is introduced.
         *
         * This checks only for MINECRAFT VERSION though
         */
        if (!VersionUtils.shouldApplyCompatibilityMixin(mixinClassName)) {
            return false;
        }

        /*
         * Check for FORGE VERSION
         */
        String forgeVersion = extractAfter(mixinClassName, ".forge", false);
        if (forgeVersion != null) {
            Integer major = null;
            Integer minor = null;
            Integer patch = null;

            // Read forge version of this package
            if (forgeVersion.length() >= 7) {
                major = integerParse(forgeVersion.substring(0, 3));
                minor = integerParse(forgeVersion.substring(3, 5));
                patch = integerParse(forgeVersion.substring(5, 7));
            }

            // Flexibility for compatibility range
            boolean isPlus = false;
            boolean isMinus = false;
            if (forgeVersion.length() > 7) {
                String range = extractUntil(forgeVersion.substring(7), ".", true);
                isPlus = "plus".equals(range);
                isMinus = "minus".equals(range);
            }

            // Apply settings if valid name
            if (minor != null && patch != null && major != null) {
                int MF = getForgeVersion()[0];
                int mF = getForgeVersion()[1];
                int pF = getForgeVersion()[2];

                // Up to this version inclusive
                if (isMinus) {
                    return MF < major || (MF == major && (mF < minor || (mF == minor && pF <= patch)));

                    // From this version onward
                } else if (isPlus) {
                    return MF > major || (MF == major && (mF > minor || (mF == minor && pF >= patch)));

                    // Forge must exactly
                } else {
                    return MF == major && mF == minor && pF == patch;
                }

                // Notify the incorrectness of this
            } else {

                System.out.println("ASI MIXIN [" + mixinClassName + "] INVALID FORGE VERSION '" + forgeVersion + "' = [" + major + "][" + minor + "][" + patch + "] - DENIED");
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @author Actually Portals
     * @since 1.0.0
     */
    private static String extractAfter(String source, String delimiter, boolean wholeIfAbsent) {
        if (source == null) {
            return null;
        }
        if (delimiter == null) {
            return wholeIfAbsent ? source : null;
        }

        int index = source.indexOf(delimiter);
        if (index < 0) {
            return wholeIfAbsent ? source : null;
        }

        return source.substring(index + delimiter.length());
    }

    /**
     * @author Actually Portals
     * @see #extractAfter(String, String, boolean)
     * @since 1.0.0
     */
    private static String extractUntil(String source, String delimiter, boolean wholeIfAbsent) {
        if (source == null) {
            return null;
        }
        if (delimiter == null) {
            return wholeIfAbsent ? source : null;
        }

        int index = source.indexOf(delimiter);
        if (index < 0) {
            return wholeIfAbsent ? source : null;
        }

        return source.substring(0, index);
    }

    /**
     * Parses an integer, returning <code>null</code> instead of throwing.
     *
     * @author Actually Portals
     * @see #extractAfter(String, String, boolean)
     * @since 1.0.0
     */
    private static Integer integerParse(String source) {
        if (source == null) {
            return null;
        }

        try {
            return Integer.parseInt(removeDecimalZeros(source));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Trims the trailing zeros off the decimal part, if there is one.
     *
     * @author Actually Portals
     * @see #integerParse(String)
     * @since 1.0.0
     */
    private static String removeDecimalZeros(String source) {
        if (!source.contains(".")) {
            return source;
        }

        int dot = source.lastIndexOf(".");
        String decimals = source.substring(dot);

        // Last index that is not a zero, relative to the dot
        int lastSignificant = -1;
        for (int c = 1; c < decimals.length(); c++) {
            if (decimals.charAt(c) != '0') {
                lastSignificant = c;
            }
        }

        return source.substring(0, dot + lastSignificant + 1);
    }

    /**
     * Returns the Forge Version, parsed into actual numbers
     *
     * @author Actually Portals
     * @since 1.0.0
     */
    public static int[] getForgeVersion() {

        // Parse and read forge version the first time this is called
        if (FORGE_VERSION == null) {

            // Read Forge Version
            FORGE_VERSION = new int[3];
            String[] version = ForgeVersion.getVersion().split("\\.");

            // Parse
            Integer major = integerParse(version[0]);
            Integer minor = integerParse(version[1]);
            Integer patch = integerParse(version[2]);

            // Parse and fill
            FORGE_VERSION[0] = major == null ? -1 : major;
            FORGE_VERSION[1] = minor == null ? -1 : minor;
            FORGE_VERSION[2] = patch == null ? -1 : patch;
        }

        return FORGE_VERSION;
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public void onLoad(String mixinPackage) {

        /*
         * Thank you Pehkui for this example :valorpray:
         */
        if (!mixinPackage.startsWith(MIXIN_PACKAGE)) {
            throw new IllegalArgumentException(
                    String.format("Invalid package: Expected \"%s\", but found \"%s\".", MIXIN_PACKAGE, mixinPackage)
            );
        }
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        // Sanity check for package apparently
        if (!mixinClassName.startsWith(MIXIN_PACKAGE)) {
            throw new IllegalArgumentException(
                    String.format("Invalid package for class \"%s\": Expected \"%s\", but found \"%s\".", targetClassName, MIXIN_PACKAGE, mixinClassName)
            );
        }

        // Redirect to checking for third-parties
        if (isThirdPartyCompatibilityMixin(mixinClassName)) {
            return shouldApplyThirdParty(mixinClassName);
        }

        // Check if it should be applied
        return shouldApplyCompatibilityMixin(mixinClassName);
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public List<String> getMixins() {
        return null;
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    /**
     * @author Actually Portals
     * @since 1.0.0
     */
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
