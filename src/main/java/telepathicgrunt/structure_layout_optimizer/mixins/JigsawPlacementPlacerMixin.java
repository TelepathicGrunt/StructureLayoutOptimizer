package telepathicgrunt.structure_layout_optimizer.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.utils.GeneralUtils;
import telepathicgrunt.structure_layout_optimizer.utils.TrojanVoxelShape;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement$Placer")
public class JigsawPlacementPlacerMixin {

    @Redirect(method = "tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/JigsawBlock;canAttach(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)Z"))
    private boolean structureLayoutOptimizer$optimizeJigsawConnecting(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        return GeneralUtils.canJigsawsAttach(jigsaw1, jigsaw2);
    }

    @WrapOperation(method = "tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;joinIsNotEmpty(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Z"))
    private boolean structureLayoutOptimizer$replaceVoxelShape2(VoxelShape parentBounds, VoxelShape pieceShape, BooleanOp booleanOp, Operation<Boolean> original, @Local(ordinal = 3) BoundingBox pieceBounds) {
        if (parentBounds instanceof TrojanVoxelShape trojanVoxelShape) {
            AABB pieceAABB = AABB.of(pieceBounds).deflate(0.25D);
            // Have to inverse because of an ! outside our wrap
            return !(trojanVoxelShape.boxOctree.boundaryContains(pieceAABB) && !trojanVoxelShape.boxOctree.intersectsAnyBox(pieceAABB));
        }

        return original.call(parentBounds, pieceShape, booleanOp);
    }

    @Redirect(method = "tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 0))
    private void structureLayoutOptimizer$replaceVoxelShape3(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 0) BoundingBox pieceBounds) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(AABB.of(pieceBounds)));
        instance.setValue(trojanVoxelShape);
    }

    @Redirect(method = "tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 1))
    private void structureLayoutOptimizer$replaceVoxelShape4(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 3) BoundingBox pieceBounds) {
        if (instance.getValue() instanceof TrojanVoxelShape trojanVoxelShape) {
            trojanVoxelShape.boxOctree.addBox(AABB.of(pieceBounds));
        }
    }

    @Redirect(method = "tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;joinUnoptimized(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape structureLayoutOptimizer$turnOffOldVoxelShapeBehavior1(VoxelShape indexmerger, VoxelShape indexmerger1, BooleanOp indexmerger2) {
        return Shapes.empty();
    }
}

