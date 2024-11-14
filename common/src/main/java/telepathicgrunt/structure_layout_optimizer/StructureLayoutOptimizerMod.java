package telepathicgrunt.structure_layout_optimizer;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class StructureLayoutOptimizerMod {

    private static ConfigHolder<SloConfig> CONFIG;

    public static SloConfig getConfig() {
        return CONFIG.get();
    }

    public static final String MODID = "structure_layout_optimizer";

    public static void init() {
        CONFIG = AutoConfig.register(SloConfig.class, JanksonConfigSerializer::new);
    }

}
