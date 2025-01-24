package telepathicgrunt.structure_layout_optimizer.fabric.entrypoints;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import telepathicgrunt.structure_layout_optimizer.SloConfig;

public class Modmenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(SloConfig.class, parent).get();
    }
}
