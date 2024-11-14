package telepathicgrunt.structure_layout_optimizer.forge.entrypoints;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

@Mod(StructureLayoutOptimizerMod.MODID)
public class Main {

    public Main() {
        StructureLayoutOptimizerMod.init();

        if(FMLEnvironment.dist.isClient()) {
            Client.init(ModLoadingContext.get());
        }
    }
}
