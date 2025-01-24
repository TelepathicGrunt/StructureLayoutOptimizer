package telepathicgrunt.structure_layout_optimizer.forge.entrypoints;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;
import telepathicgrunt.structure_layout_optimizer.configs.SLOConfig;

@Mod(StructureLayoutOptimizerMod.MODID)
public class Main {

    public Main(FMLJavaModLoadingContext context) {
        StructureLayoutOptimizerMod.init();
        SLOConfig.configStartup(FMLPaths.CONFIGDIR.get());
    }
}
