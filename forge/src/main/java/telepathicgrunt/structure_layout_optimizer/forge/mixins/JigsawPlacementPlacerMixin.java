package telepathicgrunt.structure_layout_optimizer.forge.mixins;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;
import telepathicgrunt.structure_layout_optimizer.forge.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.forge.utils.GeneralUtils;
import telepathicgrunt.structure_layout_optimizer.forge.utils.TrojanArrayList;
import telepathicgrunt.structure_layout_optimizer.forge.utils.TrojanVoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(JigsawManager.Assembler.class)
public class JigsawPlacementPlacerMixin {

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/JigsawBlock;canAttach(Lnet/minecraft/world/gen/feature/template/Template$BlockInfo;Lnet/minecraft/world/gen/feature/template/Template$BlockInfo;)Z"))
    private boolean structureLayoutOptimizer$optimizeJigsawConnecting(Template.BlockInfo jigsaw1, Template.BlockInfo jigsaw2) {
        return GeneralUtils.canJigsawsAttach(jigsaw1, jigsaw2);
    }

    ////////////////////////////////

    @ModifyExpressionValue(method = "tryPlacingChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/jigsaw/JigsawPiece;getShuffledJigsawBlocks(Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Rotation;Ljava/util/Random;)Ljava/util/List;", ordinal = 1))
    private List<Template.BlockInfo> structureLayoutOptimizer$skipBlockedJigsaws(
            List<Template.BlockInfo> original,
            @Local(ordinal = 0, argsOnly = true) boolean useExpansionHack,
            @Local(ordinal = 2) MutableObject<VoxelShape> voxelShapeMutableObject,
            @Local(ordinal = 1) JigsawPiece structurePoolElement,
            @Local(ordinal = 0) Template.BlockInfo parentJigsawBlockInfo,
            @Local(ordinal = 2) BlockPos parentTargetPosition)
    {
        if (voxelShapeMutableObject.getValue() instanceof TrojanVoxelShape) {
            TrojanVoxelShape trojanVoxelShape = ((TrojanVoxelShape) voxelShapeMutableObject.getValue());
            // If rigid and target position is already an invalid spot, do not run rest of logic.
            JigsawPattern.PlacementBehaviour candidatePlacementBehavior = structurePoolElement.getProjection();
            boolean isCandidateRigid = candidatePlacementBehavior == JigsawPattern.PlacementBehaviour.RIGID;
            if (isCandidateRigid && (!trojanVoxelShape.boxOctree.boundaryContains(parentTargetPosition) || trojanVoxelShape.boxOctree.withinAnyBox(parentTargetPosition))) {
                return new ArrayList<>();
            }
        }
        return original;
    }

    /// /////////////////////////////

    @WrapOperation(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;joinIsNotEmpty(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/IBooleanFunction;)Z"))
    private boolean structureLayoutOptimizer$replaceVoxelShape2(VoxelShape parentBounds, VoxelShape pieceShape, IBooleanFunction booleanOp, Operation<Boolean> original, @Local(ordinal = 3) MutableBoundingBox pieceBounds) {
        if (parentBounds instanceof TrojanVoxelShape) {
            AxisAlignedBB pieceAABB = AxisAlignedBB.of(pieceBounds).deflate(0.25D);

            // Have to inverse because of an ! outside our wrap
            return !((TrojanVoxelShape) parentBounds).boxOctree.withinBoundsButNotIntersectingChildren(pieceAABB);
        }

        return original.call(parentBounds, pieceShape, booleanOp);
    }

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 0))
    private void structureLayoutOptimizer$replaceVoxelShape3(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 0) MutableBoundingBox pieceBounds) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(AxisAlignedBB.of(pieceBounds)));
        instance.setValue(trojanVoxelShape);
    }

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V", ordinal = 1))
    private void structureLayoutOptimizer$replaceVoxelShape4(MutableObject<VoxelShape> instance, Object value, @Local(ordinal = 3) MutableBoundingBox pieceBounds) {
        VoxelShape shape = instance.getValue();
        if (shape instanceof TrojanVoxelShape) {
            ((TrojanVoxelShape) shape).boxOctree.addBox(AxisAlignedBB.of(pieceBounds));
        }
    }

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;joinUnoptimized(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/IBooleanFunction;)Lnet/minecraft/util/math/shapes/VoxelShape;"))
    private VoxelShape structureLayoutOptimizer$turnOffOldVoxelShapeBehavior1(VoxelShape indexmerger, VoxelShape indexmerger1, IBooleanFunction indexmerger2) {
        return VoxelShapes.empty();
    }

    /// /////////////////////////////

    @Final
    @Shadow(aliases = {"field_214887_f", "rand"})
    private Random random;

    @Final
    @Shadow(aliases = {"field_214885_d", "structureManager"})
    private TemplateManager structureManager;

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/jigsaw/JigsawPattern;getShuffledTemplates(Ljava/util/Random;)Ljava/util/List;", ordinal = 0))
    private List<JigsawPiece> structureLayoutOptimizer$removeDuplicateTemplatePoolElementLists(JigsawPattern instance, Random random) {
        if (!StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList) {
            return instance.getShuffledTemplates(random);
        }

        // Linked hashset keeps order of elements.
        ObjectLinkedOpenHashSet<JigsawPiece> uniquePieces = new ObjectLinkedOpenHashSet<>(((StructureTemplatePoolAccessor) instance).getRawTemplates().size());

        // Don't use addAll. Want to keep it simple in case of inefficiency in collection's addAll.
        // Set will ignore duplicates after first appearance of an element.
        for (JigsawPiece piece : instance.getShuffledTemplates(random)) {
            //noinspection UseBulkOperation
            uniquePieces.add(piece);
        }

        // Move the elements from set to the list in the same order.
        int uniquePiecesFound = uniquePieces.size();
        List<JigsawPiece> deduplicatedListOfPieces = new ArrayList<>(uniquePiecesFound);
        for (int i = 0; i < uniquePiecesFound; i++) {
            deduplicatedListOfPieces.add(uniquePieces.removeFirst());
        }

        return deduplicatedListOfPieces;
    }

    @Redirect(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", ordinal = 0))
    private ArrayList<JigsawPiece> structureLayoutOptimizer$skipDuplicateTemplatePoolElementLists1() {
        // Swap with trojan list, so we can record what pieces we visited
        return StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList ? Lists.newArrayList() : new TrojanArrayList<>();
    }

    @ModifyExpressionValue(method = "tryPlacingChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rotation;getShuffled(Ljava/util/Random;)Ljava/util/List;", ordinal = 0))
    private List<Rotation> structureLayoutOptimizer$skipDuplicateTemplatePoolElementLists2(List<Rotation> original, @Local(ordinal = 0) List<JigsawPiece> list, @Local(ordinal = 1) JigsawPiece structurepoolelement1) {
        if (!StructureLayoutOptimizerMod.getConfig().deduplicateShuffledTemplatePoolElementList && list instanceof TrojanArrayList) {
            TrojanArrayList<JigsawPiece> trojanArrayList = (TrojanArrayList<JigsawPiece>) list;
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
