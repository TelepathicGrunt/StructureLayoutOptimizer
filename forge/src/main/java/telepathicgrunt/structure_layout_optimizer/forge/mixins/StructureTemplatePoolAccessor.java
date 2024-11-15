package telepathicgrunt.structure_layout_optimizer.forge.mixins;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(JigsawPattern.class)
public interface StructureTemplatePoolAccessor {
    @Accessor("rawTemplates")
    List<Pair<JigsawPiece, Integer>> getRawTemplates();
}
