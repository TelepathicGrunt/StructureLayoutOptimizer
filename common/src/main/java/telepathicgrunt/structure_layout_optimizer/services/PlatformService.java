package telepathicgrunt.structure_layout_optimizer.services;

import telepathicgrunt.structure_layout_optimizer.utils.GeneralUtils;

public interface PlatformService {

    PlatformService INSTANCE = GeneralUtils.loadService(PlatformService.class);

    String getFinalizeProcessingMethodName();
}
