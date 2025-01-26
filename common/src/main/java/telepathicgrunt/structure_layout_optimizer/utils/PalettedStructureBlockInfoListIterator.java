package telepathicgrunt.structure_layout_optimizer.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A list that is secretly a palette under the hood since most nbt has many positions sharing the same block.
 * To maintain compat, the palette will make and spit out the original StructureBlockInfo objects when queried with any method.
 * Sort order of list output will be by position starting from 0, 0, 0.
 * <p></p>
 * The palette itself is a modified copy from Glacier.
 * Source: https://github.com/KingContaria/glacier/blob/main/src/main/java/me/contaria/glacier/optimization/memory/structure_block_infos/GlacierStructureBlockInfoList.java#L26
 */
public class PalettedStructureBlockInfoListIterator implements Iterator<StructureTemplate.StructureBlockInfo> {
    private final PalettedStructureBlockInfoList infos;

    private final int xOffset, yOffset, zOffset, stateOffset;
    private final int xMask, yMask, zMask, stateMask, tagMask;

    private int index = 0;

    public PalettedStructureBlockInfoListIterator(PalettedStructureBlockInfoList infos) {
        this.infos = infos;
        this.xOffset = infos.xBits;
        this.yOffset = this.xOffset + infos.yBits;
        this.zOffset = this.yOffset + infos.zBits;
        this.stateOffset = this.zOffset + infos.stateBits;
        this.xMask = (1 << infos.xBits) - 1;
        this.yMask = (1 << infos.yBits) - 1;
        this.zMask = (1 << infos.zBits) - 1;
        this.stateMask = (1 << infos.stateBits) - 1;
        this.tagMask = (1 << infos.nbtBits) - 1;
    }

    @Override
    public boolean hasNext() {
        return this.index < this.size();
    }

    @Override
    public StructureTemplate.StructureBlockInfo next() {
        int index = this.toIndex(this.index++);
        if (index >= this.infos.size) {
            throw new IndexOutOfBoundsException();
        }

        int bitsPerEntry = this.infos.bitsPerEntry;
        if (bitsPerEntry == 0) {
            // The position is guaranteed to be {0, 0, 0}
            // New object in case other mods save reference to it or store it in their own list.
            return new StructureTemplate.StructureBlockInfo(new BlockPos(0, 0, 0), this.infos.states[0], this.infos.nbts[0]);
        }

        int entriesPerLong = 64 / bitsPerEntry;
        long entry = this.infos.data[index / entriesPerLong] >>> ((index % entriesPerLong) * bitsPerEntry);

        int x = (int) (entry & this.xMask);
        int y = (int) (entry >> this.xOffset & this.yMask);
        int z = (int) (entry >> this.yOffset & this.zMask);
        int state = (int) (entry >> this.zOffset & this.stateMask);
        int tag = (int) (entry >> this.stateOffset & this.tagMask);

        // New object in case other mods save reference to it or store it in their own list.
        return new StructureTemplate.StructureBlockInfo(new BlockPos(x, y, z), this.infos.states[state], this.infos.nbts[tag]);
    }

    protected int toIndex(int index) {
        return index;
    }

    protected int size() {
        return this.infos.size;
    }
}