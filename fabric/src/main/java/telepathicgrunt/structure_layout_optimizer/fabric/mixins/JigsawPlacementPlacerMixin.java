package telepathicgrunt.structure_layout_optimizer.fabric.mixins;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.GeneralUtils;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.TrojanArrayList;
import telepathicgrunt.structure_layout_optimizer.fabric.utils.TrojanVoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(JigsawPlacement.Placer.class)
public class JigsawPlacementPlacerMixin {

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/JigsawBlock;canAttach(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)Z"))
    private boolean structureLayoutOptimizer$optimizeJigsawConnecting(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        return GeneralUtils.canJigsawsAttach(jigsaw1, jigsaw2);
    }

    ////////////////////////////////

    @ModifyExpressionValue(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/structures/StructurePoolElement;getShuffledJigsawBlocks(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Ljava/util/Random;)Ljava/util/List;", ordinal = 1))
    private List<StructureTemplate.StructureBlockInfo> structureLayoutOptimizer$skipBlockedJigsaws(
                                                        List<StructureTemplate.StructureBlockInfo> original,
                                                        @Local(ordinal = 0, argsOnly = true) boolean useExpansionHack,
                                                        @Local(ordinal = 2) MutableObject<VoxelShape> voxelShapeMutableObject,
                                                        @Local(ordinal = 1) StructurePoolElement structurePoolElement,
                                                        @Local(ordinal = 0) StructureTemplate.StructureBlockInfo parentJigsawBlockInfo,
                                                        @Local(ordinal = 2) BlockPos parentTargetPosition)
    {
        if (voxelShapeMutableObject.getValue() instanceof TrojanVoxelShape) {
            TrojanVoxelShape trojanVoxelShape = ((TrojanVoxelShape) voxelShapeMutableObject.getValue());
            // If rigid and target position is already an invalid spot, do not run rest of logic.
            StructureTemplatePool.Projection candidatePlacementBehavior = structurePoolElement.getProjection();
            boolean isCandidateRigid = candidatePlacementBehavior == StructureTemplatePool.Projection.RIGID;
            if (isCandidateRigid && (!trojanVoxelShape.boxOctree.boundaryContains(parentTargetPosition) || trojanVoxelShape.boxOctree.withinAnyBox(parentTargetPosition))) {
                return new ArrayList<>();
            }
        }
        return original;
    }

    ////////////////////////////////

    @WrapOperation(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;joinIsNotEmpty(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Z"))
    private boolean structureLayoutOptimizer$replaceVoxelShape2(VoxelShape parentBounds, VoxelShape pieceShape, BooleanOp booleanOp, Operation<Boolean> original, @Local(ordinal = 3) BoundingBox pieceBounds) {
        if (parentBounds instanceof TrojanVoxelShape) {
            AABB pieceAABB = AABB.of(pieceBounds).deflate(0.25D);

            // Have to inverse because of an ! outside our wrap
            return !((TrojanVoxelShape) parentBounds).boxOctree.withinBoundsButNotIntersectingChildren(pieceAABB);
        }

        return original.call(parentBounds, pieceShape, booleanOp);
    }

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 0))
    private void structureLayoutOptimizer$replaceVoxelShape3(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 0) BoundingBox pieceBounds) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(AABB.of(pieceBounds)));
        instance.setValue(trojanVoxelShape);
    }

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 1))
    private void structureLayoutOptimizer$replaceVoxelShape4(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 3) BoundingBox pieceBounds) {
        VoxelShape shape = instance.getValue();
        if (shape instanceof TrojanVoxelShape) {
            ((TrojanVoxelShape) shape).boxOctree.addBox(AABB.of(pieceBounds));
        }
    }

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/Shapes;joinUnoptimized(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape structureLayoutOptimizer$turnOffOldVoxelShapeBehavior1(VoxelShape indexmerger, VoxelShape indexmerger1, BooleanOp indexmerger2) {
        return Shapes.empty();
    }

    ////////////////////////////////

    @Final
    @Shadow
    private Random random;

    @Final
    @Shadow
    private StructureManager structureManager;

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;getShuffledTemplates(Ljava/util/Random;)Ljava/util/List;", ordinal = 0))
    private List<StructurePoolElement> structureLayoutOptimizer$removeDuplicateTemplatePoolElementLists(StructureTemplatePool instance, Random random) {
        if (!StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList) {
            return instance.getShuffledTemplates(random);
        }

        // Linked hashset keeps order of elements.
        ObjectLinkedOpenHashSet<StructurePoolElement> uniquePieces = new ObjectLinkedOpenHashSet<>(((StructureTemplatePoolAccessor) instance).getRawTemplates().size());

        // Don't use addAll. Want to keep it simple in case of inefficiency in collection's addAll.
        // Set will ignore duplicates after first appearance of an element.
        for (StructurePoolElement piece : instance.getShuffledTemplates(random)) {
            //noinspection UseBulkOperation
            uniquePieces.add(piece);
        }

        // Move the elements from set to the list in the same order.
        int uniquePiecesFound = uniquePieces.size();
        List<StructurePoolElement> deduplicatedListOfPieces = new ArrayList<>(uniquePiecesFound);
        for (int i = 0; i < uniquePiecesFound; i++) {
            deduplicatedListOfPieces.add(uniquePieces.removeFirst());
        }

        return deduplicatedListOfPieces;
    }

    @Redirect(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", ordinal = 0))
    private ArrayList<StructurePoolElement> structureLayoutOptimizer$skipDuplicateTemplatePoolElementLists1() {
        // Swap with trojan list, so we can record what pieces we visited
        return StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList ? Lists.newArrayList() : new TrojanArrayList<>();
    }

    @ModifyExpressionValue(
            method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Rotation;getShuffled(Ljava/util/Random;)Ljava/util/List;", ordinal = 0))
    private List<Rotation> structureLayoutOptimizer$skipDuplicateTemplatePoolElementLists2(List<Rotation> original,
                                                                                           @Local(ordinal = 0) List<StructurePoolElement> list,
                                                                                           @Local(ordinal = 1) StructurePoolElement structurepoolelement1)
    {
        if (!StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList && list instanceof TrojanArrayList) {
            TrojanArrayList<StructurePoolElement> trojanArrayList = (TrojanArrayList<StructurePoolElement>) list;
            // Do not run this piece's logic since we already checked its 4 rotations in the past.
            if (trojanArrayList.elementsAlreadyParsed.contains(structurepoolelement1)) {

                // Prime the random with the random calls we would've skipped.
                // Maintains vanilla compat.
                for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
                    structurepoolelement1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
                }

                // Short circuit the Rotation loop
                return new ArrayList<>();
            }
            // Record piece as it will go through the 4 rotation checks for spawning.
            else {
                trojanArrayList.elementsAlreadyParsed.add(structurepoolelement1);
            }
        }

        // Allow the vanilla code to run normally.
        return original;
    }
}