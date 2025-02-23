package net.fabricmc.megamaptracker.mixin;

import net.fabricmc.megamaptracker.MegamapTracker;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public class MinecraftMixin {

    private MegamapTracker megamapTracker;

    // initialize the MegamapTracker when initializing the player
    @Inject(at = @At("TAIL"), method = "<init>")
    private void onInit(CallbackInfo info) {
        megamapTracker = new MegamapTracker();
    }

    // on each tick, call the fillUpBuffer method
    @Inject(at = @At("TAIL"), method = "tick")
    private void fillBuffer(CallbackInfo info) {
        MinecraftClient client = (MinecraftClient)(Object)this;

        String saveFolder = client.getServer().getWorld().getSaveHandler().getWorldFolder().getPath();

        long time = System.currentTimeMillis();
        megamapTracker.fillUpBuffer(saveFolder, time, client.player);
    }
}
