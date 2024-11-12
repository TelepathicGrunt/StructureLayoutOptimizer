package telepathicgrunt.structure_layout_optimizer.neoforge.entrypoints;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

@Mod(StructureLayoutOptimizerMod.MODID)
public class Main {

    public Main(ModContainer modContainer) {
        StructureLayoutOptimizerMod.init();

        if(FMLEnvironment.dist.isClient()) {
            Client.init(modContainer);
        }
    }
}
