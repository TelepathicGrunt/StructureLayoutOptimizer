package telepathicgrunt.structure_layout_optimizer.forge.services;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import telepathicgrunt.structure_layout_optimizer.services.PlatformService;

public class ForgePlatformService implements PlatformService {

    @Override
    public String getFinalizeProcessingMethodName() {
        return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, "m_276976_");
    }
}
