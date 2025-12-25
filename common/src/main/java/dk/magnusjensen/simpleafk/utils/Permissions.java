package dk.magnusjensen.simpleafk.utils;

import dk.magnusjensen.simpleafk.Constants;
import net.minecraft.resources.ResourceLocation;

public class Permissions {
    public static final ResourceLocation TOGGLE = new ResourceLocation(Constants.MOD_ID, "toggle");
    public static final ResourceLocation TOGGLE_OTHER = new ResourceLocation(Constants.MOD_ID, "toggle.other");
    public static final ResourceLocation BYPASS_AFK = new ResourceLocation(Constants.MOD_ID, "bypass.afk");
    public static final ResourceLocation BYPASS_SLEEP = new ResourceLocation(Constants.MOD_ID, "bypass.sleep");
    public static final ResourceLocation MODIFY_BYPASS = new ResourceLocation(Constants.MOD_ID, "bypass.modify");
}
