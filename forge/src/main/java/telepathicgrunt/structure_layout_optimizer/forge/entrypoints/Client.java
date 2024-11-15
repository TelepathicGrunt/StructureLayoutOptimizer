package telepathicgrunt.structure_layout_optimizer.forge.entrypoints;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import telepathicgrunt.structure_layout_optimizer.SloConfig;

public class Client {

    public static void init(ModLoadingContext context) {
        context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, parent) -> AutoConfig.getConfigScreen(SloConfig.class, parent).get());
    }
}
