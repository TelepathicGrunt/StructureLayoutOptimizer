package telepathicgrunt.structure_layout_optimizer.forge.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.forge.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.forge.utils.TrojanVoxelShape;

@Mixin(JigsawManager.class)
public class JigsawPlacementMixin {


    @Redirect(method = "addPieces(Lnet/minecraft/util/registry/DynamicRegistries;Lnet/minecraft/world/gen/feature/structure/VillageConfig;Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/util/math/BlockPos;Ljava/util/List;Ljava/util/Random;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;join(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/IBooleanFunction;)Lnet/minecraft/util/math/shapes/VoxelShape;"))
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape pShape1, VoxelShape pShape2, IBooleanFunction pFunction, @Local(ordinal = 0) AxisAlignedBB aabb, @Local(ordinal = 0) MutableBoundingBox boundingbox) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(aabb));
        trojanVoxelShape.boxOctree.addBox(AxisAlignedBB.of(boundingbox));
        return trojanVoxelShape;
    }
}
