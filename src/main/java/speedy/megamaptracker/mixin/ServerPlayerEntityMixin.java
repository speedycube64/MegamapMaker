package speedy.megamaptracker.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import speedy.megamaptracker.MegamapTracker;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntity {

    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    ServerPlayerEntity _this = (ServerPlayerEntity) (Object) (this);

    String saveFolder = this.world.getSaveHandler().getWorldFolder().toString();

    private static final Logger LOGGER = LogManager.getLogger("MegamapTracker");

    @Inject(method = "tick", at = @At("TAIL"))
    private void getPositionEveryTick(CallbackInfo ci) {

        MegamapTracker.fillUpBuffer(saveFolder, _this);
    }
}
