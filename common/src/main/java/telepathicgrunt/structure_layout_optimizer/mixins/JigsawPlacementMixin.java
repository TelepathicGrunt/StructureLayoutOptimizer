package telepathicgrunt.structure_layout_optimizer.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import telepathicgrunt.structure_layout_optimizer.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.utils.TrojanVoxelShape;

@Mixin(value = JigsawPlacement.class)
public class JigsawPlacementMixin {

    // need the intermediary name of the lambda method because fabric remaps those and uses intermediary even in dev
    @WrapOperation(method = {"lambda$addPieces$0", "method_39824"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;join(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"), require = 1)
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape shape1, VoxelShape shape2, BooleanOp function, Operation<VoxelShape> original, @Local(ordinal = 0) AABB aabb, @Local(ordinal = 0, argsOnly = true) BoundingBox boundingbox) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(aabb));
        trojanVoxelShape.boxOctree.addBox(AABB.of(boundingbox));
        return trojanVoxelShape;
    }
}
