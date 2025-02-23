package net.fabricmc.megamaptracker.mixin;

import net.fabricmc.megamaptracker.IMegamapTracker;
import net.fabricmc.megamaptracker.MegamapTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WorldSaveHandler.class)
public class SaveHandlerMixin {

    // When player data is saved (such as when the game is paused), also flush the MegamapTracker buffers
    @Inject(at = @At("TAIL"), method = "savePlayerData")

    private void flushOnSave(PlayerEntity player, CallbackInfo info) {

        if (player instanceof IMegamapTracker) {

            WorldSaveHandler handler = (WorldSaveHandler) (Object) this;

            MegamapTracker megamapTracker = ((IMegamapTracker) player).getMegamapTracker();

            megamapTracker.flushToDisk(handler.getWorldFolder().toString());

        }

    }
}
