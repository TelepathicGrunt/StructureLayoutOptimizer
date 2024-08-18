package telepathicgrunt.structure_layout_optimizer;

import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class GeneralUtils {
    private GeneralUtils() {}

    // More optimized with checking if the jigsaw blocks can connect
    public static boolean canJigsawsAttach(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        FrontAndTop prop1 = jigsaw1.state().getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = jigsaw2.state().getValue(JigsawBlock.ORIENTATION);
        String joint = jigsaw1.nbt().getString("joint");
        if(joint.isEmpty()) {
            joint = prop1.front().getAxis().isHorizontal() ? "aligned" : "rollable";
        }

        return prop1.front() == prop2.front().getOpposite() &&
                (joint.equals("rollable") || prop1.top() == prop2.top()) &&
                jigsaw1.nbt().getString("target").equals(jigsaw2.nbt().getString("name"));
    }
}