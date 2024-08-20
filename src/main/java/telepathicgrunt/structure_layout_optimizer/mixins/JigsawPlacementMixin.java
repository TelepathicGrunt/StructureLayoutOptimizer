package telepathicgrunt.structure_layout_optimizer.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.utils.TrojanVoxelShape;

@Mixin(value = JigsawPlacement.class)
public class JigsawPlacementMixin {

    @Redirect(
            method = {
                "lambda$addPieces$2"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;join(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"),
            remap = false)
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape shape1, VoxelShape shape2, BooleanOp function, @Local(ordinal = 0) AABB aabb) {
        return new TrojanVoxelShape(new BoxOctree(aabb));
    }
}

