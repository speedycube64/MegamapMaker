package speedy.megamaptracker;

import net.fabricmc.api.ModInitializer;

public class MegamapEntry implements ModInitializer {

    @Override
    public void onInitialize() {
        // make a static instance of the MegamapTracker
        MegamapTracker.tracker = new MegamapTracker();
    }

}
