package telepathicgrunt.structure_layout_optimizer.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * A list that is secretly a palette under the hood since most nbt has many positions sharing the same block.
 * To maintain compat, the palette will make and spit out the original StructureBlockInfo objects when queried with any method.
 * Sort order of list output will be by position starting from 0, 0, 0.
 * <p></p>
 * The palette itself is a modified copy from Glacier.
 * Source: https://github.com/KingContaria/glacier/blob/main/src/main/java/me/contaria/glacier/optimization/memory/structure_block_infos/GlacierStructureBlockInfoList.java#L26
 */
public class PalettedStructureBlockInfoList implements List<StructureTemplate.StructureBlockInfo> {
    private static final long[] EMPTY_DATA = new long[0];
    private static final CompoundTag[] NULL_TAGS = new CompoundTag[]{null};

    protected final long[] data;
    protected final BlockState[] states;
    protected final CompoundTag[] nbts;
    protected final int xBits, yBits, zBits;
    protected final int stateBits, nbtBits;
    protected final int bitsPerEntry;
    protected final int size;

    public PalettedStructureBlockInfoList(List<StructureTemplate.StructureBlockInfo> infos) {
        this(infos, null);
    }

    public PalettedStructureBlockInfoList(List<StructureTemplate.StructureBlockInfo> infos, Predicate<StructureTemplate.StructureBlockInfo> predicate) {
        List<Entry> entries = new ArrayList<>();
        List<BlockState> states = new ArrayList<>();
        List<CompoundTag> tags = new ArrayList<>();

        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;

        for (StructureTemplate.StructureBlockInfo info : infos) {
            if (predicate != null && !predicate.test(info)) {
                continue;
            }

            int state = states.indexOf(info.state());
            if (state == -1) {
                state = states.size();
                states.add(info.state());
            }

            int tag = indexOf(tags, info.nbt());
            if (tag == -1) {
                tag = tags.size();
                tags.add(info.nbt());
            }

            int x = info.pos().getX();
            int y = info.pos().getY();
            int z = info.pos().getZ();

            if (x < 0 || y < 0 || z < 0) {
                throw new RuntimeException("StructureLayoutOptimizer: Invalid StructureBlockInfo position: " + info.pos());
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
            if (z > maxZ) {
                maxZ = z;
            }

            entries.add(new Entry(x, y, z, state, tag));
        }

        this.xBits = bits(maxX);
        this.yBits = bits(maxY);
        this.zBits = bits(maxZ);
        this.stateBits = bits(states.size() - 1);
        this.nbtBits = bits(tags.size() - 1);
        this.bitsPerEntry = this.xBits + this.yBits + this.zBits + this.stateBits + this.nbtBits;

        if (this.bitsPerEntry > 64) {
            throw new RuntimeException("StructureLayoutOptimizer: Too many bits per entry: " + this.bitsPerEntry);
        }

        this.size = entries.size();
        if (this.bitsPerEntry != 0) {
            int entriesPerLong = 64 / this.bitsPerEntry;
            this.data = new long[(this.size + entriesPerLong - 1) / entriesPerLong];
            for (int i = 0; i < this.size; i++) {
                this.data[i / entriesPerLong] |= entries.get(i).compress(this.xBits, this.yBits, this.zBits, this.stateBits) << ((i % entriesPerLong) * this.bitsPerEntry);
            }
        } else {
            this.data = EMPTY_DATA;
        }
        this.states = states.toArray(new BlockState[0]);
        this.nbts = tags.size() == 1 && tags.get(0) == null ? NULL_TAGS : tags.toArray(new CompoundTag[0]);
    }

    private static int bits(int i) {
        int bits = 0;
        while (i >= 1 << bits) {
            bits++;
        }
        return bits;
    }

    private static <T> int indexOf(List<T> list, T o) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == o) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @NotNull
    @Override
    public Iterator<StructureTemplate.StructureBlockInfo> iterator() {
        return new PalettedStructureBlockInfoListIterator(this);
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(StructureTemplate.StructureBlockInfo info) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends StructureTemplate.StructureBlockInfo> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends StructureTemplate.StructureBlockInfo> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StructureTemplate.StructureBlockInfo get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StructureTemplate.StructureBlockInfo set(int index, StructureTemplate.StructureBlockInfo element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, StructureTemplate.StructureBlockInfo element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StructureTemplate.StructureBlockInfo remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ListIterator<StructureTemplate.StructureBlockInfo> listIterator() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public ListIterator<StructureTemplate.StructureBlockInfo> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public List<StructureTemplate.StructureBlockInfo> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private static class Entry {
        private final int x, y, z;
        private final int state, nbt;

        private Entry(int x, int y, int z, int state, int nbt) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.state = state;
            this.nbt = nbt;
        }

        private long compress(int xBits, int yBits, int zBits, int stateBits) {
            return this.x + ((this.y + ((this.z + ((this.state + ((long) this.nbt << stateBits)) << zBits)) << yBits)) << xBits);
        }
    }
}