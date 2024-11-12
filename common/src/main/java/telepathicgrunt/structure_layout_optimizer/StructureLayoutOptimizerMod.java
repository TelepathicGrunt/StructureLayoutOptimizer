package telepathicgrunt.structure_layout_optimizer;


import com.teamresourceful.resourcefulconfig.common.config.Configurator;

public class StructureLayoutOptimizerMod {

    public static final String MODID = "structure_layout_optimizer";
    public static final Configurator CONFIGURATOR = new Configurator();

    public static void init() {
        CONFIGURATOR.registerConfig(SloConfig.class);
    }

}
