package telepathicgrunt.structure_layout_optimizer.fabric.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StructureTemplateOptimizer {

    public static @NotNull List<StructureTemplate.StructureBlockInfo> getStructureBlockInfosInBounds(StructureTemplate.Palette instance, BlockPos offset, StructurePlaceSettings structurePlaceSettings) {
        BoundingBox boundingBox = structurePlaceSettings.getBoundingBox();
        if (boundingBox == null) {
            return instance.blocks();
        }

        Mirror mirror = structurePlaceSettings.getMirror();
        Rotation rotation = structurePlaceSettings.getRotation();
        BlockPos pivot = structurePlaceSettings.getRotationPivot();

        List<StructureTemplate.StructureBlockInfo> list = new ArrayList<>();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (StructureTemplate.StructureBlockInfo blockInfo : instance.blocks()) {
            mutableBlockPos.set(blockInfo.pos);
            transform(mutableBlockPos, mirror, rotation, pivot);
            mutableBlockPos.move(offset);

            if (boundingBox.isInside(mutableBlockPos)) {
                list.add(blockInfo);
            }
        }

        return list;
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
