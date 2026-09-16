package telepathicgrunt.structure_layout_optimizer.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public final class GeneralUtils {
    private GeneralUtils() {}

    public static <T> T loadService(Class<T> service) {
        return ServiceLoader.load(service, service.getClassLoader()).findFirst().orElseThrow(() -> new IllegalStateException("No platform implementation found for " + service.getName()));
    }

    public static void shuffleAndPrioritize(List<StructureTemplate.JigsawBlockInfo> list, RandomSource random) {
        Int2ObjectArrayMap<List<StructureTemplate.JigsawBlockInfo>> buckets = new Int2ObjectArrayMap<>();

        // Add entries to the bucket
        for (StructureTemplate.JigsawBlockInfo structureBlockInfo : list) {
            int key = structureBlockInfo.selectionPriority();
            buckets.computeIfAbsent(key, _ -> new ArrayList<>()).add(structureBlockInfo);
        }

        // Shuffle the entries in the bucket
        for (List<StructureTemplate.JigsawBlockInfo> bucketList : buckets.values()) {
            Util.shuffle(bucketList, random);
        }

        if (buckets.size() == 1) {
            list.clear();
            copyAll(buckets.int2ObjectEntrySet().fastIterator().next().getValue(), list);
        }
        else if (buckets.size() > 1) {
            // Priorities found. Concat them into a single new master list in reverse order to match vanilla behavior
            list.clear();

            IntArrayList keys = new IntArrayList(buckets.keySet());
            keys.sort(IntComparators.OPPOSITE_COMPARATOR);

            for (int i = 0; i < keys.size(); i++) {
                copyAll(buckets.get(keys.getInt(i)), list);
            }
        }
    }

    public static String getStringMicroOptimised(CompoundTag tag, String key) {
        return tag.get(key) instanceof StringTag(String value) ? value : "";
    }

    // From XFactHD at https://github.com/XFactHD/FramedBlocks/blob/d89311e31d55630b5a9cecd3b8f75abcf9693ce0/src/main/java/xfacthd/framedblocks/api/util/Utils.java#L396-L409
    public static <T> void copyAll(List<T> src, List<T> dest) {
        // Do not listen to IDE. This is faster than addAll
        for (int i = 0; i < src.size(); i++) {
            dest.add(src.get(i));
        }
    }
}
