package telepathicgrunt.structure_layout_optimizer.forge.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import telepathicgrunt.structure_layout_optimizer.forge.utils.StructureTemplateOptimizer;

import java.util.List;

@Mixin(Template.class)
public class StructureTemplateMixin {

    @Redirect(method = "placeInWorld(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Ljava/util/Random;I)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/template/Template$Palette;blocks()Ljava/util/List;"))
    private List<Template.BlockInfo> structureLayoutOptimizer$shrinkStructureTemplateBlocksList(Template.Palette instance, @Local(ordinal = 0, argsOnly = true) BlockPos offset, @Local(ordinal = 0, argsOnly = true) PlacementSettings settings) {
        return StructureTemplateOptimizer.getStructureBlockInfosInBounds(instance, offset, settings);
    }
}
