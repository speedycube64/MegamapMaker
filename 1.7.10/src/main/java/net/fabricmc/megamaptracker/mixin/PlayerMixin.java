package net.fabricmc.megamaptracker.mixin;

import net.fabricmc.megamaptracker.IMegamapTracker;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.megamaptracker.MegamapTracker;

@Mixin(ServerPlayerEntity.class)
public class PlayerMixin implements IMegamapTracker {

    @Unique
    private MegamapTracker megamapTracker;

    // initialize the MegamapTracker when initializing the player
    @Inject(at = @At("TAIL"), method = "<init>")
    private void initTracker(CallbackInfo info) {
        megamapTracker = new MegamapTracker();
    }

    // on each tick, call the fillUpBuffer method
    @Inject(at = @At("TAIL"), method = "tick")
    private void fillBufferOnTick(CallbackInfo info) {

        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        String saveFolder = player.world.getSaveHandler().getWorldFolder().toString();

        long time = System.currentTimeMillis();
        megamapTracker.fillUpBuffer(saveFolder, time, player);

    }

    // allows the tracker to be accessed from other mixins
    @Override
    public MegamapTracker getMegamapTracker() {
        return megamapTracker;
    }
}
