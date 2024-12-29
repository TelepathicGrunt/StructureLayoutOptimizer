package telepathicgrunt.structure_layout_optimizer.forge.utils;


import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

import java.util.ArrayList;
import java.util.List;

public class BoxOctree {

    private static final int subdivideThreshold = 10;
    private static final int maximumDepth = 3;

    private final AxisAlignedBB boundary;
    private final Vector3i size;
    private final int depth;
    private final List<AxisAlignedBB> innerBoxes = new ArrayList<>();
    private final List<BoxOctree> childrenOctants = new ArrayList<>();

    public BoxOctree(AxisAlignedBB axisAlignedBB) {
        this(axisAlignedBB, 0);
    }

    private BoxOctree(AxisAlignedBB axisAlignedBB, int parentDepth) {
        boundary = axisAlignedBB.move(0, 0, 0); // deep copy
        size = new Vector3i(roundAwayFromZero(boundary.getXsize()), roundAwayFromZero(boundary.getYsize()), roundAwayFromZero(boundary.getZsize()));
        depth = parentDepth + 1;
    }

    private int roundAwayFromZero(double value) {
        return (value >= 0) ? (int)Math.ceil(value) : (int)Math.floor(value);
    }

    private void subdivide() {
        if(!childrenOctants.isEmpty()) {
            throw new UnsupportedOperationException(StructureLayoutOptimizerMod.MODID + " - Tried to subdivide when there are already children octants.");
        }

        int halfXSize = size.getX()/2;
        int halfYSize = size.getY()/2;
        int halfZSize = size.getZ()/2;

        // Lower Left Back Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX, boundary.minY, boundary.minZ,
                boundary.minX + halfXSize, boundary.minY + halfYSize, boundary.minZ + halfZSize),
                depth));

        // Lower Left Front Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX, boundary.minY, boundary.minZ + halfZSize,
                boundary.minX + halfXSize, boundary.minY + halfYSize, boundary.maxZ),
                depth));

        // Lower Right Back Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX + halfXSize, boundary.minY, boundary.minZ,
                boundary.maxX, boundary.minY + halfYSize, boundary.minZ + halfZSize),
                depth));

        // Lower Right Front Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX + halfXSize, boundary.minY, boundary.minZ + halfZSize,
                boundary.maxX, boundary.minY + halfYSize, boundary.maxZ),
                depth));

        // Upper Left Back Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX, boundary.minY + halfYSize, boundary.minZ,
                boundary.minX + halfXSize, boundary.maxY, boundary.minZ + halfZSize),
                depth));

        // Upper Left Front Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX, boundary.minY + halfYSize, boundary.minZ + halfZSize,
                boundary.minX + halfXSize, boundary.maxY, boundary.maxZ),
                depth));

        // Upper Right Back Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX + halfXSize, boundary.minY + halfYSize, boundary.minZ,
                boundary.maxX, boundary.maxY, boundary.minZ + halfZSize),
                depth));

        // Upper Right Front Corner
        childrenOctants.add(new BoxOctree(new AxisAlignedBB(
                boundary.minX + halfXSize, boundary.minY + halfYSize, boundary.minZ + halfZSize,
                boundary.maxX, boundary.maxY, boundary.maxZ),
                depth));

        for (AxisAlignedBB parentInnerBox : innerBoxes) {
            for (BoxOctree octree : childrenOctants) {
                if (octree.boundaryIntersects(parentInnerBox)) {
                    octree.addBox(parentInnerBox);
                }
            }
        }

        innerBoxes.clear();
    }

    public void addBox(AxisAlignedBB axisAlignedBB) {
        if (depth < maximumDepth && innerBoxes.size() > subdivideThreshold) {
            subdivide();
        }

        if (!childrenOctants.isEmpty()) {
            for (BoxOctree octree : childrenOctants) {
                if (octree.boundaryIntersects(axisAlignedBB)) {
                    octree.addBox(axisAlignedBB);
                }
            }
        }
        else {
            // Prevent re-adding the same box if it already exists
            for (AxisAlignedBB parentInnerBox : innerBoxes) {
                if (parentInnerBox.equals(axisAlignedBB)) {
                    return;
                }
            }

            innerBoxes.add(axisAlignedBB);
        }
    }

    public boolean boundaryEntirelyContains(AxisAlignedBB axisAlignedBB) {
        return boundary.contains(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ) &&
                boundary.contains(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    }

    public boolean boundaryIntersects(AxisAlignedBB axisAlignedBB) {
        return boundary.intersects(axisAlignedBB);
    }

    public boolean withinBoundsButNotIntersectingChildren(AxisAlignedBB axisAlignedBB) {
        return this.boundaryEntirelyContains(axisAlignedBB) && !this.intersectsAnyBox(axisAlignedBB);
    }

    public boolean intersectsAnyBox(AxisAlignedBB axisAlignedBB) {
        if (!childrenOctants.isEmpty()) {
            for (BoxOctree octree : childrenOctants) {
                if (octree.boundaryIntersects(axisAlignedBB) && octree.intersectsAnyBox(axisAlignedBB)) {
                    return true;
                }
            }
        }
        else {
            for (AxisAlignedBB innerBox : innerBoxes) {
                if (innerBox.intersects(axisAlignedBB)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean boundaryContains(BlockPos position) {
        return boundary.contains(position.getX(), position.getY(), position.getZ());
    }

    public boolean withinAnyBox(BlockPos position) {
        if (!childrenOctants.isEmpty()) {
            for (BoxOctree octree : childrenOctants) {
                if (octree.boundaryContains(position) && octree.withinAnyBox(position)) {
                    return true;
                }
            }
        }
        else {
            for (AxisAlignedBB innerBox : innerBoxes) {
                if (innerBox.contains(position.getX(), position.getY(), position.getZ())) {
                    return true;
                }
            }
        }

        return false;
    }
}
