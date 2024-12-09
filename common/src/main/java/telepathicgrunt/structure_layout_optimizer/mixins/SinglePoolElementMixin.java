package telepathicgrunt.structure_layout_optimizer.mixins;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.utils.GeneralUtils;

import java.util.List;

@Mixin(value = SinglePoolElement.class)
public class SinglePoolElementMixin {

    @Redirect(
            method = "getShuffledJigsawBlocks(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/util/RandomSource;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;shuffle(Ljava/util/List;Lnet/minecraft/util/RandomSource;)V"))
    private void structureLayoutOptimizer$fasterJigsawListShuffling1(List<StructureTemplate.JigsawBlockInfo> list, RandomSource randomSource) {
        GeneralUtils.shuffleAndPrioritize(list, randomSource);
    }

    @Redirect(
            method = "getShuffledJigsawBlocks(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/util/RandomSource;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/pools/SinglePoolElement;sortBySelectionPriority(Ljava/util/List;)V"))
    private void structureLayoutOptimizer$fasterJigsawListShuffling2(List<StructureTemplate.JigsawBlockInfo> structureBlockInfos) {}
}

