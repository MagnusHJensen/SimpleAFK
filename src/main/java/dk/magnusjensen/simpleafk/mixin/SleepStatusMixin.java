/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.mixin;

import dk.magnusjensen.simpleafk.AFKManager;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {

    @Shadow private int activePlayers;

    @Inject(method = "sleepersNeeded", at = @At("HEAD"), cancellable = true)
    private void onSleepersNeeded(int requiredSleepPercentage, CallbackInfoReturnable<Integer> cir) {
        int actualActivePlayers = this.activePlayers - AFKManager.getInstance().getSleepBypassCount();
        cir.setReturnValue(Math.max(1, Mth.ceil((float)(actualActivePlayers * requiredSleepPercentage) / 100.0F)));
    }
}
