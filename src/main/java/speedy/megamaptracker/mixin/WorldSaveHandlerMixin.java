package speedy.megamaptracker.mixin;

import net.minecraft.world.PlayerDataHandler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.WorldSaveHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import speedy.megamaptracker.MegamapTracker;


@Mixin(WorldSaveHandler.class)
public abstract class WorldSaveHandlerMixin implements SaveHandler, PlayerDataHandler {

    private static final Logger LOGGER = LogManager.getLogger("MegamapTracker");

    @Inject(method = "saveWorld", at = @At("HEAD"))
    private void flushOnSave(CallbackInfo ci) {
        String saveFolder = this.getWorldFolder().toString();
        MegamapTracker.flushToDisk(saveFolder);
    }
}
