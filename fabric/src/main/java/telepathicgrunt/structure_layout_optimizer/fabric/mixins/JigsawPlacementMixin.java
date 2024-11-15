package telepathicgrunt.structure_layout_optimizer.fabric.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.TrojanVoxelShape;

@Mixin(JigsawPlacement.class)
public class JigsawPlacementMixin {

    @WrapOperation(method = "addPieces(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/levelgen/feature/configurations/JigsawConfiguration;Lnet/minecraft/world/level/levelgen/feature/structures/JigsawPlacement$PieceFactory;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Lnet/minecraft/core/BlockPos;Ljava/util/List;Ljava/util/Random;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;join(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape shape1, VoxelShape shape2, BooleanOp function, Operation<VoxelShape> original, @Local(ordinal = 0) AABB aabb, @Local(ordinal = 0) BoundingBox boundingbox) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(aabb));
        trojanVoxelShape.boxOctree.addBox(AABB.of(boundingbox));
        return trojanVoxelShape;
    }
}
