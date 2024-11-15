package telepathicgrunt.structure_layout_optimizer.forge.utils;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShape;

public class TrojanVoxelShape extends VoxelShape {
    public final BoxOctree boxOctree;

    public TrojanVoxelShape(BoxOctree boxOctree) {
        super(new BitSetVoxelShapePart(0 ,0, 0, 0, 0, 0, 0, 0, 0));
        this.boxOctree = boxOctree;
    }

    @Override
    protected DoubleList getCoords(Direction.Axis pAxis) {
        return null;
    }
}
