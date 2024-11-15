package telepathicgrunt.structure_layout_optimizer.forge.utils;

import net.minecraft.block.JigsawBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraft.world.gen.feature.template.Template;

public final class GeneralUtils {
    private GeneralUtils() {}

    // More optimized with checking if the jigsaw blocks can connect
    public static boolean canJigsawsAttach(Template.BlockInfo jigsaw1, Template.BlockInfo jigsaw2) {
        JigsawOrientation prop1 = jigsaw1.state.getValue(JigsawBlock.ORIENTATION);
        JigsawOrientation prop2 = jigsaw2.state.getValue(JigsawBlock.ORIENTATION);

        return prop1.front() == prop2.front().getOpposite() &&
                (prop1.top() == prop2.top() || isRollableJoint(jigsaw1, prop1)) &&
                getStringMicroOptimised(jigsaw1.nbt, "target").equals(getStringMicroOptimised(jigsaw2.nbt, "name"));
    }

    private static boolean isRollableJoint(Template.BlockInfo jigsaw1, JigsawOrientation prop1) {
        String joint = getStringMicroOptimised(jigsaw1.nbt, "joint");
        if(!joint.equals("rollable") && !joint.equals("aligned")) {
            return !prop1.front().getAxis().isHorizontal();
        }
        else {
            return joint.equals("rollable");
        }
    }

    // From Thailkil by reducing grabbing of the entry by half
    public static String getStringMicroOptimised(CompoundNBT tag, String key) {
        INBT value = tag.get(key);
        return value instanceof StringNBT ? value.getAsString() : "";
    }
}
