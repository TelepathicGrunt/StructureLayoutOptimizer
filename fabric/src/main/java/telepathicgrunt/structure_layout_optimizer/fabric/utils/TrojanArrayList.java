package telepathicgrunt.structure_layout_optimizer.fabric.utils;

import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrojanArrayList<E extends @Nullable Object> extends ArrayList<E> {
    public final Set<StructurePoolElement> elementsAlreadyParsed = new HashSet<>();
}
