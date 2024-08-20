package telepathicgrunt.structure_layout_optimizer.utils;

import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public final class GeneralUtils {
    private GeneralUtils() {}

    // More optimized with checking if the jigsaw blocks can connect
    public static boolean canJigsawsAttach(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        FrontAndTop prop1 = jigsaw1.state().getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = jigsaw2.state().getValue(JigsawBlock.ORIENTATION);

        return prop1.front() == prop2.front().getOpposite() &&
                (prop1.top() == prop2.top() || isRollableJoint(jigsaw1, prop1)) &&
                jigsaw1.nbt().getString("target").equals(jigsaw2.nbt().getString("name"));
    }

    private static boolean isRollableJoint(StructureTemplate.StructureBlockInfo jigsaw1, FrontAndTop prop1) {
        String joint = jigsaw1.nbt().getString("joint");
        if(!joint.equals("rollable") && !joint.equals("aligned")) {
            return !prop1.front().getAxis().isHorizontal();
        }
        else {
            return joint.equals("rollable");
        }
    }
}