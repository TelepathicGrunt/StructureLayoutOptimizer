package telepathicgrunt.structure_layout_optimizer.utils;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrojanVoxelShape extends VoxelShape {
    public final BoxOctree boxOctree;

    public TrojanVoxelShape(BoxOctree boxOctree) {
        super(BitSetDiscreteVoxelShape.withFilledBounds(0 ,0, 0, 0, 0, 0, 0, 0, 0));
        this.boxOctree = boxOctree;
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return null;
    }
}
