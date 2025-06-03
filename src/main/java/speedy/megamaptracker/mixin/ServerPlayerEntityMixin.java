package speedy.megamaptracker.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import speedy.megamaptracker.MegamapTracker;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin{

    private static final Logger LOGGER = LogManager.getLogger("MegamapTracker");

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setupTracker(CallbackInfo ci){
        ServerPlayerEntity _this = (ServerPlayerEntity) (Object) (this);
        String saveFolder = _this.world.getServer().getSavePath(WorldSavePath.ROOT).toString();
        MegamapTracker.setSaveFolder(saveFolder);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void savePositionEveryTick(CallbackInfo ci) {
        ServerPlayerEntity _this = (ServerPlayerEntity) (Object) (this);
        MegamapTracker.fillUpBuffer(_this);
    }
}
