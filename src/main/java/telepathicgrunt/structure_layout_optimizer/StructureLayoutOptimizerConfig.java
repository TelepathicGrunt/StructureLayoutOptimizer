package telepathicgrunt.structure_layout_optimizer;

import net.neoforged.neoforge.common.ModConfigSpec;

public class StructureLayoutOptimizerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEDUPLICATE_SHUFFLED_TEMPLATE_POOL_ELEMENT_LIST = BUILDER
            .comment("""
                    --------------------------------------------------------------------------------------------------
                    Whether to use an alternative strategy to make structure layouts generate slightly even faster than
                    the default optimization this mod has for template pool weights. This alternative strategy works by
                    changing the list of pieces that structures collect from the template pool to not have duplicate entries.
                    
                    This will not break the structure generation, but it will make the structure layout different than
                    if this config was off (breaking vanilla seed parity). The cost of speed may be worth it in large
                    modpacks where many structure mods are using very high weight values in their template pools.
                    
                    Pros: Get a bit more performance from high weight Template Pool Structures.
                    Cons: Loses parity with vanilla seeds on the layout of the structure. (Structure layout is not broken, just different)
                    """)
            .define("deduplicateShuffledTemplatePoolElementList", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
