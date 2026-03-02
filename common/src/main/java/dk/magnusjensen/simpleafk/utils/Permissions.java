package dk.magnusjensen.simpleafk.utils;

import dk.magnusjensen.simpleafk.Constants;
import net.minecraft.resources.Identifier;

public class Permissions {
    public static final Identifier TOGGLE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "toggle");
    public static final Identifier TOGGLE_OTHER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "toggle.other");
    public static final Identifier BYPASS_AFK = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bypass.afk");
    public static final Identifier BYPASS_SLEEP = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bypass.sleep");
    public static final Identifier MODIFY_BYPASS = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bypass.modify");
}
