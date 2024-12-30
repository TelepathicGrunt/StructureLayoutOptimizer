package telepathicgrunt.structure_layout_optimizer.fabric.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.calculateRelativePosition;
import static net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.processBlockInfos;

public class StructureTemplateOptimizer {

    public static @NotNull List<StructureTemplate.StructureBlockInfo> getStructureBlockInfosInBounds(
            LevelAccessor level,
            BlockPos offset,
            BlockPos offset2,
            StructurePlaceSettings structurePlaceSettings,
            List<StructureTemplate.StructureBlockInfo> originalPositions)
    {

        BoundingBox boundingBox = structurePlaceSettings.getBoundingBox();
        if (boundingBox == null) {
            return originalPositions;
        }

        Mirror mirror = structurePlaceSettings.getMirror();
        Rotation rotation = structurePlaceSettings.getRotation();
        BlockPos pivot = structurePlaceSettings.getRotationPivot();

        List<StructureTemplate.StructureBlockInfo> listOfInBoundsRelativePositions = new ArrayList<>();
        List<StructureTemplate.StructureBlockInfo> listOfWorldPositions = new ArrayList<>();
        BlockPos.MutableBlockPos mutableWorldPosition = new BlockPos.MutableBlockPos();

        for (StructureTemplate.StructureBlockInfo relativeBlockInfo : originalPositions) {

            // vanilla position transforms
            BlockPos test = calculateRelativePosition(structurePlaceSettings, relativeBlockInfo.pos).offset(offset);

            // my mutable position transforms
            mutableWorldPosition.set(relativeBlockInfo.pos);
            transform(mutableWorldPosition, mirror, rotation, pivot);
            mutableWorldPosition.move(offset);

            // verify the transforms are correct
            if (test.getX() != mutableWorldPosition.getX() || test.getY() != mutableWorldPosition.getY() || test.getZ() != mutableWorldPosition.getZ()) {
                int breakpointHereNeverTriggers = 5;
            }

            StructureTemplate.StructureBlockInfo worldBlockInfo = new StructureTemplate.StructureBlockInfo(
                    mutableWorldPosition.immutable(), relativeBlockInfo.state, relativeBlockInfo.nbt != null ? relativeBlockInfo.nbt.copy() : null
            );

            listOfWorldPositions.add(worldBlockInfo);

            // Using the world positions for the bounds check. Same as line 229 in StructureTemplate's placeInWorld method (yarn)
            // World position is same as vanilla's processBlockInfos method result.
            if (structurePlaceSettings.getBoundingBox().isInside(worldBlockInfo.pos)) {
                listOfInBoundsRelativePositions.add(relativeBlockInfo);
            }
        }

        List<StructureTemplate.StructureBlockInfo> vanillaWorldBlockInfos = processBlockInfos(level, offset, offset2, structurePlaceSettings, originalPositions);

        // Proves again that listOfWorldPositions is EXACTLY the same as processBlockInfos result down to the world position being used for bounds check.
        List<StructureTemplate.StructureBlockInfo> worldDiffListFromVanilla = new ArrayList<>(vanillaWorldBlockInfos);
        for (StructureTemplate.StructureBlockInfo worldVanillaBlockInfo : vanillaWorldBlockInfos) {
            for (StructureTemplate.StructureBlockInfo worldBlockInfo : listOfWorldPositions) {
                if (worldVanillaBlockInfo.pos.equals(worldBlockInfo.pos)) {
                    worldDiffListFromVanilla.remove(worldVanillaBlockInfo);
                }
            }
        }

        // The check to verify that yes, we transformed the positions correctly to match vanilla exactly.
        if (!worldDiffListFromVanilla.isEmpty()) {
            int breakpointHereNeverTriggers = 5;
        }

        List<StructureTemplate.StructureBlockInfo> vanillaFilteredWorldBlockInfos = new ArrayList<>();
        for (StructureTemplate.StructureBlockInfo worldVanillaBlockInfo : vanillaWorldBlockInfos) {
            if (structurePlaceSettings.getBoundingBox().isInside(worldVanillaBlockInfo.pos)) {
                vanillaFilteredWorldBlockInfos.add(worldVanillaBlockInfo);
            }
        }

        // Check to make sure that vanilla's filtered result will be the same size list. Proves we filtered the correct number of positions.
        if (vanillaFilteredWorldBlockInfos.size() != listOfInBoundsRelativePositions.size()) {
            int breakpointHereNeverTriggers = 5;
        }

        // Using seed -4673899320199289746, go to village here to see missing center well: /tp @s -432 ~ -144
        return listOfInBoundsRelativePositions;
    }

    private static void transform(BlockPos.MutableBlockPos mutableBlockPos, Mirror mirror, Rotation rotation, BlockPos pivot) {
        int i = mutableBlockPos.getX();
        int j = mutableBlockPos.getY();
        int k = mutableBlockPos.getZ();
        boolean flag = true;
        switch (mirror) {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        int l = pivot.getX();
        int i1 = pivot.getZ();
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
                mutableBlockPos.set(l - i1 + k, j, l + i1 - i);
                return;
            case CLOCKWISE_90:
                mutableBlockPos.set(l + i1 - k, j, i1 - l + i);
                return;
            case CLOCKWISE_180:
                mutableBlockPos.set(l + l - i, j, i1 + i1 - k);
                return;
            default:
                if (flag) mutableBlockPos.set(i, j, k);
        }
    }
}
