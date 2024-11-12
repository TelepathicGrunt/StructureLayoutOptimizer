package telepathicgrunt.structure_layout_optimizer.fabric.entrypoints;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import telepathicgrunt.structure_layout_optimizer.SloConfig;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

public class Modmenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, null, StructureLayoutOptimizerMod.CONFIGURATOR.getConfig(SloConfig.class));
    }
}
