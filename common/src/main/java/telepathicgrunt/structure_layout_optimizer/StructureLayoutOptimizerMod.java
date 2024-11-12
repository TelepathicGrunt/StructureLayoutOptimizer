package telepathicgrunt.structure_layout_optimizer;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;

public class StructureLayoutOptimizerMod {

    public static final String MODID = "structure_layout_optimizer";
    public static final Configurator CONFIGURATOR = new Configurator(MODID);

    public static void init() {
        CONFIGURATOR.register(SloConfig.class);
    }

}
