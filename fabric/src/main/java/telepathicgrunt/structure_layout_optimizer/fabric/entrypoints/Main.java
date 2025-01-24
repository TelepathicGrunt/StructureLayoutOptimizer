package telepathicgrunt.structure_layout_optimizer.fabric.entrypoints;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;
import telepathicgrunt.structure_layout_optimizer.configs.SLOConfig;

import java.nio.file.Path;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        StructureLayoutOptimizerMod.init();
        SLOConfig.configStartup(FabricLoader.getInstance().getConfigDir());
    }
}
