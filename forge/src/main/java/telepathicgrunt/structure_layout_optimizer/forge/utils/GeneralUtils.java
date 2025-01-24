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
        JigsawOrientation prop1 = jigsaw1.state.get(JigsawBlock.field_235506_a_);
        JigsawOrientation prop2 = jigsaw2.state.get(JigsawBlock.field_235506_a_);

        return prop1.func_239642_b_() == prop2.func_239642_b_().getOpposite() &&
                (prop1.func_239644_c_() == prop2.func_239644_c_() || isRollableJoint(jigsaw1, prop1)) &&
                getStringMicroOptimised(jigsaw1.nbt, "target").equals(getStringMicroOptimised(jigsaw2.nbt, "name"));
    }

    private static boolean isRollableJoint(Template.BlockInfo jigsaw1, JigsawOrientation prop1) {
        String joint = getStringMicroOptimised(jigsaw1.nbt, "joint");
        if(!joint.equals("rollable") && !joint.equals("aligned")) {
            return !prop1.func_239642_b_().getAxis().isHorizontal();
        }
        else {
            return joint.equals("rollable");
        }
    }

    // From Thailkil by reducing grabbing of the entry by half
    public static String getStringMicroOptimised(CompoundNBT tag, String key) {
        INBT value = tag.get(key);
        return value instanceof StringNBT ? value.getString() : "";
    }
}
