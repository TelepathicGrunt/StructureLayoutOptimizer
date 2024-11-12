package telepathicgrunt.structure_layout_optimizer.neoforge.entrypoints;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import telepathicgrunt.structure_layout_optimizer.SloConfig;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

public class Client {

    public static void init(ModContainer modContainer) {
        modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new ConfigScreen(parent, null, StructureLayoutOptimizerMod.CONFIGURATOR.getConfig(SloConfig.class))));
    }
}
