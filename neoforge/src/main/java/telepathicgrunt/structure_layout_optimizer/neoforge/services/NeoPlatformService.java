package telepathicgrunt.structure_layout_optimizer.neoforge.services;

import telepathicgrunt.structure_layout_optimizer.services.PlatformService;

public class NeoPlatformService implements PlatformService {

    @Override
    public String getFinalizeProcessingMethodName() {
        return "finalizeProcessing";
    }
}
