package telepathicgrunt.structure_layout_optimizer.forge.utils;

import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrojanArrayList<E extends @Nullable Object> extends ArrayList<E> {
    public final Set<JigsawPiece> elementsAlreadyParsed = new HashSet<>();
}
