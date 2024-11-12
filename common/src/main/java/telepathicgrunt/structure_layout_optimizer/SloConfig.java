package telepathicgrunt.structure_layout_optimizer;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;

@ConfigInfo(
        title = "Structure Layout Optimizer",
        titleTranslation = "config.structure_layout_optimizer.title",

        description = "An attempt at optimizing jigsaw generation",
        descriptionTranslation = "config.structure_layout_optimizer.desc",

        icon = "layout-template",
        links = {
                @ConfigInfo.Link(text = "Curseforge", value = "https://www.curseforge.com/projects/1087831", icon = "curseforge"),
                @ConfigInfo.Link(text = "Modrinth", value = "https://modrinth.com/mod/ayPU0OHc", icon = "modrinth"),
                @ConfigInfo.Link(text = "Report a Bug", textTranslation = "config.structure_layout_optimizer.link.bug_report", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer/issues", icon = "bug"),
                @ConfigInfo.Link(text = "GitHub", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer", icon = "github"),
                @ConfigInfo.Link(text = "Discord", value = "https://discord.gg/K8qRev3yKZ", icon = "gamepad-2"),
                @ConfigInfo.Link(text = "License", textTranslation = "config.structure_layout_optimizer.link.license", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer/blob/HEAD/LICENSE.txt", icon = "copyright"),
        }
)
@Config(StructureLayoutOptimizerMod.MODID)
public class SloConfig {

    @Comment(value = """
            Whether to use an alternative strategy to make structure layouts generate slightly even faster than
            the default optimization this mod has for template pool weights. This alternative strategy works by
            changing the list of pieces that structures collect from the template pool to not have duplicate entries.
            
            This will not break the structure generation, but it will make the structure layout different than
            if this config was off (breaking vanilla seed parity). The cost of speed may be worth it in large
            modpacks where many structure mods are using very high weight values in their template pools.
            
            Pros: Get a bit more performance from high weight Template Pool Structures.
            Cons: Loses parity with vanilla seeds on the layout of the structure. (Structure layout is not broken, just different)
            """, translation = "config.structure_layout_optimizer.deduplicate_shuffled_template_pool_element_list.desc")
    @ConfigEntry(id = "deduplicateShuffledTemplatePoolElementList", translation = "config.structure_layout_optimizer.deduplicate_shuffled_template_pool_element_list")
    public static boolean deduplicateShuffledTemplatePoolElementList = false;
}
