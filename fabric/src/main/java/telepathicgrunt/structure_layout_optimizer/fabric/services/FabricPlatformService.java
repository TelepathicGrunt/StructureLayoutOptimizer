package telepathicgrunt.structure_layout_optimizer.fabric.services;

import net.fabricmc.loader.api.FabricLoader;
import telepathicgrunt.structure_layout_optimizer.services.PlatformService;

public class FabricPlatformService implements PlatformService {

    @Override
    public String getFinalizeProcessingMethodName() {
        return FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_3491", "method_49887", "(Lnet/minecraft/class_5425;Lnet/minecraft/class_2338;Lnet/minecraft/class_2338;Ljava/util/List;Ljava/util/List;Lnet/minecraft/class_3492;)Ljava/util/List;");
    }
}
