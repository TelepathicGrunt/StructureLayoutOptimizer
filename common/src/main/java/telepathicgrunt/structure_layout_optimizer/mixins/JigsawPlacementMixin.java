package telepathicgrunt.structure_layout_optimizer.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import telepathicgrunt.structure_layout_optimizer.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.utils.TrojanVoxelShape;

@Mixin(value = JigsawPlacement.class)
public class JigsawPlacementMixin {

    // need the intermediary name of the lambda method because fabric remaps those and uses intermediary even in dev
    @WrapOperation(method = {"lambda$addPieces$2", "method_39824(Lnet/minecraft/class_3790;IILnet/minecraft/class_5434$class_11600;ILnet/minecraft/class_5539;Lnet/minecraft/class_9778;ILnet/minecraft/class_3341;Lnet/minecraft/class_3195$class_7149;ZLnet/minecraft/class_2794;Lnet/minecraft/class_3485;Lnet/minecraft/class_2919;Lnet/minecraft/class_2378;Lnet/minecraft/class_8891;Lnet/minecraft/class_9822;Lnet/minecraft/class_6626;)V"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;join(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
            require = 1)
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape shape1, VoxelShape shape2, BooleanOp function, Operation<VoxelShape> original, @Local(ordinal = 0) AABB aabb, @Local(ordinal = 0, argsOnly = true) BoundingBox boundingbox) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(aabb));
        trojanVoxelShape.boxOctree.addBox(AABB.of(boundingbox));
        return trojanVoxelShape;
    }
}
