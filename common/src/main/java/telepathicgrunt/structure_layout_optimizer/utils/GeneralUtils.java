package telepathicgrunt.structure_layout_optimizer.utils;

import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class GeneralUtils {
    private GeneralUtils() {}

    // More optimized with checking if the jigsaw blocks can connect
    public static boolean canJigsawsAttach(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        FrontAndTop prop1 = jigsaw1.state().getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = jigsaw2.state().getValue(JigsawBlock.ORIENTATION);

        return prop1.front() == prop2.front().getOpposite() &&
                (prop1.top() == prop2.top() || isRollableJoint(jigsaw1, prop1)) &&
                getStringMicroOptimised(jigsaw1.nbt(), "target").equals(getStringMicroOptimised(jigsaw2.nbt(), "name"));
    }

    private static boolean isRollableJoint(StructureTemplate.StructureBlockInfo jigsaw1, FrontAndTop prop1) {
        String joint = getStringMicroOptimised(jigsaw1.nbt(), "joint");
        if(!joint.equals("rollable") && !joint.equals("aligned")) {
            return !prop1.front().getAxis().isHorizontal();
        }
        else {
            return joint.equals("rollable");
        }
    }

    // From Thailkil by reducing grabbing of the entry by half
    public static String getStringMicroOptimised(CompoundTag tag, String key) {
        return tag.get(key) instanceof StringTag stringTag ? stringTag.getAsString() : "";
    }
}
