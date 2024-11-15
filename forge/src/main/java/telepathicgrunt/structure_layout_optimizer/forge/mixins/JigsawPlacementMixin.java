package telepathicgrunt.structure_layout_optimizer.forge.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import telepathicgrunt.structure_layout_optimizer.forge.utils.BoxOctree;
import telepathicgrunt.structure_layout_optimizer.forge.utils.TrojanVoxelShape;

import java.util.List;
import java.util.Random;

@Mixin(JigsawManager.class)
public class JigsawPlacementMixin {

    @Unique
    private static final ThreadLocal<AxisAlignedBB> slo$aabb = new ThreadLocal<>();

    @Unique
    private static final ThreadLocal<MutableBoundingBox> slo$mutableBB = new ThreadLocal<>();

    @Inject(method = "addPieces(Lnet/minecraft/util/registry/DynamicRegistries;Lnet/minecraft/world/gen/feature/structure/VillageConfig;Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/util/math/BlockPos;Ljava/util/List;Ljava/util/Random;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;join(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/IBooleanFunction;)Lnet/minecraft/util/math/shapes/VoxelShape;"))
    private static void structureLayoutOptimizer$replaceVoxelShapeGetLocals(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_, JigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_, TemplateManager p_242837_4_, BlockPos p_242837_5_, List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_, boolean p_242837_8_, boolean p_242837_9_, CallbackInfo ci, @Local MutableBoundingBox mutableboundingbox, @Local AxisAlignedBB axisalignedbb) {
        slo$aabb.set(axisalignedbb);
        slo$mutableBB.set(mutableboundingbox);
    }


    @Redirect(method = "addPieces(Lnet/minecraft/util/registry/DynamicRegistries;Lnet/minecraft/world/gen/feature/structure/VillageConfig;Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/util/math/BlockPos;Ljava/util/List;Ljava/util/Random;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/shapes/VoxelShapes;join(Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/VoxelShape;Lnet/minecraft/util/math/shapes/IBooleanFunction;)Lnet/minecraft/util/math/shapes/VoxelShape;"))
    private static VoxelShape structureLayoutOptimizer$replaceVoxelShape1(VoxelShape pShape1, VoxelShape pShape2, IBooleanFunction pFunction) {
        TrojanVoxelShape trojanVoxelShape = new TrojanVoxelShape(new BoxOctree(slo$aabb.get()));
        trojanVoxelShape.boxOctree.addBox(AxisAlignedBB.of(slo$mutableBB.get()));
        slo$aabb.remove();
        slo$mutableBB.remove();
        return trojanVoxelShape;
    }
}
