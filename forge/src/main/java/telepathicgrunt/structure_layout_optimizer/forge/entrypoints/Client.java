package telepathicgrunt.structure_layout_optimizer.forge.entrypoints;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import telepathicgrunt.structure_layout_optimizer.SloConfig;

public class Client {

    public static void init(ModContainer modContainer) {
        modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> AutoConfig.getConfigScreen(SloConfig.class, parent).get()));
    }
}
