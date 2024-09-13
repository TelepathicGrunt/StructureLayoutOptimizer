package telepathicgrunt.structure_layout_optimizer.utils;

import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrojanArrayList<E extends @Nullable Object> extends ArrayList<E> {
    public final Set<StructurePoolElement> elementsAlreadyParsed = new HashSet<>();
}
