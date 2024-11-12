package telepathicgrunt.structure_layout_optimizer;

import com.teamresourceful.resourcefulconfig.common.annotations.Comment;
import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.web.annotations.Link;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;

@WebInfo(
        title = "Structure Layout Optimizer",
        description = "An attempt at optimizing jigsaw generation",
        icon = "layout-template",
        links = {
                @Link(title = "Curseforge", value = "https://www.curseforge.com/projects/1087831", icon = "curseforge"),
                @Link(title = "Modrinth", value = "https://modrinth.com/mod/ayPU0OHc", icon = "modrinth"),
                @Link(title = "Report a Bug", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer/issues", icon = "bug"),
                @Link(title = "GitHub", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer", icon = "github"),
                @Link(title = "Discord", value = "https://discord.gg/K8qRev3yKZ", icon = "gamepad-2"),
                @Link(title = "License", value = "https://github.com/TelepathicGrunt/StructureLayoutOptimizer/blob/HEAD/LICENSE.txt", icon = "copyright"),
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
    @ConfigEntry(type = EntryType.BOOLEAN, id = "deduplicateShuffledTemplatePoolElementList", translation = "config.structure_layout_optimizer.deduplicate_shuffled_template_pool_element_list")
    public static boolean deduplicateShuffledTemplatePoolElementList = false;
}
