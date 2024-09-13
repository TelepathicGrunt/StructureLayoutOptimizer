package telepathicgrunt.structure_layout_optimizer;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(StructureLayoutOptimizerMod.MODID)
public class StructureLayoutOptimizerMod {
    public static final String MODID = "structure_layout_optimizer";

    public StructureLayoutOptimizerMod(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, StructureLayoutOptimizerConfig.SPEC);
    }
}
