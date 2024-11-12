package telepathicgrunt.structure_layout_optimizer.fabric.entrypoints;

import net.fabricmc.api.ModInitializer;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        StructureLayoutOptimizerMod.init();
    }
}
